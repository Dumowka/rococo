package guru.qa.rococo.config;

import guru.qa.rococo.grpc.RococoArtistServiceGrpc;
import guru.qa.rococo.grpc.RococoCountryServiceGrpc;
import guru.qa.rococo.grpc.RococoMuseumServiceGrpc;
import guru.qa.rococo.grpc.RococoPaintingServiceGrpc;
import guru.qa.rococo.grpc.RococoUserServiceGrpc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelFactory;

@Configuration
public class GrpcConfig {

    private static final String USERDATA_CHANNEL_NAME = "rococo-userdata";
    private static final String ARTIST_CHANNEL_NAME = "rococo-artist";
    private static final String MUSEUM_CHANNEL_NAME = "rococo-museum";
    private static final String PAINTING_CHANNEL_NAME = "rococo-painting";

    @Bean
    public RococoUserServiceGrpc.RococoUserServiceBlockingStub userServiceStub(GrpcChannelFactory channels) {
        return RococoUserServiceGrpc.newBlockingStub(channels.createChannel(USERDATA_CHANNEL_NAME));
    }

    @Bean
    public RococoArtistServiceGrpc.RococoArtistServiceBlockingStub artistServiceStub(GrpcChannelFactory channels) {
        return RococoArtistServiceGrpc.newBlockingStub(channels.createChannel(ARTIST_CHANNEL_NAME));
    }

    @Bean
    public RococoCountryServiceGrpc.RococoCountryServiceBlockingStub countryServiceStub(GrpcChannelFactory channels) {
        return RococoCountryServiceGrpc.newBlockingStub(channels.createChannel(MUSEUM_CHANNEL_NAME));
    }

    @Bean
    public RococoMuseumServiceGrpc.RococoMuseumServiceBlockingStub museumServiceStub(GrpcChannelFactory channels) {
        return RococoMuseumServiceGrpc.newBlockingStub(channels.createChannel(MUSEUM_CHANNEL_NAME));
    }

    @Bean
    public RococoPaintingServiceGrpc.RococoPaintingServiceBlockingStub paintingServiceStub(GrpcChannelFactory channels) {
        return RococoPaintingServiceGrpc.newBlockingStub(channels.createChannel(PAINTING_CHANNEL_NAME));
    }
}
