import React from "react";
import { Image, StyleSheet, Text } from "react-native";
import { Card, Paragraph } from "react-native-paper";
import { primary } from "../constants/Colors";
import Post from "../models/Post";
import { MaterialCommunityIcons } from "@expo/vector-icons";

interface Props {
  posts: Post[];
}

const UserPosts: React.FC<Props> = ({ posts }) => {
  return (
    <>
      {posts.map((post) => (
        <Card mode="outlined" style={styles.post} key={post._id}>
          <Card.Title
            title={post.createdBy.name}
            subtitle={`@${post.createdBy.username}`}
            left={() => (
              <Image
                style={styles.img}
                source={
                  post.createdBy.image === "profile.png"
                    ? require("../assets/profile.png")
                    : { uri: post.createdBy.image }
                }
              />
            )}
          />
          <Card.Content>
            <Paragraph>{post.content}</Paragraph>
          </Card.Content>
          <Card.Actions style={styles.cardActions}>
            <Text style={styles.postDate}>
              {new Date(post.createdAt).toLocaleDateString()}
            </Text>
            <MaterialCommunityIcons
              name="comment-outline"
              color={primary}
              size={24}
            />
          </Card.Actions>
        </Card>
      ))}
    </>
  );
};

const styles = StyleSheet.create({
  titleStyle: {
    textAlign: "center",
    color: primary,
    fontWeight: "normal",
    fontSize: 16,
  },
  img: {
    width: 48,
    height: 48,
    borderRadius: 500,
  },
  post: {
    marginTop: 10,
    marginHorizontal: 10,
  },
  cardActions: {
    alignSelf: "flex-end",
  },
  postDate: {
    alignSelf: "flex-start",
  },
});

export default UserPosts;
