import * as React from "react";
import { AppRegistry } from "react-native";
import { Provider as PaperProvider } from "react-native-paper";
import Login from "./screens/Login";

export default function Main() {
  return (
    <PaperProvider>
      <Login />
    </PaperProvider>
  );
}

AppRegistry.registerComponent("Vevericka", () => Main);
