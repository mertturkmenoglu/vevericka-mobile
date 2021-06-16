import "react-native-gesture-handler";
import * as React from "react";
import Routes from "./navigation/Routes";
import { Provider as PaperProvider } from "react-native-paper";
import { AuthProvider } from "./context/AuthContext";
import { primary } from "./constants/Colors";
import { StatusBar } from "react-native";

const App = () => {
  return (
    <AuthProvider>
      <PaperProvider>
        <StatusBar barStyle="dark-content" backgroundColor="#fff" />
        <Routes />
      </PaperProvider>
    </AuthProvider>
  );
};

export default App;
