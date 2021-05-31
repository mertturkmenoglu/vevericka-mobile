import React from "react";
import { createBottomTabNavigator } from "@react-navigation/bottom-tabs";
import { AntDesign, EvilIcons } from "@expo/vector-icons";
import Home from "./screens/Home";
interface AppTabsProps {}

export type AppParamList = {
  Home: undefined;
};

const Tabs = createBottomTabNavigator<AppParamList>();

export const AppTabs: React.FC<AppTabsProps> = ({}) => {
  return (
    <Tabs.Navigator
      screenOptions={({ route }) => ({
        tabBarIcon: ({ focused, color, size }) => {
          if (route.name === "Home") {
            return <AntDesign name={"home"} size={size} color={color} />;
          } else {
            return <EvilIcons name={"search"} size={size} color={color} />;
          }
        },
      })}
      tabBarOptions={{
        activeTintColor: "#ff5722",
        inactiveTintColor: "gray",
      }}
    >
      <Tabs.Screen name="Home" component={Home} />
    </Tabs.Navigator>
  );
};
