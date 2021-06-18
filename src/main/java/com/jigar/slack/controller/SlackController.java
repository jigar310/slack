package com.jigar.slack.controller;

import com.jigar.slack.model.SlackMessage;
import com.jigar.slack.service.SlackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.util.HashMap;
import java.util.Map;

@Controller
public class SlackController {

    @Value("${spring.application.name}")
    String appName;

    @Autowired
    SlackService slackService;

    @GetMapping("/")
    public String homePage(Model model) {
        SlackMessage slackMessage = new SlackMessage();
        model.addAttribute("slackMessage", slackMessage);
        return "home";
    }

    @PostMapping("/send")
    public String sendSlackMessage(@ModelAttribute("slackMessage") SlackMessage slackMessage) {
        System.out.println(slackMessage.getMessage());
        System.out.println(slackMessage.getWebhookUrl());
        if(slackService.sendMessage(slackMessage.getWebhookUrl(),slackMessage.getMessage())){
            slackMessage.setPreviousMessageStatus("Message Sent Successfully!");
        }else{
            slackMessage.setPreviousMessageStatus("Error Sending Message!");
        }
        return "message";
    }

    @GetMapping("/slack/oauth/callback")
    public ModelAndView slackOAuthCallBack(@RequestParam String code, @RequestParam String state) {
        String webhookUrl = slackService.postForSlackToken(code);
        System.out.println("Webhook URL: "+ webhookUrl);
        Map<String,Object> model = new HashMap<String, Object>();
        SlackMessage slackMessage = new SlackMessage();
        slackMessage.setWebhookUrl(webhookUrl);
        model.put("slackMessage", slackMessage);
        return new ModelAndView("message", model);
    }
}
