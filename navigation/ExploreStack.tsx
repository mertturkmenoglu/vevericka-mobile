import React from "react";
import {
  createStackNavigator,
  StackNavigationProp,
} from "@react-navigation/stack";
import { RouteProp } from "@react-navigation/native";
import { primary } from "../constants/Colors";
import { MaterialCommunityIcons } from "@expo/vector-icons";
import { StyleSheet } from "react-native";
import Explore from "../screens/Explore";

interface ExploreStackProps {}

export type ExploreParamList = {
  Explore: undefined;
};

export type ExploreNavProps<T extends keyof ExploreParamList> = {
  navigation: StackNavigationProp<ExploreParamList, T>;
  route: RouteProp<ExploreParamList, T>;
};

const Stack = createStackNavigator<ExploreParamList>();

export const ExploreStack: React.FC<ExploreStackProps> = ({}) => {
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
          title: "Explore",
          headerLeft: () => (
            <MaterialCommunityIcons
              style={styles.ExploreIcon}
              name="pound"
              size={32}
              color={primary}
            />
          ),
        }}
        name="Explore"
        component={Explore}
      />
    </Stack.Navigator>
  );
};

const styles = StyleSheet.create({
  ExploreIcon: {
    width: 32,
    height: 32,
    marginLeft: 10,
  },
});
