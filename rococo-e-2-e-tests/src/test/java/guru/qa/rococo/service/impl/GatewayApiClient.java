package guru.qa.rococo.service.impl;

import guru.qa.rococo.api.ArtistApi;
import guru.qa.rococo.api.CountryApi;
import guru.qa.rococo.api.MuseumApi;
import guru.qa.rococo.api.PaintingApi;
import guru.qa.rococo.api.UserdataApi;
import guru.qa.rococo.model.artist.ArtistJson;
import guru.qa.rococo.model.museum.CountryJson;
import guru.qa.rococo.model.museum.MuseumJson;
import guru.qa.rococo.model.pageable.RestResponsePage;
import guru.qa.rococo.model.painting.PaintingJson;
import guru.qa.rococo.model.userdata.UserJson;
import guru.qa.rococo.service.RestClient;
import io.qameta.allure.Step;
import retrofit2.Response;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.UUID;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ParametersAreNonnullByDefault
public class GatewayApiClient extends RestClient {

    private final ArtistApi artistApi;
    private final MuseumApi museumApi;
    private final CountryApi countryApi;
    private final PaintingApi paintingApi;
    private final UserdataApi userdataApi;

    public GatewayApiClient() {
        super(CFG.gatewayUrl());
        this.artistApi = create(ArtistApi.class);
        this.museumApi = create(MuseumApi.class);
        this.countryApi = create(CountryApi.class);
        this.paintingApi = create(PaintingApi.class);
        this.userdataApi = create(UserdataApi.class);
    }

    @Step("Получение пользователя '{0}' через REST API")
    public UserJson getCurrentUser(String bearerToken) {
        try {
            Response<UserJson> response = userdataApi.currentUser(bearerToken).execute();
            assertEquals(
                    200,
                    response.code(),
                    "Response code: " + response.code() + ", response message: " + response.message()
            );
            return requireNonNull(response.body());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Step("Обновление пользователя через REST API")
    public UserJson updateUser(String bearerToken, UserJson user) {
        try {
            Response<UserJson> response = userdataApi.updateUser(bearerToken, user).execute();
            assertEquals(
                    200,
                    response.code(),
                    "Response code: " + response.code() + ", response message: " + response.message()
            );
            return requireNonNull(response.body());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Step("Получение всех художников со страницы '{0}' в размере '{1}' и заголовком '{2}' через REST API")
    public RestResponsePage<ArtistJson> getAllArtist(int page, int size, String name) {
        try {
            Response<RestResponsePage<ArtistJson>> response = artistApi.getAllArtist(page, size, name).execute();
            assertEquals(
                    200,
                    response.code(),
                    "Response code: " + response.code() + ", response message: " + response.message()
            );
            return requireNonNull(response.body());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Step("Получение художника с id = '{0}' через REST API")
    public ArtistJson getArtistById(UUID id) {
        try {
            Response<ArtistJson> response = artistApi.getArtistById(id).execute();
            assertEquals(
                    200,
                    response.code(),
                    "Response code: " + response.code() + ", response message: " + response.message()
            );
            return requireNonNull(response.body());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Step("Создание художника через REST API")
    public ArtistJson createArtist(String bearerToken, ArtistJson artist) {
        try {
            Response<ArtistJson> response = artistApi.createArtist(bearerToken, artist).execute();
            assertEquals(
                    200,
                    response.code(),
                    "Response code: " + response.code() + ", response message: " + response.message()
            );
            return requireNonNull(response.body());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Step("Обновление художника через REST API")
    public ArtistJson updateArtist(String bearerToken, ArtistJson artist) {
        try {
            Response<ArtistJson> response = artistApi.updateArtist(bearerToken, artist).execute();
            assertEquals(
                    200,
                    response.code(),
                    "Response code: " + response.code() + ", response message: " + response.message()
            );
            return requireNonNull(response.body());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Step("Получение всех музеев со страницы '{0}' в размере '{1}' и заголовком '{2}' через REST API")
    public RestResponsePage<MuseumJson> getAllMuseum(int page, int size, String title) {
        try {
            Response<RestResponsePage<MuseumJson>> response = museumApi.getAllMuseum(page, size, title).execute();
            assertEquals(
                    200,
                    response.code(),
                    "Response code: " + response.code() + ", response message: " + response.message()
            );
            return requireNonNull(response.body());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Step("Получение музея с id = '{0}' через REST API")
    public MuseumJson getMuseumById(UUID id) {
        try {
            Response<MuseumJson> response = museumApi.getMuseumById(id).execute();
            assertEquals(
                    200,
                    response.code(),
                    "Response code: " + response.code() + ", response message: " + response.message()
            );
            return requireNonNull(response.body());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Step("Создание музея через REST API")
    public MuseumJson createMuseum(String bearerToken, MuseumJson museum) {
        try {
            Response<MuseumJson> response = museumApi.createMuseum(bearerToken, museum).execute();
            assertEquals(
                    200,
                    response.code(),
                    "Response code: " + response.code() + ", response message: " + response.message()
            );
            return requireNonNull(response.body());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Step("Обновление музея через REST API")
    public MuseumJson updateMuseum(String bearerToken, MuseumJson museum) {
        try {
            Response<MuseumJson> response = museumApi.updateMuseum(bearerToken, museum).execute();
            assertEquals(
                    200,
                    response.code(),
                    "Response code: " + response.code() + ", response message: " + response.message()
            );
            return requireNonNull(response.body());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Step("Получение всех стран со страницы '{0}' в размере '{1}' и заголовком '{2}' через REST API")
    public RestResponsePage<CountryJson> getAllCountry(String bearerToken, int page, int size, String title) {
        try {
            Response<RestResponsePage<CountryJson>> response = countryApi.getAllCountry(bearerToken, page, size, title).execute();
            assertEquals(
                    200,
                    response.code(),
                    "Response code: " + response.code() + ", response message: " + response.message()
            );
            return requireNonNull(response.body());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Step("Получение всех картин со страницы '{0}' в размере '{1}' и заголовком '{2}' через REST API")
    public RestResponsePage<PaintingJson> getAllPainting(int page, int size, String title) {
        try {
            Response<RestResponsePage<PaintingJson>> response = paintingApi.getAllPainting(page, size, title).execute();
            assertEquals(
                    200,
                    response.code(),
                    "Response code: " + response.code() + ", response message: " + response.message()
            );
            return requireNonNull(response.body());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Step("Получение картины с id = '{0}' через REST API")
    public PaintingJson getPaintingById(UUID id) {
        try {
            Response<PaintingJson> response = paintingApi.getPaintingById(id).execute();
            assertEquals(
                    200,
                    response.code(),
                    "Response code: " + response.code() + ", response message: " + response.message()
            );
            return requireNonNull(response.body());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Step("Получение всех картин со страницы '{0}' в размере '{1}' для художника с идентификатором '{2}' через REST API")
    public RestResponsePage<PaintingJson> getPaintingsByArtistId(int page, int size, UUID id) {
        try {
            Response<RestResponsePage<PaintingJson>> response = paintingApi.getPaintingsByArtistId(id, page, size).execute();
            assertEquals(
                    200,
                    response.code(),
                    "Response code: " + response.code() + ", response message: " + response.message()
            );
            return requireNonNull(response.body());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Step("Создание картины через REST API")
    public PaintingJson createPainting(String bearerToken, PaintingJson painting) {
        try {
            Response<PaintingJson> response = paintingApi.createPainting(bearerToken, painting).execute();
            assertEquals(
                    200,
                    response.code(),
                    "Response code: " + response.code() + ", response message: " + response.message()
            );
            return requireNonNull(response.body());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Step("Обновление картины через REST API")
    public PaintingJson updatePainting(String bearerToken, PaintingJson painting) {
        try {
            Response<PaintingJson> response = paintingApi.updatePainting(bearerToken, painting).execute();
            assertEquals(
                    200,
                    response.code(),
                    "Response code: " + response.code() + ", response message: " + response.message()
            );
            return requireNonNull(response.body());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
