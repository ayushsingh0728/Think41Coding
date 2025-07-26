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

    @Autowired
    private GroqLLMService groqLLMService;  // ✅ Inject LLM service

    public Message processChat(Long userId, String userMessage, Long conversationId) {
        User user = userRepository.findById(userId).orElseThrow();
        ConversationSession session;

        if (conversationId != null) {
            session = sessionRepository.findById(conversationId).orElseThrow();
        } else {
            session = new ConversationSession();
            session.setUser(user);
            session.setCreatedAt(LocalDateTime.now());  // ✅ Use LocalDateTime
            session = sessionRepository.save(session);
        }

        // Save user's message
        Message userMsg = new Message();
        userMsg.setSession(session);
        userMsg.setSender("user");
        userMsg.setContent(userMessage);
        userMsg.setTimestamp(LocalDateTime.now());
        messageRepository.save(userMsg);

        // 🔗 Get response from Groq LLM
        String aiReply = groqLLMService.getLLMResponse(userMessage);

        // Save AI response
        Message aiMsg = new Message();
        aiMsg.setSession(session);
        aiMsg.setSender("ai");
        aiMsg.setContent(aiReply);
        aiMsg.setTimestamp(LocalDateTime.now());
        return messageRepository.save(aiMsg);
    }
}
