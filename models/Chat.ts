import Message from "./Message";

export type ChatUser = {
  username: string;
  name: string;
  image: string;
  _id: string;
};

interface Chat {
  _id: string;
  users: ChatUser[];
  chatName: string;
  lastMessage: Message | null;
  chatImage: string;
  isGroupChat: boolean;
  createdAt: string;
  updatedAt: string;
}

export default Chat;
