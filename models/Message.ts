interface Message {
  sender: string;
  content: string;
  chat: string;
  readBy: string[];
  createdAt: string;
  updatedAt: string;
}

export default Message;
