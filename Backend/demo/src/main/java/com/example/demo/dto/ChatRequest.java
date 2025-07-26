package com.example.demo.dto;

import lombok.Data;

@Data
public class ChatRequest {
    private Long userId;
    private String message;
    private Long conversationId; // optional
}