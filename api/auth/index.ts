import axios, {AxiosInstance} from 'axios';

type LoginResponse = {
    userId: string;
    username: string;
    token: string;
}

class AuthService {
  static get auth(): AxiosInstance {
    return axios.create({
      baseURL: 'https://vevericka-backend.herokuapp.com/api/v2/auth'
    });
  }

  static async login(email: string, password: string): Promise<LoginResponse> {
    const resp = await AuthService.auth.post('/login', {
      email,
      password,
    });

    const data = resp.data;
    const { userId, username } = data;
    const token = resp.headers['authorization'];

    return {
      userId,
      username,
      token,
    };
  }

  static async register(email: string, username: string, name: string, password: string): Promise<string | null> {
    const resp = await AuthService.auth.post('/register', {
      email, username, name, password,
    });

    const data = resp.data;
    if (data['id']) {
      return data['id'];
    }

    return null;
  }

  static async sendPasswordResetEmail(email: string): Promise<boolean> {
    await AuthService.auth.post('/send-password-reset-email', {
      email,
    });

    return true;
  }

  static async resetPassword(email: string, code: string, newPassword: string): Promise<boolean> {
    await AuthService.auth.post('/reset-password', {
      email,
      code,
      password: newPassword,
    });

    return true;
  }
}

export default AuthService;