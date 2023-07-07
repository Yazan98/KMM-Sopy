# Sopy

A Kotlin Multiplatform Mobile library to Share Utility Classes between Android, IOS Apps to Lead Mobile Apps by Domain Layer and UseCases

## Library Description

This Library Share Common Parts between Android and IOS Applications and the Idea of this Library is to Control the Applications from UseCases and Domain Layer
No Logic on UI or ViewModel Level, the Whole Logic Should be Inside UseCases and for Api Part Sopy Use Ktor Client as a Http Client

## Supported Platforms
1. Android
2. IOS (Darwin, Legacy Darwin)

## Library Components
1. Local Storage (Shared Prefs, UserDefaults)
2. UseCases and Constraints
3. Api Requests (OneRequest, Crud Operations)
4. Ktor Client Configurations (Android, IOS, IOS Darwin Legacy)

## Library Versions
1. Ktor Version: 2.3.0
2. Java Version: 17
3. Kotlin Coroutines: 1.6.4
4. Android Lifecycle ViewModels: 2.5.1

## IOS Supported Architectures
1. Arm32
2. Arm64
3. x64

## Classes Description
1. SopyOneRequest: Execute Ktor Api Request and Handle Response to UseCase
2. SopyCrudRequest: Execute Ktor Api Request (Crud Operations) and Handle Response to UseCase
3. SopyStorageKeyValue: Local Storage Key Value (SharedPrefs, UserDefaults)
4. SopyUseCase: UseCases Implementation with Ktor Client and State Management (Success, Failed, Loading)
5. SopyDirectUseCase: UseCases Implementation with Ktor Client like SopyUseCase but without Input Type, UseCases to Return Results Only
6. SopyUseCaseAlias: Singleton Instances Helpful in Hilt Scopes in Android Apps Only
7. SopyViewModel: ViewModel Implementation with UseCases and Lifecycle
8. SopyRequestInterceptor: Ktor Request Interceptor to Send Common Headers in All Apis
9. SopyApplicationConfigurations: Application Configurations and General Configs : Registered in App Delegate and Application Scope Only
10. SopyBaseViewModel: Base ViewModels Implementation (IOS Only)


## Ktor IOS Specific Implementation
1. SopyHttpBaseClient: Ktor Darwin Engine : x64, Arm64
2. SopyLegacyHttpBaseClient: Ktor Darwin Legacy: Arm32 Only

## Library Builds Details

> Builds Location in Releases Tab

Library Builds Available to Download on Android, IOS by Build Files

1. Android Builds .aar File (Debug, Release) Available
2. IOS Builds .zip Available for All Supported Architectures (Debug, Release)

## Library Gradle Installation

```
dependencies {

    // Common Main
    implementation "com.yazantarifi:sopy:1.0.0"
    
    // Android
    implementation "com.yazantarifi:sopy-android:1.0.0"
    implementation "com.yazantarifi:sopy-android-debug:1.0.0"
    
    // IOS
    implementation "com.yazantarifi:sopy-iosx64:1.0.0"
    implementation "com.yazantarifi:sopy-iossimulatorarm64:1.0.0"
    implementation "com.yazantarifi:sopy-iosarm64:1.0.0"
    implementation "com.yazantarifi:sopy-iosarm32:1.0.0"
}
```

