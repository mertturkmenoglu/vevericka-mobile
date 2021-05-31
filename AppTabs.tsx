import React from "react";
import { createBottomTabNavigator } from "@react-navigation/bottom-tabs";
import { MaterialCommunityIcons } from "@expo/vector-icons";
import Home from "./screens/Home";

interface AppTabsProps {}

export type AppParamList = {
  Home: undefined;
  Search: undefined;
  Notifications: undefined;
  Messages: undefined;
  Profile: undefined;
};

const Tabs = createBottomTabNavigator<AppParamList>();

const mapTabsToIcons = {
  Home: "home-roof" as const,
  Search: "magnify" as const,
  Notifications: "bell-outline" as const,
  Messages: "email-outline" as const,
  Profile: "account-circle-outline" as const,
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
      <Tabs.Screen name="Search" component={Home} />
      <Tabs.Screen name="Notifications" component={Home} />
      <Tabs.Screen name="Messages" component={Home} />
      <Tabs.Screen name="Profile" component={Home} />
    </Tabs.Navigator>
  );
};
