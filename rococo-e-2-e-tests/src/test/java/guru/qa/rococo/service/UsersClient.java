package guru.qa.rococo.service;

import guru.qa.rococo.model.userdata.UserJson;
import guru.qa.rococo.service.impl.UsersDbClient;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface UsersClient {

    @Nonnull
    static UsersClient getInstance() {
        return new UsersDbClient();
    }

    @Nonnull
    UserJson createUser(String username, String password);
}
