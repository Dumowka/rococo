package guru.qa.rococo.api;

import guru.qa.rococo.service.RestClient;

public class UserdataApiClient extends RestClient {

    private final UserdataApi userdataApi;

    public UserdataApiClient() {
        super(CFG.gatewayUrl());
        userdataApi = create(UserdataApi.class);
    }
}
