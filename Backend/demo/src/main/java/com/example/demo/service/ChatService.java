package com.example.demo.service;

import com.example.demo.Repository.ConversationSessionRepository;
import com.example.demo.Repository.MessageRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.entity.ConversationSession;
import com.example.demo.entity.Message;
import com.example.demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ChatService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConversationSessionRepository sessionRepository;

    @Autowired
    private MessageRepository messageRepository;

    public Message processChat(Long userId, String userMessage, Long conversationId) {
        User user = userRepository.findById(userId).orElseThrow();
        ConversationSession session;

        if (conversationId != null) {
            session = sessionRepository.findById(conversationId).orElseThrow();
        } else {
            session = new ConversationSession();
            session.setUser(user);
            session.setCreatedAt(LocalDateTime.now());  // ✅ FIXED: changed to LocalDateTime
            session = sessionRepository.save(session);
        }

        // Save user message
        Message userMsg = new Message();
        userMsg.setSession(session);
        userMsg.setSender("user");
        userMsg.setContent(userMessage);
        userMsg.setTimestamp(LocalDateTime.now());  // ✅ LocalDateTime
        messageRepository.save(userMsg);

        // Simulated AI response
        Message aiMsg = new Message();
        aiMsg.setSession(session);
        aiMsg.setSender("ai");
        aiMsg.setContent("This is a sample AI response.");
        aiMsg.setTimestamp(LocalDateTime.now());  // ✅ LocalDateTime
        return messageRepository.save(aiMsg);
    }
}
