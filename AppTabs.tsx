import React from "react";
import { createBottomTabNavigator } from "@react-navigation/bottom-tabs";
import { MaterialCommunityIcons } from "@expo/vector-icons";
import Home from "./screens/Home";
import User from "./screens/User";
import Search from "./screens/Search";
import Notifications from "./screens/Notifications";
import Messages from "./screens/Messages";

interface AppTabsProps {}

export type AppParamList = {
  Home: undefined;
  Search: undefined;
  Notifications: undefined;
  Messages: undefined;
  User: undefined;
};

const Tabs = createBottomTabNavigator<AppParamList>();

const mapTabsToIcons = {
  Home: "home-roof" as const,
  Search: "magnify" as const,
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
      <Tabs.Screen name="Home" component={Home} />
      <Tabs.Screen name="Search" component={Search} />
      <Tabs.Screen name="Notifications" component={Notifications} />
      <Tabs.Screen name="Messages" component={Messages} />
      <Tabs.Screen name="User" component={User} />
    </Tabs.Navigator>
  );
};
