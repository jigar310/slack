package com.jigar.slack.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AuthedUser {

    public String id;
    public String scope;
    @JsonProperty("access_token")
    public String accessToken;
    @JsonProperty("token_type")
    public String tokenType;

}
