package com.jigar.slack.model;

import lombok.Data;

@Data
public class SlackMessage {

    private String webhookUrl;
    private String message;
    private String previousMessageStatus;

}
