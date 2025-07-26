import React, { useEffect, useState, useContext } from 'react';
import axios from 'axios';
import { ChatContext } from '../context/ChatContext';

const Sidebar = () => {
  const [conversations, setConversations] = useState([]);
  const { setMessages } = useContext(ChatContext);

  useEffect(() => {
    axios.get('/api/conversations').then(res => setConversations(res.data));
  }, []);

  const loadConversation = async (id) => {
    const res = await axios.get(`/api/conversations/${id}/messages`);
    setMessages(res.data);
  };

  return (
    <div className="sidebar">
      <h3>Past Chats</h3>
      <ul>
        {conversations.map(conv => (
          <li key={conv.id} onClick={() => loadConversation(conv.id)}>
            Conversation #{conv.id}
          </li>
        ))}
      </ul>
    </div>
  );
};

export default Sidebar;
