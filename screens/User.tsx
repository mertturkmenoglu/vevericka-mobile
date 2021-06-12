import React, { useContext, useEffect, useState } from "react";
import {
  RefreshControl,
  ScrollView,
  StyleSheet,
  Text,
  View,
} from "react-native";
import { AuthContext } from "../context/AuthContext";
import { UserNavProps } from "../navigation/UserStack";
import UserModel from "../models/User";
import UserService from "../api/user";
import UserHeader from "../components/UserHeader";
import UserAction from "../components/UserAction";
import UserPosts from "../components/UserPosts";
import { primary } from "../constants/Colors";
import PostService from "../api/post";
import Post from "../models/Post";

type UserProps = UserNavProps<"User">;

const User: React.FC<UserProps> = ({ route }) => {
  const [user, setUser] = useState<UserModel | null>(null);
  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);
  const [posts, setPosts] = useState<Post[]>([]);
  const authContext = useContext(AuthContext);

  const fetchUser = async (username: string) => {
    try {
      const userService = new UserService(authContext.user?.token || "");
      return await userService.getUserByUsername(username);
    } catch (e) {
      return null;
    }
  };

  const fetchUserPosts = async (username: string) => {
    try {
      const postService = new PostService(authContext.user?.token || "");
      return await postService.getUserPosts(username);
    } catch (e) {
      return [];
    }
  };

  const onRefresh = async () => {
    setLoading(true);
    setRefreshing(true);
    const user = await fetchUser(route.params.username);
    setUser(user);
    const posts = await fetchUserPosts(route.params.username);
    setPosts(posts);
    setLoading(false);
    setRefreshing(false);
  };

  useEffect(() => {
    if (loading) {
      fetchUser(route.params.username)
        .then((user) => {
          setUser(user);
          return fetchUserPosts(route.params.username);
        })
        .then((posts) => {
          setPosts(posts);
        })
        .catch((e) => {
          console.log(e);
        })
        .finally(() => {
          setLoading(false);
        });
    }
  }, [setUser, setLoading, route]);

  if (user === null) {
    return (
      <View>
        <Text>User not found</Text>
      </View>
    );
  }

  return (
    <ScrollView
      refreshControl={
        <RefreshControl
          refreshing={refreshing}
          onRefresh={onRefresh}
          colors={[primary]}
        />
      }
    >
      <UserHeader user={user} />
      <UserAction user={user} />
      <UserPosts posts={posts} />
    </ScrollView>
  );
};

const styles = StyleSheet.create({});

export default User;
