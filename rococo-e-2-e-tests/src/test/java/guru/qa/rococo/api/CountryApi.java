package guru.qa.rococo.api;

import guru.qa.rococo.model.museum.CountryJson;
import guru.qa.rococo.model.pageable.RestResponsePage;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface CountryApi {

    @GET("api/country")
    Call<RestResponsePage<CountryJson>> getAllCountry(
            @Header("Authorization") String bearerToken,
            @Query("page") int page,
            @Query("size") int size,
            @Query("title") String title
    );
}
