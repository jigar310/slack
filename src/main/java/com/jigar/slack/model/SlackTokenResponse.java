package com.jigar.slack.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SlackTokenResponse {
    public boolean ok;
    @JsonProperty("access_token")
    public String accessToken;
    @JsonProperty("token_type")
    public String tokenType;
    public String scope;
    @JsonProperty("bot_user_id")
    public String botUserId;
    @JsonProperty("app_id")
    public String appId;
    public Team team;
    public Enterprise enterprise;
    @JsonProperty("authed_user")
    public AuthedUser authedUser;
    @JsonProperty("is_enterprise_install")
    public boolean isEnterpriseInstall;
    @JsonProperty("incoming_webhook")
    public IncomingWebhook incomingWebhook;


}
