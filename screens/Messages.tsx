import React from "react";
import { StyleSheet, Text, View } from "react-native";

interface MessagesProps {}

const Messages: React.FC<MessagesProps> = ({}) => {
  return (
    <View style={styles.container}>
      <Text>Messages</Text>
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

export default Messages;
