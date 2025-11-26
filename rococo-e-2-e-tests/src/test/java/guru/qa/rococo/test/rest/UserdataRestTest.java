package guru.qa.rococo.test.rest;

import guru.qa.rococo.jupiter.annotation.ApiLogin;
import guru.qa.rococo.jupiter.annotation.RestTest;
import guru.qa.rococo.jupiter.annotation.Token;
import guru.qa.rococo.jupiter.annotation.User;
import guru.qa.rococo.jupiter.extension.ApiLoginExtension;
import guru.qa.rococo.model.userdata.UserJson;
import guru.qa.rococo.service.impl.GatewayApiClient;
import guru.qa.rococo.service.impl.UsersDbClient;
import guru.qa.rococo.utils.RandomDataUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RestTest
public class UserdataRestTest {

    @RegisterExtension
    private static final ApiLoginExtension apiLoginExtension = ApiLoginExtension.restApiLoginExtension();

    private final GatewayApiClient gatewayApiClient = new GatewayApiClient();
    private final UsersDbClient usersDbClient = new UsersDbClient();

    @Test
    @User
    @ApiLogin
    void shouldReturnCreateUser(@Token String token, UserJson user) {
        UserJson response = gatewayApiClient.getCurrentUser(token);
        assertAll(
                () -> assertEquals(user.id(), response.id()),
                () -> assertEquals(user.username(), response.username()),
                () -> assertEquals(user.firstname(), response.firstname()),
                () -> assertEquals(user.lastname(), response.lastname()),
                () -> assertEquals(user.avatar(), response.avatar())
        );
    }

    private static Stream<String> incorrectTokens() {
        return Stream.of(
                "invalid-token",
                "",
                null
        );
    }

    @ParameterizedTest
    @MethodSource("incorrectTokens")
    void shouldReturnErrorWhenGetCurrentUserWithIncorrectToken(String token) {
        AssertionError assertionError = assertThrows(AssertionError.class, () -> gatewayApiClient.getCurrentUser(token));
        assertTrue(assertionError.getMessage().contains("Response code: 401"));
    }

    @Test
    @User
    @ApiLogin
    void shouldUpdateUser(@Token String token, UserJson user) {
        UserJson updatedUser = new UserJson(
                user.id(),
                user.username(),
                RandomDataUtils.randomFirstName(),
                RandomDataUtils.randomLastName(),
                user.avatar(),
                user.testData()
        );

        UserJson response = gatewayApiClient.updateUser(token, updatedUser);
        UserJson expectedUser = usersDbClient.getUserById(updatedUser.id());
        assertAll(
                () -> assertEquals(expectedUser.id(), response.id()),
                () -> assertEquals(expectedUser.username(), response.username()),
                () -> assertEquals(expectedUser.firstname(), response.firstname()),
                () -> assertEquals(expectedUser.lastname(), response.lastname()),
                () -> assertEquals(expectedUser.avatar(), response.avatar())
        );
    }

    @ParameterizedTest
    @MethodSource("incorrectTokens")
    @User
    void shouldReturnErrorWhenUpdateUserWithIncorrectToken(String token, UserJson user) {
        UserJson updatedUser = new UserJson(
                user.id(),
                user.username(),
                RandomDataUtils.randomFirstName(),
                RandomDataUtils.randomLastName(),
                user.avatar(),
                user.testData()
        );
        AssertionError assertionError = assertThrows(AssertionError.class, () -> gatewayApiClient.updateUser(token, updatedUser));
        assertTrue(assertionError.getMessage().contains("Response code: 401"));
    }

}
