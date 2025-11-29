package guru.qa.rococo.test.grpc;

import guru.qa.rococo.jupiter.annotation.ApiLogin;
import guru.qa.rococo.jupiter.annotation.GrpcTest;
import guru.qa.rococo.jupiter.annotation.User;
import guru.qa.rococo.model.userdata.UserJson;
import guru.qa.rococo.service.grpc.UserGrpcClient;
import guru.qa.rococo.service.impl.UsersDbClient;
import guru.qa.rococo.utils.RandomDataUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@GrpcTest
public class UserdataGrpcTest {

    private final UserGrpcClient userGrpcClient = new UserGrpcClient();

    private final UsersDbClient usersDbClient = new UsersDbClient();

    @Test
    @User
    @ApiLogin
    void shouldReturnCreateUser(UserJson user) {
        UserJson response = userGrpcClient.getUser(user.username());
        assertAll(
                () -> assertEquals(user.id(), response.id()),
                () -> assertEquals(user.username(), response.username()),
                () -> assertEquals(user.firstname(), response.firstname()),
                () -> assertEquals(user.lastname(), response.lastname()),
                () -> assertEquals(user.avatar(), response.avatar())
        );
    }

    @Test
    @User
    void shouldUpdateUser(UserJson user) {
        UserJson updatedUser = new UserJson(
                user.id(),
                user.username(),
                RandomDataUtils.randomFirstName(),
                RandomDataUtils.randomLastName(),
                user.avatar(),
                user.testData()
        );

        UserJson response = userGrpcClient.updateUser(updatedUser);
        UserJson expectedUser = usersDbClient.getUserById(updatedUser.id());
        assertAll(
                () -> assertEquals(expectedUser.id(), response.id()),
                () -> assertEquals(expectedUser.username(), response.username()),
                () -> assertEquals(expectedUser.firstname(), response.firstname()),
                () -> assertEquals(expectedUser.lastname(), response.lastname()),
                () -> assertEquals(expectedUser.avatar(), response.avatar())
        );
    }
}
