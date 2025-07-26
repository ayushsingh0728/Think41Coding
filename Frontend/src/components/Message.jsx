import React from 'react';

const Message = ({ sender, content }) => {
  return (
    <div className={`message ${sender === 'USER' ? 'user' : 'ai'}`}>
      <div className="message-content">
        <strong>{sender === 'USER' ? 'You' : 'SwiftBot'}:</strong> {content}
      </div>
    </div>
  );
};

export default Message;
