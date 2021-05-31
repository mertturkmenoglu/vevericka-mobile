import React from "react";
import { StyleSheet, Text, View } from "react-native";

interface SearchProps {}

const Search: React.FC<SearchProps> = ({}) => {
  return (
    <View style={styles.container}>
      <Text>Search</Text>
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

export default Search;
