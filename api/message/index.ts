import axios, { AxiosInstance } from "axios";
import Chat from "../../models/Chat";
import Message from "../../models/Message";

class MessageService {
  private axiosInstance: AxiosInstance;

  constructor(private readonly authToken: string) {
    this.axiosInstance = axios.create({
      baseURL: "https://vevericka-backend.herokuapp.com/api/v2/message",
      headers: {
        authorization: this.authToken,
      },
    });
  }

  async getUserChats(username: string): Promise<Chat[]> {
    const res = await this.axiosInstance.get<Chat[]>(
      `/chat/user-chats/${username}`
    );
    return res.data;
  }

  async getChatMessages(chatId: string): Promise<Message[]> {
    const res = await this.axiosInstance.get<Message[]>(
      `/chat/messages/${chatId}`
    );
    return res.data;
  }
}

export default MessageService;
