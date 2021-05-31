import "react-native-gesture-handler";
import * as React from "react";
import Routes from "./Routes";
import { Provider as PaperProvider } from "react-native-paper";
import { AuthProvider } from "./context/AuthContext";

const App = () => {
  return (
    <AuthProvider>
      <PaperProvider>
        <Routes />
      </PaperProvider>
    </AuthProvider>
  );
};

export default App;
