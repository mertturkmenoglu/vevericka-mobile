import React from "react";
import {
  createStackNavigator,
  StackNavigationProp,
} from "@react-navigation/stack";
import { RouteProp } from "@react-navigation/native";
import { primary } from "../constants/Colors";
import { MaterialCommunityIcons } from "@expo/vector-icons";
import { StyleSheet } from "react-native";
import Messages from "../screens/Messages";

interface MessageStackProps {}

export type MessageParamList = {
  Messages: undefined;
};

export type MessageNavProps<T extends keyof MessageParamList> = {
  navigation: StackNavigationProp<MessageParamList, T>;
  route: RouteProp<MessageParamList, T>;
};

const Stack = createStackNavigator<MessageParamList>();

export const MessageStack: React.FC<MessageStackProps> = ({}) => {
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
        options={{
          title: "Messages",
          headerStyle: {
            elevation: 0,
            shadowOpacity: 0,
          },
          headerLeft: () => (
            <MaterialCommunityIcons
              style={styles.notificationIcon}
              name="email-outline"
              size={32}
              color={primary}
            />
          ),
        }}
        name="Messages"
        component={Messages}
      />
    </Stack.Navigator>
  );
};

const styles = StyleSheet.create({
  notificationIcon: {
    width: 32,
    height: 32,
    marginLeft: 10,
  },
});
