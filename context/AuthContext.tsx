import React, { useState } from "react";
import AuthUser from "../models/AuthUser";
import AuthService from "../api/auth";
import {
  deleteUserFromStorage,
  writeUserToStorage,
} from "../utils/UserStorageUtils";

export type AuthContextState = {
  user: AuthUser | null;
  login: (email: string, password: string) => Promise<AuthUser | null>;
  logout: () => Promise<void>;
};

export const AuthContext = React.createContext<AuthContextState>({
  user: null,
  login: async (email: string, password: string) => null,
  logout: async () => {},
});

interface AuthProviderProps {}

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [user, setUser] = useState<AuthUser | null>(null);

  return (
    <AuthContext.Provider
      value={{
        user,
        login: async (email, password) => {
          try {
            const result = await AuthService.login(email, password);
            await writeUserToStorage(result);
            setUser(result);
            return result;
          } catch (e) {
            return null;
          }
        },
        logout: async () => {
          await deleteUserFromStorage();
          setUser(null);
        },
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};
