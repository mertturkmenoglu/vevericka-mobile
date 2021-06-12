import axios, { AxiosInstance } from "axios";
import Notification from "../../models/Notification";

class NotificationService {
  private axiosInstance: AxiosInstance;

  constructor(private readonly authToken: string) {
    this.axiosInstance = axios.create({
      baseURL: "https://vevericka-backend.herokuapp.com/api/v2/notifications",
      headers: {
        authorization: this.authToken,
      },
    });
  }

  async getNotifications(username: string): Promise<Notification[]> {
    const res = await this.axiosInstance.get<Notification[]>(
      `/user/${username}`
    );
    return res.data;
  }

  async deleteNotification(id: string): Promise<{ message: string }> {
    const res = await this.axiosInstance.delete<{ message: string }>(`/${id}`);
    return res.data;
  }
}

export default NotificationService;
