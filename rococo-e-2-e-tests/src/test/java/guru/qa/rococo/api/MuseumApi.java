package guru.qa.rococo.api;

import guru.qa.rococo.model.museum.MuseumJson;
import guru.qa.rococo.model.pageable.RestResponsePage;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.UUID;

public interface MuseumApi {

    @GET("api/museum")
    Call<RestResponsePage<MuseumJson>> getAllMuseum(
            @Query("page") int page,
            @Query("size") int size,
            @Query("title") String title
    );

    @GET("api/museum/{id}")
    Call<MuseumJson> getMuseumById(@Path("id") UUID id);

    @POST("api/museum")
    Call<MuseumJson> createMuseum(
            @Header("Authorization") String bearerToken,
            @Body MuseumJson museumJson
    );

    @PATCH("api/museum")
    Call<MuseumJson> updateMuseum(
            @Header("Authorization") String bearerToken,
            @Body MuseumJson museumJson
    );
}
