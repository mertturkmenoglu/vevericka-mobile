import axios, { AxiosInstance } from "axios";
import Post from "../../models/Post";

type PostDto = {
  createdBy: string;
  content: string;
};

class PostService {
  private axiosInstance: AxiosInstance;

  constructor(private readonly authToken: string) {
    this.axiosInstance = axios.create({
      baseURL: "https://vevericka-backend.herokuapp.com/api/v2/post",
      headers: {
        authorization: this.authToken,
      },
    });
  }

  async getUserPosts(username: string): Promise<Post[]> {
    const res = await this.axiosInstance.get<Post[]>("/user/" + username);
    return res.data;
  }

  async getPostById(id: string): Promise<Post> {
    const res = await this.axiosInstance.get<Post>("/" + id);
    return res.data;
  }

  async getFeedByUsername(username: string): Promise<Post[]> {
    const res = await this.axiosInstance.get<Post[]>("/feed/" + username);
    return res.data;
  }

  async createPost(post: PostDto): Promise<Post> {
    const res = await this.axiosInstance.post<Post>("/", post);
    return res.data;
  }

  async deletePost(postId: string): Promise<boolean> {
    await this.axiosInstance.delete("/" + postId);
    return true;
  }
}

export default PostService;
