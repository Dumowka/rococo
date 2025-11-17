package guru.qa.rococo.service.api.gprc;

import guru.qa.rococo.grpc.GetUserRequest;
import guru.qa.rococo.grpc.RococoUserServiceGrpc;
import guru.qa.rococo.grpc.UpdateUserRequest;
import guru.qa.rococo.grpc.User;
import guru.qa.rococo.model.UserJson;
import guru.qa.rococo.service.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static guru.qa.rococo.model.UserJson.fromGrpcUser;

@Service
public class UserGrpcService implements UserService {

    private final RococoUserServiceGrpc.RococoUserServiceBlockingStub userServiceStub;

    @Autowired
    public UserGrpcService(RococoUserServiceGrpc.RococoUserServiceBlockingStub userServiceStub) {
        this.userServiceStub = userServiceStub;
    }

    @Override
    public UserJson getUser(String username) {
        GetUserRequest request = GetUserRequest.newBuilder()
                .setUsername(username)
                .build();

        User response = userServiceStub.getUser(request);
        return fromGrpcUser(response);
    }

    @Override
    public UserJson updateUser(UserJson user) {
        UpdateUserRequest.Builder requestBuilder = UpdateUserRequest.newBuilder()
                .setUsername(user.username())
                .setFirstname(user.firstname() != null ? user.firstname() : "")
                .setLastname(user.lastname() != null ? user.lastname() : "");

        if (user.avatar() != null) {
            requestBuilder.setAvatar(user.avatar());
        }

        User response = userServiceStub.updateUser(requestBuilder.build());
        return fromGrpcUser(response);
    }
}
