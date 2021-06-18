package com.jigar.slack.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class IncomingWebhook {
    public String channel;
    @JsonProperty("channel_id")
    public String channelId;
    @JsonProperty("configuration_url")
    public String configurationUrl;
    public String url;
}
