package guru.qa.rococo.api;

import guru.qa.rococo.model.userdata.UserJson;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface UserdataApi {

    @GET("user")
    Call<UserJson> currentUser(@Query("username") String username);

    @POST("user")
    Call<UserJson> updateUser(@Body UserJson user);
}
