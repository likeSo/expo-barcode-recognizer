## expo-barcode-recognizer

基于 Google ML Kit 的本地二维码/条形码识别器，提供一个简单的方法从本地图片中识别条码信息，完全离线运行。
A lightweight on-device barcode/QR recognizer powered by Google ML Kit. It exposes a single method to recognize barcodes from an image.

### 特性
- **本地识别**: 依赖 Google ML Kit，离线运行，低延迟。
- **跨平台**: iOS 与 Android 支持。
- **简单 API**: 一个方法完成识别，支持常见条码格式与详细结构化结果。

### 安装
```sh
npx expo install expo-barcode-recognizer
```

由于包含原生代码，请用`npx expo prebuild`或者`npx expo run:android|ios`的方式启动项目。

> 提示：如果你搭配 `expo-image-picker` 选择图片，请参考它的文档配置相册/相机权限。

### 快速开始
以下示例基于 `example/App.tsx`，演示从相册选择图片并识别：

```tsx
import ExpoBarcodeRecognizer, { ExpoBarcodeRecognizeResult } from "expo-barcode-recognizer";
import { launchImageLibraryAsync } from "expo-image-picker";

async function pickAndRecognize() {
  const image = await launchImageLibraryAsync({
    mediaTypes: "images",
    allowsEditing: false,
    selectionLimit: 1,
    quality: 1,
  });

  if (!image.canceled) {
    const results: ExpoBarcodeRecognizeResult[] = await ExpoBarcodeRecognizer.recognizeCodeFromImageAsync({
      base64OrImageUri: image.assets[0].uri,
      // 可选：formats, orientation(iOS), rotationDegrees(Android)
    });

    console.log(results);
  }
}
```

### API 参考

#### recognizeCodeFromImageAsync(options)
从图片中识别条码/二维码。

```ts
type RecognizeOptions = {
  base64OrImageUri: string;            // 必填，本地图片 URI 或 base64
  orientation?: InputImageOrientation; // 可选，仅 iOS
  formats?: BarcodeFormat[];           // 可选，限定识别格式
  rotationDegrees?: number;            // 可选，仅 Android，旋转角度
};

function recognizeCodeFromImageAsync(
  options: RecognizeOptions
): Promise<ExpoBarcodeRecognizeResult[]>;
```

返回值 `ExpoBarcodeRecognizeResult[]`（节选字段）：

```ts
interface ExpoBarcodeRecognizeResult {
  rawValue?: string;                 // 条码原始内容
  format: BarcodeFormat;             // 条码格式
  valueType: ExpoBarcodeValueType;   // 值类型（文本、URL、WiFi、联系人等）
  frame?: { x: number; y: number; width: number; height: number };
  cornerPoints?: { x: number; y: number }[];
  // 根据 valueType 可能包含：email, phone, sms, url, wifi, geoPoint,
  // contactInfo, calendarEvent, driverLicense 等结构化信息
}
```



### 开发与示例
仓库包含一个可运行的示例应用：`example/`。
- 进入示例并启动开发：
```sh
cd example
npm install
npm run start
```

### 注意事项
MLKit会造成安装包增大。比如，在安卓上，大小增加约 2.4 MB。更多信息可以在[这里](https://developers.google.com/ml-kit/vision/barcode-scanning?hl=zh-cn)看。
iOS的`GoogleMLKit/BarcodeScanning`（iOS基于此）不支持arm架构的模拟器，如果你希望在iOS模拟器上运行，请运行在Rosetta模拟器上，或者使用真机。

### 联系我
本框架积极维护，如有任何问题，欢迎提交issue或者PR。 QQ 群：682911244。

### 许可
MIT
