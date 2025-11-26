package guru.qa.rococo.api;

import guru.qa.rococo.model.userdata.UserJson;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface UserdataApi {

    @GET("api/user")
    Call<UserJson> currentUser(
            @Header("Authorization") String bearerToken
    );

    @PATCH("api/user")
    Call<UserJson> updateUser(
            @Header("Authorization") String bearerToken,
            @Body UserJson user
    );
}
