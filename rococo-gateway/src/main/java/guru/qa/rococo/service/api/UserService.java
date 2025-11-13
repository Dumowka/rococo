package guru.qa.rococo.service.api;

import guru.qa.rococo.model.UserJson;

public interface UserService {

    UserJson getUser(String username);

    UserJson updateUser(UserJson user);
}
