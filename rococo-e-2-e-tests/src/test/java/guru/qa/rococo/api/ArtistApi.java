package guru.qa.rococo.api;

import guru.qa.rococo.model.artist.ArtistJson;
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

public interface ArtistApi {

    @GET("api/artist")
    Call<RestResponsePage<ArtistJson>> getAllArtist(
            @Query("page") int page,
            @Query("size") int size,
            @Query("name") String name
    );

    @GET("api/artist/{id}")
    Call<ArtistJson> getArtistById(@Path("id") UUID id);

    @POST("api/artist")
    Call<ArtistJson> createArtist(
            @Header("Authorization") String bearerToken,
            @Body ArtistJson artistJson
    );

    @PATCH("api/artist")
    Call<ArtistJson> updateArtist(
            @Header("Authorization") String bearerToken,
            @Body ArtistJson artistJson
    );
}
