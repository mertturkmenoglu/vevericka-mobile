import UserPopulated from "./UserPopulated";
import Language from "./Language";

interface User {
  _id: string;
  username: string;
  name: string;
  email: string;
  image: string;
  hobbies: string[];
  features: string[];
  bdate?: Date;
  followers: UserPopulated[];
  following: UserPopulated[];
  location: {
    city?: string;
    country?: string;
  };
  job: string;
  school: string;
  website: string;
  twitter: string;
  bio: string;
  gender: string;
  languages: Language[];
  wishToSpeak: string[];
}

export default User;
