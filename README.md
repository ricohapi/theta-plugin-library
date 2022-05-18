# RICOH THETA Plug-in Library

Version: 3.0.2

## Contents

* [Terms of Service](#terms)
* [Files included in the archive](#files)
* [Technical requirements for development](#requirements)
* [Contents of the Library](#library)
* [How to Use](#how_to_use)
* [See Also](#see_also)
* [Troubleshooting](#troubleshooting)
* [Trademark Information](#trademark)

<a name="terms"></a>
## Terms of Service

> You agree to comply with all applicable export and import laws and regulations applicable to the jurisdiction in which the Software was obtained and in which it is used. Without limiting the foregoing, in connection with use of the Software, you shall not export or re-export the Software  into any U.S. embargoed countries (currently including, but necessarily limited to, Crimea – Region of Ukraine, Cuba, Iran, North Korea, Sudan and Syria) or  to anyone on the U.S. Treasury Department’s list of Specially Designated Nationals or the U.S. Department of Commerce Denied Person’s List or Entity List.  By using the Software, you represent and warrant that you are not located in any such country or on any such list.  You also agree that you will not use the Software for any purposes prohibited by any applicable laws, including, without limitation, the development, design, manufacture or production of missiles, nuclear, chemical or biological weapons.

By using the RICOH THETA Plug-in Library, you are agreeing to the above and the license terms, [LICENSE.txt](LICENSE.txt).

Copyright &copy; 2018-2022 Ricoh Company, Ltd.

<a name="files"></a>
## Files included in the archive

```
├── README.md:            This file
├── LICENSE.txt:          Files concerning the contract
├── library:              Plug-in library
├── build.gradle:         Android Studio build script
├── gradle:               Android Studio build script
├── gradle.properties:    Android Studio build script
├── gradlew:              Android Studio build script
├── gradlew.bat:          Android Studio build script
├── repository:           maven repository
└── settings.gradle:      Android Studio build script
```

<a name="requirements"></a>
## Technical requirements for development

This Library has been tested under the following conditions.

### Camera

#### Hardware

* RICOH THETA V
* RICOH THETA Z1
* RICOH THETA X

### Development Environment

This Library has been confirmed to operate under the following conditions.

#### Operating System

* Windows 10 Version 21H1
* macOS Monterey Version 12.2

#### Development environment

* Android&trade; Studio 4.2.2
* gradle 3.5.3
* Android&trade; SDK (API Level 29)
* compileSdkVersion 29
* minSdkVersion 25
* targetSdkVersion 29

<a name="library"></a>
## Contents of the Library

* This plug-in library provides following features.
    * Get KeyEvent for THETA V/Z1
    * Terminate Plug-in
    * Control LED/OLED
    * Control speaker
    * Simple method to send THETA specified broadcast intent, please refer to ricoh.api website for detail.
    * Implement Camera API & MediaRecorder API interface for THETA X
        * theta360.hardware.Camera
        * theta360.media.MediaRecorder
        * etc.
    * Also implement Factory class wrapping Camera & MediaRecorder of THETA V/Z1 and THETA X.

<a name="how_to_use"></a>
## How to Use

### Add repository

*  Add the theta-plugin-library repository to your build file

```java
allprojects {
    repositories {
        ...
        maven { url 'https://github.com/ricohapi/theta-plugin-library/raw/master/repository' }
    }
}
```
### Download

```
dependencies {
    implementation 'com.theta360:pluginlibrary:3.0.1'
}
```

<a name="see_also"></a>
## See Also

* The latest information related to plug-in development is published on [api.ricoh](https://api.ricoh/docs/theta-plugin/).
* The latest plug-in library is released on [the GitHub project](https://github.com/ricohapi/theta-plugin-library).
* The example plugin projects are below.
  * [RICOH THETA Plug-in SDK](https://github.com/ricohapi/theta-plugin-sdk) is the template project for the plug-in development.  

<a name="troubleshooting"></a>
## Troubleshooting

If you had a request or found a problem, please create an issue on [the GitHub project](https://github.com/ricohapi/theta-plugin-library/issues).

<a name="trademark"></a>
## Trademark Information

The names of products and services described in this document are trademarks or registered trademarks of each company.

* Android, Nexus, Google Chrome, Google Play, Google Play logo, Google Maps, Google+, Gmail, Google Drive, Google Cloud Print and YouTube are trademarks of Google Inc.
* Apple, Apple logo, Macintosh, Mac, Mac OS, OS X, AppleTalk, Apple TV, App Store, AirPrint, Bonjour, iPhone, iPad, iPad mini, iPad Air, iPod, iPod mini, iPod classic, iPod touch, iWork, Safari, the App Store logo, the AirPrint logo, Retina and iPad Pro are trademarks of Apple Inc., registered in the United States and other countries. The App Store is a service mark of Apple Inc.
* Bluetooth Low Energy and Bluetooth are trademarks or registered trademarks of US Bluetooth SIG, INC., in the United States and other countries.
* Microsoft, Windows, Windows Vista, Windows Live, Windows Media, Windows Server System, Windows Server, Excel, PowerPoint, Photosynth, SQL Server, Internet Explorer, Azure, Active Directory, OneDrive, Outlook, Wingdings, Hyper-V, Visual Basic, Visual C ++, Surface, SharePoint Server, Microsoft Edge, Active Directory, BitLocker, .NET Framework and Skype are registered trademarks or trademarks of Microsoft Corporation in the United States and other countries. The name of Skype, the trademarks and logos associated with it, and the "S" logo are trademarks of Skype or its affiliates.
* Wi-Fi™, Wi-Fi Certified Miracast, Wi-Fi Certified logo, Wi-Fi Direct, Wi-Fi Protected Setup, WPA, WPA 2 and Miracast are trademarks of the Wi-Fi Alliance.
* The official name of Windows is Microsoft Windows Operating System.
* All other trademarks belong to their respective owners.
