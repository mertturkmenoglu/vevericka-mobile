import * as React from "react";
import {
  AppRegistry,
  Image,
  ScrollView,
  StyleSheet,
  Text,
  View,
} from "react-native";
import {
  Button,
  Provider as PaperProvider,
  TextInput,
} from "react-native-paper";

export default function Main() {
  const [email, setEmail] = React.useState("");
  const [password, setPassword] = React.useState("");
  const [isPasswordHidden, setIsPasswordHidden] = React.useState(true);

  return (
    <PaperProvider>
      <ScrollView>
        <View style={styles.container}>
          <View style={styles.imageContainer}>
            <Image
              style={styles.logo}
              source={require("./assets/icon-white.png")}
              width={100}
              height={100}
            />
            <Text style={styles.appName}>Vevericka</Text>
            <Text style={styles.appMotto}>
              We are the squirrels who say Vik!
            </Text>
          </View>

          <Text style={styles.loginTitle}>Login</Text>

          <View style={styles.formContainer}>
            <TextInput
              label="Email"
              mode="outlined"
              theme={{
                colors: { primary: "#ff5722", background: "white" },
              }}
              left={<TextInput.Icon name="email" color="#757575" />}
              value={email}
              onChangeText={(text) => setEmail(text)}
            />
            <TextInput
              label="Password"
              mode="outlined"
              theme={{
                colors: { primary: "#ff5722", background: "white" },
              }}
              secureTextEntry={isPasswordHidden}
              left={<TextInput.Icon name="lock" color="#757575" />}
              right={
                <TextInput.Icon
                  name={isPasswordHidden ? "eye-off" : "eye"}
                  color="#757575"
                  onPress={() => setIsPasswordHidden((prev) => !prev)}
                />
              }
              onChangeText={(text) => setPassword(text)}
            />
            <Button
              mode="outlined"
              style={styles.loginButton}
              onPress={() => {
                console.log("pressed");
                console.log(password);
              }}
              color="#ff5722"
            >
              Login
            </Button>
          </View>
          <View style={styles.actionsContainer}>
            <Text style={styles.registerText}>
              New User? <Text style={styles.actionText}>Register</Text>
            </Text>
            <Text style={styles.forgotPasswordText}>
              Forgot password? <Text style={styles.actionText}>Reset</Text>
            </Text>
          </View>
        </View>
      </ScrollView>
    </PaperProvider>
  );
}

const styles = StyleSheet.create({
  container: {
    marginVertical: 50,
    alignItems: "center",
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
  formContainer: {
    alignSelf: "stretch",
    marginHorizontal: 10,
    marginTop: 20,
  },
  loginButton: {
    marginTop: 20,
  },
  loginTitle: {
    color: "#ff5722",
    fontSize: 24,
    fontWeight: "100",
    marginTop: 20,
  },
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

AppRegistry.registerComponent("Vevericka", () => Main);
