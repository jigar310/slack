package com.jigar.slack.service;

import com.jigar.slack.model.DownstreamMessage;
import com.jigar.slack.model.SlackTokenResponse;
import com.slack.api.Slack;
import com.slack.api.webhook.Payload;
import com.slack.api.webhook.WebhookResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Service
public class SlackService {

    @Value("${slack.oauth.v2.url}")
    String slackURL;
    @Value("${slack.client.id}")
    String clientID;
    @Value("${slack.client.secret}")
    String clientSecret;

    private final WebClient webClient;

    public SlackService(){

        webClient =  WebClient.builder().build();
    }

    public String postForSlackToken(String code){

        SlackTokenResponse tokenResponse = webClient.post()
                .uri(slackURL)
                        .body(BodyInserters.fromFormData("code", code))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .header("Authorization","Basic "+ Base64Utils.encodeToString((clientID+":"+clientSecret).getBytes()))
                        .retrieve()
                        .bodyToMono(SlackTokenResponse.class)
                        .onErrorMap(e -> new Exception("Error connecting Slack oAuth API", e))
                        .block();
        if (tokenResponse != null && tokenResponse.getIncomingWebhook() != null){
            return tokenResponse.getIncomingWebhook().getUrl();
        }
        return "";
    }

    public Boolean sendMessage(String webhookUrl, String message){
        DownstreamMessage dsMessage = new DownstreamMessage();
        dsMessage.setText(message);
        Boolean webhookResponse = false;
        webhookResponse = webClient.post()
                .uri(webhookUrl)
                .body(Mono.just(dsMessage), DownstreamMessage.class)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchangeToMono(response -> {
                    if (response.statusCode()
                            .equals(HttpStatus.OK)) {
                        return Mono.just(true);
                    } else {
                        return Mono.just(false);
                    }
                })
                .onErrorMap(e -> new Exception("Error connecting Slack oAuth API", e))
                .block();
        return webhookResponse;
    }
}
