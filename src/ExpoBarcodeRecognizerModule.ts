import { NativeModule, requireNativeModule } from 'expo';

import { RecognizeOptions, ExpoBarcodeRecognizerModuleEvents, ExpoBarcodeRecognizeResult } from './ExpoBarcodeRecognizer.types';

declare class ExpoBarcodeRecognizerModule extends NativeModule<ExpoBarcodeRecognizerModuleEvents> {

  recognizeCodeFromImageAsync(options: RecognizeOptions): Promise<ExpoBarcodeRecognizeResult[]>;
}

// This call loads the native module object from the JSI.
export default requireNativeModule<ExpoBarcodeRecognizerModule>('ExpoBarcodeRecognizer');
