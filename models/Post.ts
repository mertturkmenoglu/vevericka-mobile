interface Post {
  _id: string;
  createdBy: {
    userId: string;
    username: string;
    name: string;
    image: string;
  };
  content: string;
  comments: string[];
  hashtags: string[];
  mentions: string[];
  createdAt: string;
  updatedAt: string;
}

export default Post;
