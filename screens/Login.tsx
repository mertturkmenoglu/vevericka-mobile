import React, { useState } from "react";
import { Image, ScrollView, StyleSheet, Text, View } from "react-native";
import { Snackbar } from "react-native-paper";
import LoginActions from "../components/LoginActions";
import LoginForm from "../components/LoginForm";
import { primary } from "../constants/Colors";
import { AuthNavProps } from "../AuthStack";

type Props = AuthNavProps<"Login">;

export default function Login(props: Props) {
  const [isSnackbarVisible, setIsSnackbarVisible] = useState(false);

  return (
    <>
      <ScrollView>
        <View style={styles.container}>
          <View style={styles.imageContainer}>
            <Image
              style={styles.logo}
              source={require("../assets/icon-white.png")}
              width={100}
              height={100}
            />
            <Text style={styles.appName}>Vevericka</Text>
            <Text style={styles.appMotto}>
              We are the squirrels who say Vik!
            </Text>
          </View>

          <Text style={styles.loginTitle}>Login</Text>

          <LoginForm setSnackbar={() => setIsSnackbarVisible(true)} />

          <LoginActions />
        </View>
      </ScrollView>
      <Snackbar
        visible={isSnackbarVisible}
        onDismiss={() => setIsSnackbarVisible(false)}
        action={{
          label: "Close",
          onPress: () => {
            setIsSnackbarVisible(false);
          },
        }}
        duration={3000}
        theme={{
          colors: {
            accent: "white",
            onSurface: primary,
          },
        }}
      >
        Cannot login
      </Snackbar>
    </>
  );
}

const styles = StyleSheet.create({
  container: {
    marginVertical: 50,
    alignItems: "center",
  },
  mainContainer: {
    alignSelf: "stretch",
  },
  imageContainer: {
    alignSelf: "stretch",
    aspectRatio: 3 / 2,
    backgroundColor: "#ff5722",
    alignItems: "center",
    justifyContent: "center",
    marginHorizontal: 10,
  },
  logo: {
    width: 100,
    height: 100,
  },
  appName: {
    color: "white",
    fontSize: 18,
    marginTop: 12,
  },
  appMotto: {
    color: "white",
    fontSize: 12,
    marginTop: 6,
    textTransform: "uppercase",
  },
  loginTitle: {
    color: "#ff5722",
    fontSize: 24,
    fontWeight: "100",
    marginTop: 20,
    alignSelf: "center",
  },
});
