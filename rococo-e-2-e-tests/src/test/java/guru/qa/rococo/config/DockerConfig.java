package guru.qa.rococo.config;

import javax.annotation.Nonnull;

public enum DockerConfig implements Config {
    INSTANCE;

    @Nonnull
    @Override
    public String frontUrl() {
        return "http://frontend.rococo.dc/";
    }

    @Nonnull
    @Override
    public String authUrl() {
        return "http://auth.rococo.dc:9000/";
    }

    @Nonnull
    @Override
    public String authJdbcUrl() {
        return "jdbc:postgresql://rococo-all-db:5432/rococo-auth";
    }

    @Nonnull
    @Override
    public String gatewayUrl() {
        return "http://gateway.rococo.dc:8080/";
    }

    @Nonnull
    @Override
    public String userdataGrpcAddress() {
        return "userdata.rococo.dc";
    }

    @Nonnull
    @Override
    public String userdataJdbcUrl() {
        return "jdbc:postgresql://rococo-all-db:5432/rococo-userdata";
    }

    @Nonnull
    @Override
    public String artistGrpcAddress() {
        return "artist.rococo.dc";
    }

    @Nonnull
    @Override
    public String artistJdbcUrl() {
        return "jdbc:postgresql://rococo-all-db:5432/rococo-artist";
    }

    @Nonnull
    @Override
    public String museumGrpcAddress() {
        return "museum.rococo.dc";
    }

    @Nonnull
    @Override
    public String museumJdbcUrl() {
        return "jdbc:postgresql://rococo-all-db:5432/rococo-museum";
    }

    @Nonnull
    @Override
    public String paintingGrpcAddress() {
        return "painting.rococo.dc";
    }

    @Nonnull
    @Override
    public String paintingJdbcUrl() {
        return "jdbc:postgresql://rococo-all-db:5432/rococo-painting";
    }

    @Nonnull
    @Override
    public String screenshotBaseDir() {
        return "screenshots/selenoid/";
    }

    @Nonnull
    @Override
    public String allureDockerUrl() {
        final String allureDockerApiFromEnv = System.getenv("ALLURE_DOCKER_API");
        return allureDockerApiFromEnv != null
                ? allureDockerApiFromEnv
                : "http://allure:5050/";
    }
}
