import React, { useContext } from "react";
import { View, StyleSheet, Text } from "react-native";
import { AuthContext } from "../context/AuthContext";

interface UserProps {}

const User: React.FC<UserProps> = ({}) => {
  const authContext = useContext(AuthContext);

  return (
    <View style={styles.container}>
      <Text>{authContext.user?.username}</Text>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: "center",
    justifyContent: "center",
  },
});

export default User;
