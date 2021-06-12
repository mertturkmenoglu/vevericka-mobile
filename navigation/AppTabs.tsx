import React from "react";
import { createBottomTabNavigator } from "@react-navigation/bottom-tabs";
import { MaterialCommunityIcons } from "@expo/vector-icons";
import User from "../screens/User";
import Messages from "../screens/Messages";
import { HomeStack } from "./HomeStack";
import { NotificationStack } from "./NotificationStack";
import { ExploreStack } from "./ExploreStack";

interface AppTabsProps {}

export type AppParamList = {
  Home: undefined;
  Explore: undefined;
  Notifications: undefined;
  Messages: undefined;
  User: undefined;
};

const Tabs = createBottomTabNavigator<AppParamList>();

const mapTabsToIcons = {
  Home: "home-roof" as const,
  Explore: "pound" as const,
  Notifications: "bell-outline" as const,
  Messages: "email-outline" as const,
  User: "account-circle-outline" as const,
};

export const AppTabs: React.FC<AppTabsProps> = ({}) => {
  return (
    <Tabs.Navigator
      screenOptions={({ route }) => ({
        tabBarIcon: ({ focused, color, size }) => {
          const name = mapTabsToIcons[route.name];
          return (
            <MaterialCommunityIcons name={name} size={size} color={color} />
          );
        },
      })}
      tabBarOptions={{
        activeTintColor: "#ff5722",
        inactiveTintColor: "gray",
        showLabel: false,
      }}
    >
      <Tabs.Screen name="Home" component={HomeStack} />
      <Tabs.Screen name="Explore" component={ExploreStack} />
      <Tabs.Screen name="Notifications" component={NotificationStack} />
      <Tabs.Screen name="Messages" component={Messages} />
      <Tabs.Screen name="User" component={User} />
    </Tabs.Navigator>
  );
};
