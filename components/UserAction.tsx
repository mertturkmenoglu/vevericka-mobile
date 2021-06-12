import React, { useContext } from "react";
import User from "../models/User";
import { AuthContext } from "../context/AuthContext";
import { StyleSheet, Text } from "react-native";
import { Card } from "react-native-paper";
import { primary } from "../constants/Colors";

interface Props {
  user: User;
}

const UserAction: React.FC<Props> = ({ user }) => {
  const authContext = useContext(AuthContext);

  if (user.username === authContext.user?.username) {
    return (
      <Card mode="outlined" style={styles.card}>
        <Card.Title title="Settings" titleStyle={styles.titleStyle} />
      </Card>
    );
  } else if (
    user.followers.some((u) => u.username === authContext.user?.username)
  ) {
    return <Text>Unfollow</Text>;
  } else {
    return <Text>Follow</Text>;
  }
};

const styles = StyleSheet.create({
  card: {
    marginHorizontal: 10,
    marginTop: 10,
  },
  titleStyle: {
    textAlign: "center",
    color: primary,
    fontWeight: "normal",
    fontSize: 16,
  },
});

export default UserAction;
