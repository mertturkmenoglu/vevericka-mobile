import AsyncStorage from "@react-native-async-storage/async-storage";
import AuthUser from "../models/AuthUser";

export const getUserFromStorage = async (): Promise<AuthUser | null> => {
  try {
    const value = await AsyncStorage.getItem("vev-user");
    if (value === null) {
      return null;
    }
    return JSON.parse(value) as AuthUser;
  } catch (e) {
    return null;
  }
};

export const writeUserToStorage = async (user: AuthUser): Promise<boolean> => {
  try {
    await AsyncStorage.setItem("vev-user", JSON.stringify(user));
    return true;
  } catch (e) {
    return false;
  }
};

export const deleteUserFromStorage = async (): Promise<boolean> => {
  try {
    await AsyncStorage.removeItem("vev-user");
    return true;
  } catch (e) {
    return false;
  }
};
