import React, { createContext, useState } from 'react';
import axios from 'axios';

export const ChatContext = createContext();

axios.defaults.baseURL = 'http://localhost:8080';

export const ChatProvider = ({ children }) => {
  const [messages, setMessages] = useState([]);
  const [loading, setLoading] = useState(false);

  const sendMessage = async (userInput) => {
    const userMessage = { sender: 'USER', content: userInput };
    setMessages((prev) => [...prev, userMessage]);
    setLoading(true);

    try {
      const response = await axios.post('/api/chat', { message: userInput });
      const botMessage = { sender: 'AI', content: response.data.reply };
      setMessages((prev) => [...prev, botMessage]);
    } catch (error) {
      const errorMessage = { sender: 'AI', content: 'Something went wrong.' };
      setMessages((prev) => [...prev, errorMessage]);
    } finally {
      setLoading(false);
    }
  };

  return (
    <ChatContext.Provider value={{ messages, sendMessage, loading }}>
      {children}
    </ChatContext.Provider>
  );
};
