package expo.modules.barcoderecognizer

import expo.modules.kotlin.records.Field
import expo.modules.kotlin.records.Record
import expo.modules.kotlin.types.Enumerable
import android.os.Bundle
import com.google.mlkit.vision.barcode.common.Barcode


enum class ExpoBarcodeFormat(val value: Int) : Enumerable {
    unknown(0),
    all(0xFFFF),
    code128(0x0001),
    code39(0x0002),
    code93(0x0004),
    codabar(0x0008),
    dataMatrix(0x0010),
    ean13(0x0020),
    ean8(0x0040),
    itf(0x0080),
    qrCode(0x0100),
    upca(0x0200),
    upce(0x0400),
    pdf417(0x0800),
    aztec(0x1000);

    companion object {
        fun fromInt(value: Int): ExpoBarcodeFormat {
            return entries.firstOrNull { it.value == value } ?: unknown
        }
    }
}


/// 调用识别方法所传参数
class ExpoBarcodeRecognizeOptions : Record {
    @Field
    val base64OrImageUri: String = ""

    @Field
    val formats: Array<ExpoBarcodeFormat>? = null

    @Field
    val rotationDegrees: Int = 0
}


/**
 * Barcode 对象转 Bundle 的完整扩展函数
 */
fun Barcode.toBundle(): Bundle {
    val bundle = Bundle()

    // 基础属性
    bundle.putString("rawValue", rawValue)
    bundle.putString("displayValue", displayValue)
    bundle.putInt("format", format)
    bundle.putInt("valueType", valueType)

    // 原始字节数据
    rawBytes?.let { bytes ->
        bundle.putByteArray("rawBytes", bytes)
    }

    // 处理边界框
    boundingBox?.let { rect ->
        val boundingBoxBundle = Bundle().apply {
            putInt("x", rect.left)
            putInt("y", rect.top)
            putInt("right", rect.right)
            putInt("bottom", rect.bottom)
            putInt("width", rect.width())
            putInt("height", rect.height())
            putInt("centerX", rect.centerX())
            putInt("centerY", rect.centerY())
        }
        bundle.putBundle("frame", boundingBoxBundle)
    }

    // 处理角点
    cornerPoints?.let { points ->
        val cornerPointsArray = points.map { point ->
            Bundle().apply {
                putInt("x", point.x)
                putInt("y", point.y)
            }
        }.toTypedArray()
        bundle.putParcelableArray("cornerPoints", cornerPointsArray)
    }

    // 根据不同类型处理特定数据
    when (valueType) {
        Barcode.TYPE_EMAIL -> {
            email?.let { bundle.putBundle("email", it.toBundle()) }
        }
        Barcode.TYPE_PHONE -> {
            phone?.let { bundle.putBundle("phone", it.toBundle()) }
        }
        Barcode.TYPE_URL -> {
            url?.let { bundle.putBundle("url", it.toBundle()) }
        }
        Barcode.TYPE_WIFI -> {
            wifi?.let { bundle.putBundle("wifi", it.toBundle()) }
        }
        Barcode.TYPE_GEO -> {
            geoPoint?.let { bundle.putBundle("geoPoint", it.toBundle()) }
        }
        Barcode.TYPE_CONTACT_INFO -> {
            contactInfo?.let { bundle.putBundle("contactInfo", it.toBundle()) }
        }
        Barcode.TYPE_CALENDAR_EVENT -> {
            calendarEvent?.let { bundle.putBundle("calendarEvent", it.toBundle()) }
        }
        Barcode.TYPE_DRIVER_LICENSE -> {
            driverLicense?.let { bundle.putBundle("driverLicense", it.toBundle()) }
        }
        Barcode.TYPE_SMS -> {
            sms?.let { bundle.putBundle("sms", it.toBundle()) }
        }
    }

    return bundle
}

/**
 * Email 扩展函数
 */
fun Barcode.Email.toBundle(): Bundle = Bundle().apply {
    putString("address", address)
    putString("body", body)
    putString("subject", subject)
    putInt("type", type)

    // 添加类型名称方便JS端使用
    val typeName = when (type) {
        Barcode.Email.TYPE_WORK -> "work"
        Barcode.Email.TYPE_HOME -> "home"
        else -> "unknown"
    }
    putString("typeName", typeName)
}

/**
 * Phone 扩展函数
 */
fun Barcode.Phone.toBundle(): Bundle = Bundle().apply {
    putString("number", number)
    putInt("type", type)

    // 添加类型名称
    val typeName = when (type) {
        Barcode.Phone.TYPE_WORK -> "work"
        Barcode.Phone.TYPE_HOME -> "home"
        Barcode.Phone.TYPE_FAX -> "fax"
        Barcode.Phone.TYPE_MOBILE -> "mobile"
        else -> "unknown"
    }
    putString("typeName", typeName)
}

/**
 * UrlBookmark 扩展函数
 */
fun Barcode.UrlBookmark.toBundle(): Bundle = Bundle().apply {
    putString("title", title)
    putString("url", url)
}

/**
 * WiFi 扩展函数
 */
fun Barcode.WiFi.toBundle(): Bundle = Bundle().apply {
    putString("ssid", ssid)
    putString("password", password)
    putInt("encryptionType", encryptionType)

    // 添加加密类型名称
    val encryptionTypeName = when (encryptionType) {
        Barcode.WiFi.TYPE_OPEN -> "open"
        Barcode.WiFi.TYPE_WPA -> "wpa"
        Barcode.WiFi.TYPE_WEP -> "wep"
        else -> "unknown"
    }
    putString("encryptionTypeName", encryptionTypeName)
}

/**
 * GeoPoint 扩展函数
 */
fun Barcode.GeoPoint.toBundle(): Bundle = Bundle().apply {
    putDouble("lat", lat)
    putDouble("lng", lng)
}

/**
 * ContactInfo 扩展函数
 */
fun Barcode.ContactInfo.toBundle(): Bundle = Bundle().apply {
    putString("title", title)
    putString("organization", organization)

    // 处理姓名
    name?.let {
        putBundle("name", it.toBundle())
    }

    // 处理电话数组
    if (phones.isNotEmpty()) {
        val phoneBundles = phones.map { it.toBundle() }.toTypedArray()
        putParcelableArray("phones", phoneBundles)
    }

    // 处理邮箱数组
    if (emails.isNotEmpty()) {
        val emailBundles = emails.map { it.toBundle() }.toTypedArray()
        putParcelableArray("emails", emailBundles)
    }

    // 处理URL数组
    if (urls.isNotEmpty()) {
        putStringArray("urls", urls.toTypedArray())
    }

    // 处理地址数组
    if (addresses.isNotEmpty()) {
        val addressBundles = addresses.map { it.toBundle() }.toTypedArray()
        putParcelableArray("addresses", addressBundles)
    }
}

/**
 * PersonName 扩展函数
 */
fun Barcode.PersonName.toBundle(): Bundle = Bundle().apply {
    putString("formattedName", formattedName)
    putString("first", first)
    putString("last", last)
    putString("middle", middle)
    putString("prefix", prefix)
    putString("suffix", suffix)
    putString("pronunciation", pronunciation)
}

/**
 * Address 扩展函数
 */
fun Barcode.Address.toBundle(): Bundle = Bundle().apply {
    putInt("type", type)
    putStringArray("addressLines", addressLines)

    // 添加类型名称
    val typeName = when (type) {
        Barcode.Address.TYPE_WORK -> "work"
        Barcode.Address.TYPE_HOME -> "home"
        else -> "unknown"
    }
    putString("typeName", typeName)
}

/**
 * CalendarEvent 扩展函数
 */
fun Barcode.CalendarEvent.toBundle(): Bundle = Bundle().apply {
    putString("summary", summary)
    putString("description", description)
    putString("location", location)
    putString("organizer", organizer)
    putString("status", status)

    // 处理开始时间
    start?.let {
        putBundle("start", it.toBundle())
    }

    // 处理结束时间
    end?.let {
        putBundle("end", it.toBundle())
    }
}

/**
 * CalendarDateTime 扩展函数
 */
fun Barcode.CalendarDateTime.toBundle(): Bundle = Bundle().apply {
    putInt("year", year)
    putInt("month", month)
    putInt("day", day)
    putInt("hours", hours)
    putInt("minutes", minutes)
    putInt("seconds", seconds)
    putBoolean("isUtc", isUtc)
    putString("rawValue", rawValue)
}

/**
 * DriverLicense 扩展函数
 */
fun Barcode.DriverLicense.toBundle(): Bundle = Bundle().apply {
    putString("documentType", documentType)
    putString("firstName", firstName)
    putString("middleName", middleName)
    putString("lastName", lastName)
    putString("gender", gender)
    putString("addressStreet", addressStreet)
    putString("addressCity", addressCity)
    putString("addressState", addressState)
    putString("addressZip", addressZip)
    putString("licenseNumber", licenseNumber)
    putString("issueDate", issueDate)
    putString("expiryDate", expiryDate)
    putString("birthDate", birthDate)
    putString("issuingCountry", issuingCountry)
}

/**
 * Sms 扩展函数
 */
fun Barcode.Sms.toBundle(): Bundle = Bundle().apply {
    putString("message", message)
    putString("phoneNumber", phoneNumber)
}

/**
 * 批量转换多个 Barcode 的工具函数
 */
fun List<Barcode>.toBundleArray(): Array<Bundle> {
    return this.map { it.toBundle() }.toTypedArray()
}

/**
 * 创建包含多个 Barcode 结果的 Bundle
 */
fun List<Barcode>.toResultBundle(): Bundle = Bundle().apply {
    putParcelableArray("barcodes", this@toResultBundle.toBundleArray())
    putInt("count", this@toResultBundle.size)
}
