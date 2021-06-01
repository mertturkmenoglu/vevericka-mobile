import "react-native-gesture-handler";
import * as React from "react";
import Routes from "./Routes";
import { Provider as PaperProvider } from "react-native-paper";
import { AuthProvider } from "./context/AuthContext";
import { primary } from "./constants/Colors";
import { StatusBar } from "react-native";

const App = () => {
  return (
    <AuthProvider>
      <PaperProvider>
        <StatusBar barStyle="light-content" backgroundColor={primary} />
        <Routes />
      </PaperProvider>
    </AuthProvider>
  );
};

export default App;
