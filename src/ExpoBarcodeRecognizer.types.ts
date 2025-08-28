
export type ExpoBarcodeRecognizerModuleEvents = {

};


export enum BarcodeFormat {
  unknown = 0,
  all = 0xFFFF,
  code128 = 0x0001,
  code39 = 0x0002,
  code93 = 0x0004,
  codabar = 0x0008,
  dataMatrix = 0x0010,
  ean13 = 0x0020,
  ean8 = 0x0040,
  itf = 0x0080,
  qrCode = 0x0100,
  upca = 0x0200,
  upce = 0x0400,
  pdf417 = 0x0800,
  aztec = 0x1000,
}

/**
 * 图片方向
 */
export enum InputImageOrientation {
  up = 0,
  down = 1,
  left = 2,
  right = 3,
  upMirrored = 4,
  downMirrored = 5,
  leftMirrored = 6,
  rightMirrored = 7,
}

/**
 * 图片识别所需参数
 */
export type RecognizeOptions = {
  base64OrImageUri: string;
  /// 图片朝向，iOS Only
  orientation?: InputImageOrientation;
  formats?: BarcodeFormat[];
  /// 旋转角度，安卓Only
  rotationDegrees?: number;
};


export interface ExpoBarcodeFrame {
    x: number;
    y: number;
    width: number;
    height: number;
}

export interface ExpoBarcodeCornerPoint {
    x: number;
    y: number;
}

export enum ExpoBarcodeValueType {
    unknown = 0,
    contactInfo = 1,
    email = 2,
    isbn = 3,
    phone = 4,
    product = 5,
    sms = 6,
    text = 7,
    url = 8,
    wifi = 9,
    geographicCoordinates = 10,
    calendarEvent = 11,
    driversLicense = 12
}

export enum ExpoBarcodeEmailType {
    unknown = 0,
    work = 1,
    home = 2
}

export interface ExpoBarcodeEmail {
    address?: string;
    body?: string;
    subject?: string;
    type: ExpoBarcodeEmailType;
}

export enum ExpoBarcodePhoneType {
    unknown = 0,
    work = 1,
    home = 2,
    fax = 3,
    mobile = 4
}

export interface ExpoBarcodePhone {
    number?: string;
    type: ExpoBarcodePhoneType;
}

export interface ExpoBarcodeSMS {
    message?: string;
    phoneNumber?: string;
}

export interface ExpoBarcodeBookmarkURL {
    title?: string;
    url?: string;
}

export enum ExpoBarcodeWiFiEncryptionType {
    unknown = 0,
    open = 1,
    wpa = 2,
    wep = 3
}

export interface ExpoBarcodeWiFi {
    ssid?: string;
    password?: string;
    type: ExpoBarcodeWiFiEncryptionType;
}

export interface ExpoBarcodeGeoPoint {
    latitude: number;
    longitude: number;
}

export enum ExpoBarcodeAddressType {
    unknown = 0,
    work = 1,
    home = 2
}

export interface ExpoBarcodeAddress {
    addressLines?: string[];
    type: ExpoBarcodeAddressType;
}

export interface ExpoBarcodePersonName {  
    formattedName?: string;
    first?: string;
    last?: string;
    middle?: string;
    prefix?: string;
    pronunciation?: string;
    suffix?: string;
}

export interface ExpoBarcodeContactInfo {
    addresses?: ExpoBarcodeAddress[];
    emails?: ExpoBarcodeEmail[];
    name?: ExpoBarcodePersonName;
    phones?: ExpoBarcodePhone[];
    urls?: string[];
    jobTitle?: string;
    organization?: string;
}

export interface ExpoBarcodeCalendarEvent {
    eventDescription?: string;
    location?: string;
    organizer?: string;
    status?: string;
    summary?: string;
    start?: bigint;
    end?: bigint;
}

export interface ExpoBarcodeDriverLicense {
    firstName?: string;
    middleName?: string;
    lastName?: string;
    gender?: string;
    addressCity?: string;
    addressState?: string;
    addressStreet?: string;
    addressZip?: string;
    birthDate?: string;
    documentType?: string;
    licenseNumber?: string;
    expiryDate?: string;
    issuingDate?: string;
    issuingCountry?: string;
}

export interface ExpoBarcodeRecognizeResult {
    frame?: ExpoBarcodeFrame;
    rawValue?: string;
    format: BarcodeFormat;
    cornerPoints?: ExpoBarcodeCornerPoint[];
    valueType: ExpoBarcodeValueType;
    email?: ExpoBarcodeEmail;
    phone?: ExpoBarcodePhone;
    sms?: ExpoBarcodeSMS;
    url?: ExpoBarcodeBookmarkURL;
    wifi?: ExpoBarcodeWiFi;
    geoPoint?: ExpoBarcodeGeoPoint;
    contactInfo?: ExpoBarcodeContactInfo;
    calendarEvent?: ExpoBarcodeCalendarEvent;
    driverLicense?: ExpoBarcodeDriverLicense;
}