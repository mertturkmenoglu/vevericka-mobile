import axios, { AxiosInstance } from "axios";
import User from "../../models/User";

class UserService {
  private axiosInstance: AxiosInstance;

  constructor(private readonly authToken: string) {
    this.axiosInstance = axios.create({
      baseURL: "https://vevericka-backend.herokuapp.com/api/v2/user",
      headers: {
        authorization: this.authToken,
      },
    });
  }

  async getUserByUsername(username: string): Promise<User> {
    const res = await this.axiosInstance.get(`/username/${username}`);
    return res.data;
  }

  async followUser(
    thisUsername: string,
    otherUsername: string
  ): Promise<boolean> {
    const data = {
      thisUsername,
      otherUsername,
    };

    await this.axiosInstance.post("/follow", data);
    return true;
  }

  async unfollowUser(
    thisUsername: string,
    otherUsername: string
  ): Promise<boolean> {
    const data = {
      thisUsername,
      otherUsername,
    };

    await this.axiosInstance.post("/unfollow", data);
    return true;
  }
}

export default UserService;
