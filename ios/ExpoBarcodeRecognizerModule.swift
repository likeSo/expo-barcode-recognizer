import ExpoModulesCore
import MLKitBarcodeScanning
import MLKitVision

public class ExpoBarcodeRecognizerModule: Module {
    public func definition() -> ModuleDefinition {
        Name("ExpoBarcodeRecognizer")
        
        AsyncFunction("recognizeCodeFromImageAsync") { (options: ExpoBarcodeRecognizeOptions,
                                                        promise: Promise) in
            if let image = ExpoBarcodeRecognizerUtils.getUIImage(fromBase64OrImageUri: options.base64OrImageUri) {
                var scannerFormats: BarcodeFormat = []
                if let formats = options.formats {
                    formats.forEach { format in
                        scannerFormats.insert(BarcodeFormat(rawValue: format.rawValue))
                    }
                } else {
                    scannerFormats.insert(.all)
                }
                let scannerOptions = BarcodeScannerOptions(formats: scannerFormats)
                
                let image = VisionImage(image: image)
                image.orientation = UIImage.Orientation(rawValue: options.orientation.rawValue) ?? .up
                
                let scanner = BarcodeScanner.barcodeScanner(options: scannerOptions)
                scanner.process(image) { outputs, error in
                    if let results = outputs, !results.isEmpty {
                        let records = results.map { barcode in
                            return barcode.toDictionary()
                        }
                        promise.resolve(records)
                    } else if error != nil {
                        promise.reject(error!)
                    } else {
                        promise.reject("ERR_NO_RESULT", "There is no barcode found")
                    }
                }
            } else {
                promise.reject("ERR_NO_INPUT_IMAGE", "Please provide a valid image")
            }
        }
    }
}
