import { registerWebModule, NativeModule } from 'expo';

import { RecognizeOptions, ExpoBarcodeRecognizerModuleEvents } from './ExpoBarcodeRecognizer.types';

class ExpoBarcodeRecognizerModule extends NativeModule<ExpoBarcodeRecognizerModuleEvents> {
  recognizeCodeFromImageAsync(options: RecognizeOptions): Promise<string> {
    return Promise.resolve('')
  }
}

export default registerWebModule(ExpoBarcodeRecognizerModule, 'ExpoBarcodeRecognizerModule');
