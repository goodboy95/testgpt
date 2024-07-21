package com.seekerhut;

import java.util.List;
import javax.validation.constraints.NotBlank;

public class ChatRequest {

    @NotBlank
    private String url;
    @NotBlank
    private String apiKey;
    @NotBlank
    private String model;
    private List<Message> messages;

    // Getters and Setters


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public static class Message {
        private String role;
        private String content;

        // Getters and Setters

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}


