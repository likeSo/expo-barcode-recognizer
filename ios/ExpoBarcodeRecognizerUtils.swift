//
//  ExpoBarcodeRecognizerUtils.swift
//  ExpoBarcodeRecognizer
//
//  Created by Aron on 2025/6/23.
//

import Foundation


struct ExpoBarcodeRecognizerUtils {
    static func getUIImage(fromBase64OrImageUri base64OrImageUri: String) -> UIImage? {
        if base64OrImageUri.hasPrefix("file://") {
            let path = String(base64OrImageUri[base64OrImageUri.index(base64OrImageUri.startIndex, offsetBy: 7)...])
            return UIImage(contentsOfFile: path)
            
        } else if let imageData = Data(base64Encoded: base64OrImageUri, options: .ignoreUnknownCharacters) {
            return UIImage(data: imageData)
        }
        
        return nil
    }
}
