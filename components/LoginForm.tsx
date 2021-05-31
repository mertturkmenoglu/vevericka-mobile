import React, { useContext, useState } from "react";
import { StyleSheet, View } from "react-native";
import { Button, TextInput } from "react-native-paper";
import { primary } from "../constants/Colors";
import { AuthContext } from "../context/AuthContext";

interface Props {
  setSnackbar: () => void;
}

const LoginForm = (props: Props) => {
  const authContext = useContext(AuthContext);
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [isPasswordHidden, setIsPasswordHidden] = useState(true);

  const onLoginButtonPress = async () => {
    try {
      await authContext.login(email, password);
    } catch (e) {
      props.setSnackbar();
    }
  };

  return (
    <View style={styles.formContainer}>
      <TextInput
        label="Email"
        mode="outlined"
        theme={{
          colors: { primary, background: "white" },
        }}
        left={<TextInput.Icon name="email" color="#757575" />}
        value={email}
        onChangeText={(text) => setEmail(text.trim())}
      />
      <TextInput
        label="Password"
        mode="outlined"
        theme={{
          colors: { primary, background: "white" },
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
        onChangeText={(text) => setPassword(text.trim())}
      />
      <Button
        mode="text"
        style={styles.loginButton}
        onPress={onLoginButtonPress}
        color={primary}
        theme={{
          colors: {
            primary,
            background: primary,
          },
        }}
      >
        Login
      </Button>
    </View>
  );
};

const styles = StyleSheet.create({
  formContainer: {
    alignSelf: "stretch",
    marginHorizontal: 10,
    marginTop: 20,
  },
  loginButton: {
    marginTop: 20,
  },
});

export default LoginForm;
