import React from "react";
import { Button, Card, Paragraph } from "react-native-paper";
import { Image, StyleSheet } from "react-native";
import { primary } from "../constants/Colors";
import User from "../models/User";

interface Props {
  user: User;
}

const UserHeader: React.FC<Props> = ({ user }) => {
  return (
    <Card mode="outlined" style={styles.header}>
      <Card.Title
        title={user?.name}
        subtitle={`@${user?.username}`}
        style={{
          alignItems: "center",
        }}
        left={() => (
          <Image
            style={styles.img}
            source={
              user?.image === "profile.png"
                ? require("../assets/profile.png")
                : { uri: user?.image }
            }
          />
        )}
      />
      <Card.Content>
        <Paragraph style={{ textAlign: "center" }}>{user?.bio}</Paragraph>
      </Card.Content>
      <Card.Actions style={styles.cardActions}>
        <Button
          mode="text"
          color={primary}
          theme={{
            colors: {
              primary,
              background: primary,
            },
          }}
        >
          {`${user?.followers.length} followers`}
        </Button>
        <Button
          mode="text"
          color={primary}
          theme={{
            colors: {
              primary,
              background: primary,
            },
          }}
        >
          {`${user?.following.length} following`}
        </Button>
      </Card.Actions>
    </Card>
  );
};

const styles = StyleSheet.create({
  cardActions: {
    alignSelf: "center",
  },
  img: {
    width: 48,
    height: 48,
    borderRadius: 500,
  },
  header: {
    marginHorizontal: 10,
    marginTop: 10,
  },
});

export default UserHeader;
