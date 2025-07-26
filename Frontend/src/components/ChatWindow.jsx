import React, { useContext } from 'react';
import { ChatContext } from '../context/ChatContext';
import MessageList from './MessageList';
import UserInput from './UserInput';

const ChatWindow = () => {
  const { messages, sendMessage, loading } = useContext(ChatContext);

  return (
    <div className="chat-window">
      <MessageList messages={messages} />
      {loading && <div className="typing-indicator">SwiftBot is typing...</div>}
      <UserInput onSend={sendMessage} />
    </div>
  );
};

export default ChatWindow;
