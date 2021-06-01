import React from "react";
import {
  createStackNavigator,
  StackNavigationProp,
} from "@react-navigation/stack";
import Home from "./screens/Home";
import { RouteProp } from "@react-navigation/native";
import { primary } from "./constants/Colors";
import { MaterialCommunityIcons } from "@expo/vector-icons";
import { Alert, Image, StyleSheet } from "react-native";

interface HomeStackProps {}

export type HomeParamList = {
  Home: undefined;
};

export type HomeNavProps<T extends keyof HomeParamList> = {
  navigation: StackNavigationProp<HomeParamList, T>;
  route: RouteProp<HomeParamList, T>;
};

const Stack = createStackNavigator<HomeParamList>();

export const HomeStack: React.FC<HomeStackProps> = ({}) => {
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
          title: "Vevericka",
          headerLeft: () => (
            <Image
              style={styles.appLogo}
              source={require("./assets/icon-primary.png")}
              width={32}
              height={32}
            />
          ),
          headerRight: () => (
            <MaterialCommunityIcons
              style={styles.searchIcon}
              name="magnify"
              onPress={() =>
                Alert.alert("Search", "Search Icon pressed", [{ text: "Ok" }])
              }
              size={32}
              color={primary}
            />
          ),
        }}
        name="Home"
        component={Home}
      />
    </Stack.Navigator>
  );
};

const styles = StyleSheet.create({
  appLogo: {
    width: 32,
    height: 32,
    marginLeft: 10,
  },
  searchIcon: {
    marginRight: 10,
  },
});
