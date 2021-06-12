import axios, { AxiosInstance } from "axios";
import Post from "../../models/Post";
import TrendingPerson from "../../models/TrendingPerson";
import Tag from "../../models/Tag";

class ExploreService {
  private axiosInstance: AxiosInstance;

  constructor(private readonly authToken: string) {
    this.axiosInstance = axios.create({
      baseURL: "https://vevericka-backend.herokuapp.com/api/v2/explore",
      headers: {
        authorization: this.authToken,
      },
    });
  }

  async getTags(): Promise<Tag[]> {
    const res = await this.axiosInstance.get<Tag[]>("/tags");
    return res.data;
  }

  async getPostsByTag(tag: string): Promise<Post[]> {
    const res = await this.axiosInstance.get<Post[]>("/tag/" + tag);
    return res.data;
  }

  async getTrendingPeople(): Promise<TrendingPerson[]> {
    const res = await this.axiosInstance.get<TrendingPerson[]>("/people");
    return res.data;
  }

  async getTrendingPosts(): Promise<Post[]> {
    const res = await this.axiosInstance.get<Post[]>("/posts");
    return res.data;
  }
}

export default ExploreService;
