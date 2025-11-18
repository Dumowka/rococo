package guru.qa.rococo.config;

import javax.annotation.Nonnull;

public enum DockerConfig implements Config {
    INSTANCE;

    @Nonnull
    @Override
    public String frontUrl() {
        return "";
    }

    @Nonnull
    @Override
    public String authUrl() {
        return "";
    }

    @Nonnull
    @Override
    public String authJdbcUrl() {
        return "";
    }

    @Nonnull
    @Override
    public String gatewayUrl() {
        return "";
    }

    @Nonnull
    @Override
    public String userdataGrpcAddress() {
        return "";
    }

    @Nonnull
    @Override
    public String userdataJdbcUrl() {
        return "";
    }

    @Nonnull
    @Override
    public String artistGrpcAddress() {
        return "";
    }

    @Nonnull
    @Override
    public String artistJdbcUrl() {
        return "";
    }

    @Nonnull
    @Override
    public String museumGrpcAddress() {
        return "";
    }

    @Nonnull
    @Override
    public String museumJdbcUrl() {
        return "";
    }

    @Nonnull
    @Override
    public String paintingGrpcAddress() {
        return "";
    }

    @Nonnull
    @Override
    public String paintingJdbcUrl() {
        return "";
    }
}
