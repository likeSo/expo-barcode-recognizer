//
//  ExpoBarcodeRecognizerTypes.swift
//  ExpoBarcodeRecognizer
//
//  Created by Aron on 2025/6/23.
//

import Foundation
import ExpoModulesCore
import CoreGraphics
import MLKitVision
import MLKitBarcodeScanning

enum ExpoBarcodeFormat: Int, Enumerable {
    case unknown = 0
    case all = 0xFFFF
    case code128 = 0x0001
    case code39 = 0x0002
    case code93 = 0x0004
    case codabar = 0x0008
    case dataMatrix = 0x0010
    case ean13 = 0x0020
    case ean8 = 0x0040
    case itf = 0x0080
    case qrCode = 0x0100
    case upca = 0x0200
    case upce = 0x0400
    case pdf417 = 0x0800
    case aztec = 0x1000
}

enum ExpoBarcodeOrientation: Int, Enumerable {
    case up = 0
    case down = 1
    case left = 2
    case right = 3
    case upMirrored = 4
    case downMirrored = 5
    case leftMirrored = 6
    case rightMirrored = 7
}


struct ExpoBarcodeRecognizeOptions: Record {
    @Field
    var base64OrImageUri: String = ""
    
    @Field
    var formats: [ExpoBarcodeFormat]?
    
    @Field
    var orientation: ExpoBarcodeOrientation = .up
}


// MARK: - 字典转换协议
protocol DictionaryConvertible {
    func toDictionary() -> [String: Any]
}

// MARK: - MLKBarcodeAddress扩展
extension BarcodeAddress: DictionaryConvertible {
    func toDictionary() -> [String: Any] {
        var dict: [String: Any] = [:]
        dict["addressLines"] = addressLines
        dict["type"] = type
        dict["typeName"] = BarcodeConverter.addressTypeName(for: type)
        return dict
    }
}

// MARK: - MLKBarcodeCalendarEvent扩展
extension BarcodeCalendarEvent: DictionaryConvertible {
    func toDictionary() -> [String: Any] {
        var dict: [String: Any] = [:]
        dict["eventDescription"] = eventDescription
        dict["location"] = location
        dict["organizer"] = organizer
        dict["status"] = status
        dict["summary"] = summary
        dict["start"] = start?.timeIntervalSince1970
        dict["end"] = end?.timeIntervalSince1970
        return dict
    }
}

// MARK: - MLKBarcodeDriverLicense扩展
extension BarcodeDriverLicense: DictionaryConvertible {
    func toDictionary() -> [String: Any] {
        var dict: [String: Any] = [:]
        dict["firstName"] = firstName
        dict["middleName"] = middleName
        dict["lastName"] = lastName
        dict["gender"] = gender
        dict["addressCity"] = addressCity
        dict["addressState"] = addressState
        dict["addressStreet"] = addressStreet
        dict["addressZip"] = addressZip
        dict["birthDate"] = birthDate
        dict["documentType"] = documentType
        dict["licenseNumber"] = licenseNumber
        dict["expiryDate"] = expiryDate
        dict["issuingDate"] = issuingDate
        dict["issuingCountry"] = issuingCountry
        return dict
    }
}

// MARK: - MLKBarcodeEmail扩展
extension BarcodeEmail: DictionaryConvertible {
    func toDictionary() -> [String: Any] {
        var dict: [String: Any] = [:]
        dict["address"] = address
        dict["body"] = body
        dict["subject"] = subject
        dict["type"] = type
        dict["typeName"] = BarcodeConverter.emailTypeName(for: type)
        return dict
    }
}

// MARK: - MLKBarcodeGeoPoint扩展
extension BarcodeGeoPoint: DictionaryConvertible {
    func toDictionary() -> [String: Any] {
        var dict: [String: Any] = [:]
        dict["latitude"] = latitude
        dict["longitude"] = longitude
        return dict
    }
}

// MARK: - MLKBarcodePersonName扩展
extension BarcodePersonName: DictionaryConvertible {
    func toDictionary() -> [String: Any] {
        var dict: [String: Any] = [:]
        dict["formattedName"] = formattedName
        dict["first"] = first
        dict["last"] = last
        dict["middle"] = middle
        dict["prefix"] = prefix
        dict["pronunciation"] = pronunciation
        dict["suffix"] = suffix
        return dict
    }
}

// MARK: - MLKBarcodePhone扩展
extension BarcodePhone: DictionaryConvertible {
    func toDictionary() -> [String: Any] {
        var dict: [String: Any] = [:]
        dict["number"] = number
        dict["type"] = type
        dict["typeName"] = BarcodeConverter.phoneTypeName(for: type)
        return dict
    }
}

// MARK: - MLKBarcodeSMS扩展
extension BarcodeSMS: DictionaryConvertible {
    func toDictionary() -> [String: Any] {
        var dict: [String: Any] = [:]
        dict["message"] = message
        dict["phoneNumber"] = phoneNumber
        return dict
    }
}

// MARK: - MLKBarcodeURLBookmark扩展
extension BarcodeURLBookmark: DictionaryConvertible {
    func toDictionary() -> [String: Any] {
        var dict: [String: Any] = [:]
        dict["title"] = title
        dict["url"] = url
        return dict
    }
}

// MARK: - MLKBarcodeWiFi扩展
extension BarcodeWifi: DictionaryConvertible {
    func toDictionary() -> [String: Any] {
        var dict: [String: Any] = [:]
        dict["ssid"] = ssid
        dict["password"] = password
        dict["type"] = type
        dict["typeName"] = BarcodeConverter.wifiEncryptionTypeName(for: type)
        return dict
    }
}

// MARK: - MLKBarcodeContactInfo扩展
extension BarcodeContactInfo: DictionaryConvertible {
    func toDictionary() -> [String: Any] {
        var dict: [String: Any] = [:]
        dict["addresses"] = addresses?.map { $0.toDictionary() }
        dict["emails"] = emails?.map { $0.toDictionary() }
        dict["name"] = name?.toDictionary()
        dict["phones"] = phones?.map { $0.toDictionary() }
        dict["urls"] = urls
        dict["jobTitle"] = jobTitle
        dict["organization"] = organization
        return dict
    }
}

// MARK: - MLKBarcode扩展
extension Barcode: DictionaryConvertible {
    func toDictionary() -> [String: Any] {
        var dict: [String: Any] = [:]
        
        dict["frame"] = [
            "x": frame.origin.x,
            "y": frame.origin.y,
            "width": frame.size.width,
            "height": frame.size.height
        ]
        dict["rawValue"] = rawValue
        dict["rawData"] = rawData?.base64EncodedString()
        dict["displayValue"] = displayValue
        dict["format"] = format
        dict["formatName"] = BarcodeConverter.formatName(for: format)
        dict["valueType"] = valueType
        
        if let cornerPoints = cornerPoints {
            dict["cornerPoints"] = cornerPoints.map { value in
                let point = value.cgPointValue
                return ["x": point.x, "y": point.y]
            }
        }
        
        // 根据valueType设置对应的属性
        switch valueType {
        case BarcodeValueType.email:
            dict["email"] = email?.toDictionary()
        case BarcodeValueType.phone:
            dict["phone"] = phone?.toDictionary()
        case BarcodeValueType.SMS:
            dict["sms"] = sms?.toDictionary()
        case BarcodeValueType.URL:
            dict["url"] = url?.toDictionary()
        case BarcodeValueType.wiFi:
            dict["wifi"] = wifi?.toDictionary()
        case BarcodeValueType.geographicCoordinates:
            dict["geoPoint"] = geoPoint?.toDictionary()
        case BarcodeValueType.contactInfo:
            dict["contactInfo"] = contactInfo?.toDictionary()
        case BarcodeValueType.calendarEvent:
            dict["calendarEvent"] = calendarEvent?.toDictionary()
        case BarcodeValueType.driversLicense:
            dict["driverLicense"] = driverLicense?.toDictionary()
        default:
            break
        }
        
        return dict
    }
}

// MARK: - 转换工具类
class BarcodeConverter {
    
    // MARK: - 批量转换方法
    static func convertBarcodesToDictionaries(_ barcodes: [Barcode]) -> [[String: Any]] {
        return barcodes.map { $0.toDictionary() }
    }
    
    // MARK: - JSON转换方法
    static func convertBarcodesToJSON(_ barcodes: [Barcode]) -> String? {
        let dictionaries = convertBarcodesToDictionaries(barcodes)
        do {
            let jsonData = try JSONSerialization.data(withJSONObject: dictionaries, options: [.prettyPrinted])
            return String(data: jsonData, encoding: .utf8)
        } catch {
            print("JSON转换失败: \(error)")
            return nil
        }
    }
    
    // MARK: - 类型名称转换方法
    
    static func addressTypeName(for type: BarcodeAddressType) -> String {
        switch type {
        case BarcodeAddressType.unknown: return "Unknown"
        case BarcodeAddressType.work: return "Work"
        case BarcodeAddressType.home: return "Home"
        default: return "Unknown"
        }
    }
    
    static func emailTypeName(for type: BarcodeEmailType) -> String {
        switch type {
        case BarcodeEmailType.unknown: return "Unknown"
        case BarcodeEmailType.work: return "Work"
        case BarcodeEmailType.home: return "Home"
        default: return "Unknown"
        }
    }
    
    static func phoneTypeName(for type: BarcodePhoneType) -> String {
        switch type {
        case BarcodePhoneType.unknown: return "Unknown"
        case BarcodePhoneType.work: return "Work"
        case BarcodePhoneType.home: return "Home"
        case BarcodePhoneType.fax: return "Fax"
        case BarcodePhoneType.mobile: return "Mobile"
        default: return "Unknown"
        }
    }
    
    static func wifiEncryptionTypeName(for type: BarcodeWiFiEncryptionType) -> String {
        switch type {
        case BarcodeWiFiEncryptionType.unknown: return "Unknown"
        case BarcodeWiFiEncryptionType.open: return "Open"
        case BarcodeWiFiEncryptionType.WPA: return "WPA"
        case BarcodeWiFiEncryptionType.WEP: return "WEP"
        default: return "Unknown"
        }
    }
    
    static func formatName(for format: BarcodeFormat) -> String {
        return "Format_\(format)"
    }
}
