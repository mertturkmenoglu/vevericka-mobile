import React, { useContext, useEffect, useState } from "react";
import { NavigationContainer } from "@react-navigation/native";
import { AuthContext } from "../context/AuthContext";
import { getUserFromStorage } from "../utils/UserStorageUtils";
import { ActivityIndicator, View } from "react-native";
import { AuthStack } from "./AuthStack";
import { AppTabs } from "./AppTabs";

interface RoutesProps {}

const Routes: React.FC<RoutesProps> = ({}): JSX.Element => {
  const authContext = useContext(AuthContext);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const asyncBootstrap = async () => {
      try {
        const token = await getUserFromStorage();
        if (token !== null) {
          authContext.user = token;
        }
        setLoading(false);
      } catch (e) {
        console.log("error", e);
      }
    };

    asyncBootstrap();
  }, []);

  if (loading) {
    return (
      <View>
        <ActivityIndicator size="large" />
      </View>
    );
  }

  return (
    <NavigationContainer>
      {authContext.user !== null ? <AppTabs /> : <AuthStack />}
    </NavigationContainer>
  );
};

export default Routes;
