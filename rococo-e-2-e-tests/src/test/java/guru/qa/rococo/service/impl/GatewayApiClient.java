package guru.qa.rococo.service.impl;

import guru.qa.rococo.api.UserdataApi;
import guru.qa.rococo.service.RestClient;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class GatewayApiClient extends RestClient {

    private final UserdataApi userdataApi;

    public GatewayApiClient() {
        super(CFG.gatewayUrl());
        this.userdataApi = create(UserdataApi.class);
    }
}
