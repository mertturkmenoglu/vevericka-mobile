import React, { useContext } from "react";
import {
  createStackNavigator,
  StackNavigationProp,
} from "@react-navigation/stack";
import { RouteProp } from "@react-navigation/native";
import { primary } from "../constants/Colors";
import { StyleSheet } from "react-native";
import User from "../screens/User";
import { AuthContext } from "../context/AuthContext";
import { MaterialCommunityIcons } from "@expo/vector-icons";

interface UserStackProps {
  username: string;
}

export type UserParamList = {
  User: {
    username: string;
  };
};

export type UserNavProps<T extends keyof UserParamList> = {
  navigation: StackNavigationProp<UserParamList, T>;
  route: RouteProp<UserParamList, T>;
};

const Stack = createStackNavigator<UserParamList>();

export const UserStack: React.FC<UserStackProps> = ({}) => {
  const authContext = useContext(AuthContext);

  return (
    <Stack.Navigator
      screenOptions={{
        headerTintColor: primary,
        headerTitleStyle: {
          fontWeight: "normal",
        },
        cardShadowEnabled: false,
        cardStyle: {
          backgroundColor: "#fff",
        },
      }}
    >
      <Stack.Screen
        name="User"
        component={User}
        initialParams={{ username: authContext.user?.username }}
        options={{
          title: authContext.user?.username,
          headerStyle: {
            elevation: 0,
            shadowOpacity: 0,
          },
          headerLeft: () => (
            <MaterialCommunityIcons
              style={styles.accountIcon}
              name="account-circle-outline"
              size={32}
              color={primary}
            />
          ),
        }}
      />
    </Stack.Navigator>
  );
};

const styles = StyleSheet.create({
  accountIcon: {
    marginStart: 10,
    width: 32,
    height: 32,
  },
});
