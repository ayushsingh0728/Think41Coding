package com.example.demo.controller;

import com.example.demo.dto.ChatRequest;
import com.example.demo.entity.Message;
import com.example.demo.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping
    public Message handleChat(@RequestBody ChatRequest request) {
        return chatService.processChat(request.getUserId(), request.getMessage(), request.getConversationId());
    }
}
