import React from "react";
import { StyleSheet, Text, View } from "react-native";

interface NotificationsProps {}

const Notifications: React.FC<NotificationsProps> = ({}) => {
  return (
    <View style={styles.container}>
      <Text>Notifications</Text>
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

export default Notifications;
