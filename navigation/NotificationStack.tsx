import React from "react";
import {
  createStackNavigator,
  StackNavigationProp,
} from "@react-navigation/stack";
import { RouteProp } from "@react-navigation/native";
import { primary } from "../constants/Colors";
import { MaterialCommunityIcons } from "@expo/vector-icons";
import { StyleSheet } from "react-native";
import Notifications from "../screens/Notifications";

interface NotificationStackProps {}

export type NotificationParamList = {
  Notifications: undefined;
};

export type NotificationNavProps<T extends keyof NotificationParamList> = {
  navigation: StackNavigationProp<NotificationParamList, T>;
  route: RouteProp<NotificationParamList, T>;
};

const Stack = createStackNavigator<NotificationParamList>();

export const NotificationStack: React.FC<NotificationStackProps> = ({}) => {
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
          title: "Notifications",
          headerStyle: {
            elevation: 0,
            shadowOpacity: 0,
          },
          headerLeft: () => (
            <MaterialCommunityIcons
              style={styles.notificationIcon}
              name="bell-outline"
              size={32}
              color={primary}
            />
          ),
        }}
        name="Notifications"
        component={Notifications}
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
