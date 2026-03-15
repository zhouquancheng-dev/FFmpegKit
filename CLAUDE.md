# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

FFmpegKit is an open-source Android library wrapping FFmpeg 8.0.1 with Kotlin API and C++ JNI bridge. It provides video/audio processing and ASS/SSA subtitle rendering via libass.

## Build Commands

```bash
# Build (use gradlew.bat on Windows)
./gradlew assembleDebug           # Debug APK
./gradlew assembleRelease         # Release APK (signed with ffmpegkit.jks)

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

- **MainActivity** (`app/src/main/java/dev/zqc/ffmpegkit/MainActivity.kt`) — Demo UI, loads native library via `System.loadLibrary("ffmpegkit")`, declares JNI methods (`printFFmpegInfo`, `getFFmpegVersion`, `getFFmpegBuildConfiguration`, `checkLibassAvailability`)
- **MyApp** (`app/src/main/java/dev/zqc/ffmpegkit/MyApp.kt`) — Application class with Coil3 image loader and lifecycle tracking
- **Native C++** (`app/src/main/cpp/native-lib.cpp`) — JNI implementation, built as shared library `libffmpegkit.so` via CMake 3.22.1 (C++17)
- **Theme** (`app/src/main/java/dev/zqc/ffmpegkit/theme/`) — Material3 theming with dynamic color support (Android 12+)

### Native / FFmpeg Structure

```
app/src/main/
├── jniLibs/{arm64-v8a,armeabi-v7a}/   ← FFmpeg .so (runtime + CMake linking, gitignored)
└── cpp/
    ├── CMakeLists.txt                  ← CMake build config, imports FFmpeg as SHARED IMPORTED
    ├── native-lib.cpp                  ← JNI methods (can split into multiple .cpp files)
    ├── libavcodec/                     ← FFmpeg headers (compile-time only, not in APK)
    ├── libavformat/
    ├── libavfilter/
    ├── libavutil/
    ├── libswresample/
    └── libswscale/
```

- **jniLibs/** — sole copy of .so files, used both for APK packaging (Gradle) and CMake linking
- JNI function naming convention: `Java_dev_zqc_ffmpegkit_ClassName_methodName`
- FFmpeg headers are C, must be wrapped in `extern "C" {}` when included from C++

### FFmpeg Integration

- **FFmpeg 8.0.1** cross-compiled for Android (NDK r29, API 24)
- **ABIs**: arm64-v8a, armeabi-v7a (configured via `ndk.abiFilters` in build.gradle.kts)
- **Libraries**: libavcodec, libavformat, libavfilter, libavutil, libswresample, libswscale
- **Enabled features**: libass, libfreetype, libfribidi, mediacodec, jni
- **Build chain**: freetype → fribidi → harfbuzz → libass → FFmpeg (see `build_android.sh`)
- libass and its dependencies are statically linked into the FFmpeg .so files
- Prebuilt .so available at [GitHub Releases](https://github.com/zhouquancheng-dev/FFmpegKit/releases)

### Build Configuration

- **Gradle**: 8.14.4 with Kotlin DSL, AGP 8.13.2
- **Kotlin**: 2.3.10, JVM target 17
- **SDK**: minSdk 26, targetSdk/compileSdk 36
- **Version catalog**: `gradle/libs.versions.toml` (central dependency management)
- **Native build**: CMake 3.22.1, C++17
- **Signing**: `ffmpegkit.jks` at project root (gitignored)

### Key Dependencies

- Jetpack Compose (BOM 2026.03.00) + Material3
- AndroidX Navigation3 (1.1.0-beta01)
- Coil3 (3.4.0) for image/video loading
- Kotlinx Coroutines (1.10.2) + Serialization (1.10.0)

## Git Workflow

- **Repository**: https://github.com/zhouquancheng-dev/FFmpegKit.git
- **Branch strategy** (Git Flow):
  - `main` — stable releases, tagged with version (e.g. `v1.0.0`)
  - `develop` — integration branch, all feature PRs merge here
  - `feature/*` — new features, branch from `develop`
  - `bugfix/*` — bug fixes, branch from `develop`
  - `hotfix/*` — urgent fixes, branch from `main`, merge to both `main` and `develop`
  - `release/*` — release preparation, branch from `develop`
- **Commit convention**: `type: description`
  - `feat:` new feature, `fix:` bug fix, `refactor:` code restructure
  - `docs:` documentation, `test:` tests, `chore:` build/tooling
- **PR flow**: feature branch → PR to `develop` → code review → merge → release branch → `main` + tag
- **Gitignored**: `jniLibs/`, `*.jks`, `.claude/`

## Module Structure

Single-module project (`:app`). All source code lives under `app/src/main/`.
