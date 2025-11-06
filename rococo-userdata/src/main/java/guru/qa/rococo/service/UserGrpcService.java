package guru.qa.rococo.service;

import guru.qa.rococo.data.UserEntity;
import guru.qa.rococo.data.repository.UserRepository;
import guru.qa.rococo.ex.UserNotFoundException;
import guru.qa.rococo.grpc.GetUserRequest;
import guru.qa.rococo.grpc.RococoUserServiceGrpc;
import guru.qa.rococo.grpc.UpdateUserRequest;
import guru.qa.rococo.grpc.User;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.grpc.server.service.GrpcService;

import java.nio.charset.StandardCharsets;

@GrpcService
public class UserGrpcService extends RococoUserServiceGrpc.RococoUserServiceImplBase {

    private final UserRepository userRepository;

    @Autowired
    public UserGrpcService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void getUser(GetUserRequest request, StreamObserver<User> responseObserver) {
        UserEntity userEntity = userRepository.findByUsername(request.getUsername())
                .orElseThrow(UserNotFoundException::new);
        responseObserver.onNext(convertToGrpcUser(userEntity));
        responseObserver.onCompleted();
    }

    @Override
    public void updateUser(UpdateUserRequest request, StreamObserver<User> responseObserver) {
        UserEntity userEntity = userRepository.findByUsername(request.getUsername())
                .orElseThrow(UserNotFoundException::new);

        userEntity.setFirstname(request.getFirstname());
        userEntity.setLastname(request.getLastname());

        if (!request.getAvatar().isEmpty()) {
            userEntity.setAvatar(request.getAvatar().getBytes(StandardCharsets.UTF_8));
        } else {
            userEntity.setAvatar(null);
        }

        UserEntity savedUser = userRepository.save(userEntity);
        responseObserver.onNext(convertToGrpcUser(savedUser));
        responseObserver.onCompleted();
    }

    private User convertToGrpcUser(UserEntity entity) {
        User.Builder builder = User.newBuilder()
                .setId(entity.getId().toString())
                .setUsername(entity.getUsername())
                .setFirstname(entity.getFirstname() != null ? entity.getFirstname() : "")
                .setLastname(entity.getLastname() != null ? entity.getLastname() : "");

        if (entity.getAvatar() != null && entity.getAvatar().length > 0) {
            builder.setAvatar(new String(entity.getAvatar(), StandardCharsets.UTF_8));
        }

        return builder.build();
    }
}
