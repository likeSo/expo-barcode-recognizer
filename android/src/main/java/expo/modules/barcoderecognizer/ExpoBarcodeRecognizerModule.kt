package expo.modules.barcoderecognizer

import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import expo.modules.kotlin.Promise
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition
import java.net.URL
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import expo.modules.kotlin.exception.CodedException
import expo.modules.kotlin.exception.toCodedException
import java.io.IOException

class ExpoBarcodeRecognizerModule : Module() {
    override fun definition() = ModuleDefinition {
        Name("ExpoBarcodeRecognizer")

        AsyncFunction("recognizeCodeFromImageAsync") { options: ExpoBarcodeRecognizeOptions, promise: Promise ->
            val inputImage: InputImage
            if (options.base64OrImageUri.startsWith("file://")) {
                try {
                    inputImage = InputImage.fromFilePath(
                        appContext.reactContext!!,
                        options.base64OrImageUri.toUri()
                    )
                } catch (e: IOException) {
                    promise.reject(
                        CodedException(
                            "ERR_NO_INPUT_IMAGE",
                            "Please provide a valid image",
                            e
                        )
                    )
                    return@AsyncFunction
                }
            } else {
                val bytes = Base64.decode(options.base64OrImageUri, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                try {
                    inputImage = InputImage.fromBitmap(bitmap, options.rotationDegrees)
                } catch (e: IOException) {
                    promise.reject(
                        CodedException(
                            "ERR_NO_INPUT_IMAGE",
                            "Please provide a valid image",
                            e
                        )
                    )
                    return@AsyncFunction
                }
            }
            var formatsIntArray = options.formats?.map { it.value }?.toIntArray()
            if (formatsIntArray == null) {
                formatsIntArray = intArrayOf(ExpoBarcodeFormat.all.value)
            }
            val otherFormats = formatsIntArray.drop(1).toIntArray()

            val scannerOptions = BarcodeScannerOptions.Builder()
                .setBarcodeFormats(formatsIntArray.firstOrNull()!!, *otherFormats)
                .enableAllPotentialBarcodes()
//        .setZoomSuggestionOptions()
                .build()


            val scanner = BarcodeScanning.getClient(scannerOptions)
            scanner.process(inputImage)
                .addOnSuccessListener { barcodes ->
                    if (barcodes != null && barcodes.isNotEmpty()) {
                        val barcodeRecords = barcodes.map { barcode ->
                            return@map barcode.toBundle()
                        }
                        promise.resolve(barcodeRecords)
                    } else {
                        promise.reject("ERR_NO_RESULT", "There is no barcode found", null)
                    }
                }
                .addOnFailureListener { error ->
                    promise.reject(error.toCodedException())
                }
        }
    }
}
