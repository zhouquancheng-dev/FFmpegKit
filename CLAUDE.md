# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

FFmpegKit is an Android application (package: `dev.zqc.ffmpegkit`) built with Kotlin, Jetpack Compose, and a C++ JNI native layer. It integrates FFmpeg 8.0.1 (with libass subtitle support) on Android.

## Build Commands

```bash
# Build (use gradlew.bat on Windows)
./gradlew assembleDebug           # Debug APK
./gradlew assembleRelease         # Release APK (signed with ffmpegkit.jks)
./gradlew assembleOther           # Build with "Other" product flavor

# Tests
./gradlew test                    # Unit tests (JUnit4)
./gradlew connectedAndroidTest    # Instrumented tests (requires device/emulator)

# Clean
./gradlew clean
```

## Architecture

### Layers

```
Compose UI (MainActivity) → JNI Bridge → C++ Native (native-lib.cpp) → FFmpeg .so
         ↓
    MyApp (Application class: Coil image loader, lifecycle monitoring)
```

### Key Components

- **MainActivity** (`app/src/main/java/dev/zqc/ffmpegkit/MainActivity.kt`) — Compose entry point, loads native library via `System.loadLibrary("ffmpegkit")`, declares JNI methods (`printFFmpegInfo`, `getFFmpegVersion`, `getFFmpegBuildConfiguration`, `checkLibassAvailability`)
- **MyApp** (`app/src/main/java/dev/zqc/ffmpegkit/MyApp.kt`) — Application class with Coil3 image loader (GIF, SVG, video frame decoders) and foreground/background lifecycle tracking
- **Native C++** (`app/src/main/cpp/native-lib.cpp`) — JNI implementation, built as shared library `libffmpegkit.so` via CMake 3.22.1 (C++17)
- **Theme** (`app/src/main/java/dev/zqc/ffmpegkit/theme/`) — Material3 theming with dynamic color support (Android 12+), custom `LocalBackgroundTheme` and `LocalTintTheme` composition locals

### Native / FFmpeg Structure

```
app/src/main/
├── jniLibs/{arm64-v8a,armeabi-v7a}/   ← FFmpeg .so (runtime + CMake linking)
└── cpp/
    ├── CMakeLists.txt                  ← CMake build config, imports FFmpeg as SHARED IMPORTED
    ├── native-lib.cpp                  ← JNI methods (can split into multiple .cpp files)
    ├── libavcodec/                     ← FFmpeg headers (compile-time only)
    ├── libavformat/
    ├── libavfilter/
    ├── libavutil/
    ├── libswresample/
    └── libswscale/
```

- **jniLibs/** — sole copy of .so files, used both for APK packaging (Gradle) and CMake linking
- **cpp/ headers** — FFmpeg C headers, only used at compile time, not included in APK
- JNI function naming convention: `Java_dev_zqc_ffmpegkit_ClassName_methodName`
- FFmpeg headers are C, must be wrapped in `extern "C" {}` when included from C++

### FFmpeg Integration

- **FFmpeg 8.0.1** cross-compiled for Android (NDK r29, API 24)
- **ABIs**: arm64-v8a, armeabi-v7a (configured via `ndk.abiFilters` in build.gradle.kts)
- **Libraries**: libavcodec, libavformat, libavfilter, libavutil, libswresample, libswscale
- **Enabled features**: libass (ASS/SSA subtitles), libfreetype, libfribidi, mediacodec, jni
- **Build script**: `D:\FFmpegSourceCode\build_android.sh` — compiles FFmpeg + dependencies (freetype → fribidi → harfbuzz → libass → FFmpeg)
- libass and its dependencies are statically linked into the FFmpeg .so files (no separate libass.so)

### Build Configuration

- **Gradle**: 8.14.4 with Kotlin DSL, AGP 8.13.2
- **Kotlin**: 2.3.10, JVM target 17
- **SDK**: minSdk 26, targetSdk/compileSdk 36
- **Version catalog**: `gradle/libs.versions.toml` (central dependency management)
- **Native build**: CMake 3.22.1, C++17
- **Product flavor**: "Other" (sets app name to FFmpegKit)
- **Signing**: `ffmpegkit.jks` at project root

### Key Dependencies

- Jetpack Compose (BOM 2026.03.00) + Material3
- AndroidX Navigation3 (1.1.0-beta01)
- Coil3 (3.4.0) for image/video loading
- Kotlinx Coroutines (1.10.2) + Serialization (1.10.0)

## Single Module

This is a single-module project (`:app` only). All source code lives under `app/src/main/`.
