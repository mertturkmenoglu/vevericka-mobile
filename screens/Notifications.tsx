import React, { useContext, useEffect, useState } from "react";
import {
  FlatList,
  Image,
  RefreshControl,
  ScrollView,
  StyleSheet,
  Text,
} from "react-native";
import { Card, Paragraph } from "react-native-paper";
import { AuthContext } from "../context/AuthContext";
import { MaterialCommunityIcons } from "@expo/vector-icons";
import { primary } from "../constants/Colors";
import { NotificationNavProps } from "../navigation/NotificationStack";
import Notification from "../models/Notification";
import NotificationService from "../api/notification";
import NotificationType from "../models/NotificationType";

type NotificationProps = NotificationNavProps<"Notifications">;

const Notifications: React.FC<NotificationProps> = ({
  navigation,
}: NotificationProps) => {
  const authContext = useContext(AuthContext);
  const [notifications, setNotifications] = useState<Notification[]>([]);
  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);

  const fetchNotifications = async () => {
    try {
      const notificationService = new NotificationService(
        authContext.user?.token || ""
      );
      return await notificationService.getNotifications(
        authContext.user?.username || ""
      );
    } catch (e) {
      console.log(e);
      return [];
    }
  };

  const deleteNotification = async (id: string) => {
    try {
      const notificationService = new NotificationService(
        authContext.user?.token || ""
      );
      await notificationService.deleteNotification(id);
      setNotifications((prevState) => {
        return prevState?.filter((it) => it._id !== id);
      });
    } catch (e) {
      console.log(e);
    }
  };

  const getNotificationText = (notificationType: NotificationType): string => {
    if (notificationType === NotificationType.ON_MENTION) {
      return "Mentioned you in a post";
    } else if (notificationType === NotificationType.ON_USER_FOLLOW) {
      return "Started following you";
    }
    return "";
  };

  const onRefresh = async () => {
    setLoading(true);
    setRefreshing(true);
    const notifications = await fetchNotifications();
    setNotifications(notifications);
    setRefreshing(false);
    setLoading(false);
  };

  useEffect(() => {
    if (loading) {
      fetchNotifications()
        .then((notifications) => {
          setNotifications(notifications);
          setLoading(false);
        })
        .catch(() => {
          setLoading(false);
        });
    }
  }, [fetchNotifications, loading, setLoading]);

  if (!loading && notifications.length === 0) {
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
        <Paragraph style={{ textAlign: "center" }}>No notifications</Paragraph>
      </ScrollView>
    );
  }

  return (
    <>
      <FlatList
        style={styles.feed}
        data={notifications}
        keyExtractor={(item, index) => item._id}
        refreshControl={
          <RefreshControl
            refreshing={refreshing}
            onRefresh={onRefresh}
            colors={[primary]}
          />
        }
        renderItem={({ item }) => (
          <Card
            mode="outlined"
            style={styles.notification}
            onPress={() => {
              console.log(item.origin.username);
            }}
          >
            <Card.Title
              title={item.origin.name}
              subtitle={`@${item.origin.username}`}
              left={() => (
                <Image
                  style={styles.img}
                  source={
                    item.origin.image === "profile.png"
                      ? require("../assets/profile.png")
                      : { uri: item.origin.image }
                  }
                />
              )}
              right={() => (
                <MaterialCommunityIcons
                  style={styles.closeIcon}
                  name="close"
                  color={primary}
                  size={24}
                  onPress={async () => {
                    await deleteNotification(item._id);
                  }}
                />
              )}
            />
            <Card.Content>
              <Paragraph>{getNotificationText(item.type)}</Paragraph>
            </Card.Content>
            <Card.Actions style={styles.cardActions}>
              <Text style={styles.notificationDate}>
                {new Date(item.createdAt).toLocaleDateString()}
              </Text>
            </Card.Actions>
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
  loadingIcon: {
    width: 48,
    height: 48,
  },
  img: {
    width: 48,
    height: 48,
    borderRadius: 500,
  },
  closeIcon: {
    marginEnd: 12,
    marginBottom: 12,
  },
  notification: {
    marginTop: 10,
  },
  cardActions: {
    alignSelf: "flex-end",
  },
  notificationDate: {
    alignSelf: "flex-start",
  },
});

export default Notifications;
