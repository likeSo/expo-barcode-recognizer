// Reexport the native module. On web, it will be resolved to ExpoBarcodeRecognizerModule.web.ts
// and on native platforms to ExpoBarcodeRecognizerModule.ts
export { default } from './ExpoBarcodeRecognizerModule';
export * from  './ExpoBarcodeRecognizer.types';
