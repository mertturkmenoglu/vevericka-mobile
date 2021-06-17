import React, { useContext, useEffect, useState } from "react";
import { FlatList, RefreshControl, ScrollView, StyleSheet } from "react-native";
import { Card, Paragraph } from "react-native-paper";
import { AuthContext } from "../context/AuthContext";
import { primary } from "../constants/Colors";
import MessageService from "../api/message";
import Chat from "../models/Chat";
import { MessageNavProps } from "../navigation/MessageStack";

type MessageProps = MessageNavProps<"Messages">;

const Messages: React.FC<MessageProps> = ({ navigation }: MessageProps) => {
  const authContext = useContext(AuthContext);
  const [chats, setChats] = useState<Chat[]>([]);
  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);

  const fetchChats = async (): Promise<Chat[]> => {
    try {
      const messageService = new MessageService(authContext.user?.token || "");
      return await messageService.getUserChats(
        authContext.user?.username || ""
      );
    } catch (e) {
      return [];
    }
  };

  const onRefresh = async () => {
    setLoading(true);
    setRefreshing(true);
    const c = await fetchChats();
    setChats(c);
    setRefreshing(false);
    setLoading(false);
  };

  useEffect(() => {
    if (loading) {
      fetchChats().then((c) => {
        setChats(c);
        setLoading(false);
      });
    }
  }, [setChats, fetchChats, loading, setLoading]);

  if (!loading && chats.length === 0) {
    return (
      <ScrollView
        contentContainerStyle={{ flexGrow: 1, justifyContent: "center" }}
        refreshControl={
          <RefreshControl
            refreshing={refreshing}
            onRefresh={onRefresh}
            colors={[primary]}
          />
        }
      >
        <Paragraph style={{ textAlign: "center" }}>No chats</Paragraph>
      </ScrollView>
    );
  }

  return (
    <>
      <FlatList
        style={styles.feed}
        data={chats}
        keyExtractor={(item) => item._id}
        refreshControl={
          <RefreshControl
            refreshing={refreshing}
            onRefresh={onRefresh}
            colors={[primary]}
          />
        }
        renderItem={({ item }) => (
          <Card mode="outlined" style={styles.post}>
            <Card.Title
              title={item.chatName}
              subtitle={new Date(item.updatedAt).toLocaleDateString()}
            />
            <Card.Content>
              <Paragraph>{item.lastMessage?.content ?? "No message"}</Paragraph>
            </Card.Content>
          </Card>
        )}
      />
    </>
  );
};

const styles = StyleSheet.create({
  feed: {
    marginHorizontal: 10,
  },
  loadingIconContainer: {
    width: "100%",
    height: "100%",
    alignSelf: "center",
    alignItems: "center",
    alignContent: "center",
    justifyContent: "center",
  },
  loadingIcon: {
    width: 48,
    height: 48,
  },
  img: {
    width: 48,
    height: 48,
    borderRadius: 500,
  },
  post: {
    marginTop: 10,
  },
  cardActions: {
    alignSelf: "flex-end",
  },
  postDate: {
    alignSelf: "flex-start",
  },
  fab: {
    position: "absolute",
    margin: 10,
    right: 0,
    bottom: 0,
  },
});

export default Messages;
