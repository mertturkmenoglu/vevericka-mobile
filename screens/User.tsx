import React, { useContext } from "react";
import { View, StyleSheet, Text } from "react-native";
import { AuthContext } from "../context/AuthContext";
import { Button } from "react-native-paper";

interface UserProps {}

const User: React.FC<UserProps> = ({}) => {
  const authContext = useContext(AuthContext);

  return (
    <View style={styles.container}>
      <Text>{authContext.user?.username}</Text>
      <Button
        onPress={async () => {
          await authContext.logout();
        }}
      >
        Logout
      </Button>
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
