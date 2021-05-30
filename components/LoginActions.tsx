import React from "react";
import { StyleSheet, View, Text } from "react-native";

const LoginActions = () => (
  <View style={styles.actionsContainer}>
    <Text style={styles.registerText}>
      New User? <Text style={styles.actionText}>Register</Text>
    </Text>
    <Text style={styles.forgotPasswordText}>
      Forgot password? <Text style={styles.actionText}>Reset</Text>
    </Text>
  </View>
);

const styles = StyleSheet.create({
  actionsContainer: {
    marginTop: 20,
    alignItems: "center",
  },
  registerText: {
    fontSize: 14,
    color: "#757575",
  },
  forgotPasswordText: {
    fontSize: 14,
    marginTop: 14,
    color: "#757575",
  },
  actionText: {
    color: "#ff5722",
  },
});

export default LoginActions;
