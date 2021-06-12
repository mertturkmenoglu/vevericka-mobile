import React, { useContext, useEffect, useState } from "react";
import { FlatList, RefreshControl, StyleSheet } from "react-native";
import { Card, Paragraph } from "react-native-paper";
import { AuthContext } from "../context/AuthContext";
import { primary } from "../constants/Colors";
import { ExploreNavProps } from "../navigation/ExploreStack";
import Tag from "../models/Tag";
import ExploreService from "../api/explore";

type ExploreProps = ExploreNavProps<"Explore">;

const Explore: React.FC<ExploreProps> = ({ navigation }: ExploreProps) => {
  const authContext = useContext(AuthContext);
  const [tags, setTags] = useState<Tag[]>();
  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);

  const fetchTags = async () => {
    try {
      const exploreService = new ExploreService(authContext.user?.token || "");
      return await exploreService.getTags();
    } catch (e) {
      console.log(e);
      return [];
    }
  };

  const onRefresh = async () => {
    setLoading(true);
    setRefreshing(true);
    const tags = await fetchTags();
    setTags(tags);
    setRefreshing(false);
    setLoading(false);
  };

  useEffect(() => {
    if (loading) {
      fetchTags()
        .then((tags) => {
          setTags(tags);
          setLoading(false);
        })
        .catch(() => {
          setLoading(false);
        });
    }
  }, [fetchTags, loading, setLoading]);

  return (
    <>
      <FlatList
        style={styles.feed}
        data={tags}
        keyExtractor={(item, index) => item.tag}
        refreshControl={
          <RefreshControl
            refreshing={refreshing}
            onRefresh={onRefresh}
            colors={[primary]}
          />
        }
        renderItem={({ item }) => (
          <Card
            mode="outlined"
            style={styles.tag}
            onPress={() => {
              console.log(item.tag);
            }}
          >
            <Card.Title title={`# ${item.tag}`} titleStyle={styles.cardTitle} />
            <Card.Content>
              <Paragraph>{`${item.count} posts`}</Paragraph>
            </Card.Content>
          </Card>
        )}
      />
    </>
  );
};

const styles = StyleSheet.create({
  feed: {
    marginHorizontal: 10,
  },
  loadingIconContainer: {
    width: "100%",
    height: "100%",
    alignSelf: "center",
    alignItems: "center",
    alignContent: "center",
    justifyContent: "center",
  },
  cardTitle: {
    color: primary,
  },
  loadingIcon: {
    width: 48,
    height: 48,
  },
  tag: {
    marginTop: 10,
  },
});

export default Explore;
