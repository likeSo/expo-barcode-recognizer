import { useEvent } from "expo";
import ExpoBarcodeRecognizer, {
  ExpoBarcodeRecognizeResult,
} from "expo-barcode-recognizer";
import {
  Alert,
  Button,
  SafeAreaView,
  ScrollView,
  Text,
  View,
} from "react-native";
import { useState } from "react";
import { launchImageLibraryAsync } from "expo-image-picker";

export default function App() {
  const [recoginizeResult, setRecoginizeResult] =
    useState<ExpoBarcodeRecognizeResult[]>();

  return (
    <SafeAreaView style={styles.container}>
      <ScrollView style={styles.container}>
        <Text style={styles.header}>Module API Example</Text>
        <Group name="Constants">
          <Text>{ExpoBarcodeRecognizer.PI}</Text>
        </Group>
        <Group name="Pick Image and Recognize">
          <Button
            title="Pick Image and Recognize"
            onPress={async () => {
              const image = await launchImageLibraryAsync({
                mediaTypes: "images",
                allowsEditing: false,
                selectionLimit: 1,
                quality: 1,
              });
              console.log(image);
              if (!image.canceled) {
                ExpoBarcodeRecognizer.recognizeCodeFromImageAsync({
                  base64OrImageUri: image.assets[0].uri,
                })
                  .then((result) => {
                    setRecoginizeResult(result);
                  })
                  .catch((reason) => {
                    Alert.alert(JSON.stringify(reason));
                  });
              }
            }}
          />
          <Text>{recoginizeResult && JSON.stringify(recoginizeResult)}</Text>
        </Group>
      </ScrollView>
    </SafeAreaView>
  );
}

function Group(props: { name: string; children: React.ReactNode }) {
  return (
    <View style={styles.group}>
      <Text style={styles.groupHeader}>{props.name}</Text>
      {props.children}
    </View>
  );
}

const styles = {
  header: {
    fontSize: 30,
    margin: 20,
  },
  groupHeader: {
    fontSize: 20,
    marginBottom: 20,
  },
  group: {
    margin: 20,
    backgroundColor: "#fff",
    borderRadius: 10,
    padding: 20,
  },
  container: {
    flex: 1,
    backgroundColor: "#eee",
  },
  view: {
    flex: 1,
    height: 200,
  },
};
