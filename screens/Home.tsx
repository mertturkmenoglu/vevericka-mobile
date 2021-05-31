import React, { useContext } from "react";
import { StyleSheet, Text } from "react-native";
import { Button } from "react-native-paper";
import { AuthContext } from "../context/AuthContext";

const Home = () => {
  const authContext = useContext(AuthContext);

  const onLogoutButtonPress = async () => {
    await authContext.logout();
  };

  return (
    <>
      <Text style={styles.username}>{authContext.user?.username}</Text>
      <Button mode="text" onPress={onLogoutButtonPress}>
        Logout
      </Button>
    </>
  );
};

const styles = StyleSheet.create({
  username: {
    marginTop: 300,
    alignSelf: "center",
  },
});

export default Home;
