# FFmpegKit

Android application integrating FFmpeg 8.0.1 with libass subtitle support, built with Kotlin + Jetpack Compose + C++ JNI.

## Features

- FFmpeg 8.0.1 (libavcodec, libavformat, libavfilter, libavutil, libswresample, libswscale)
- libass / libfreetype / libfribidi for ASS/SSA subtitle rendering
- Android MediaCodec hardware acceleration
- Support arm64-v8a and armeabi-v7a

## Prerequisites

- Android Studio (Arctic Fox or later)
- Android SDK 36
- NDK with CMake 3.22.1
- JDK 17

## Setup

### 1. Clone

```bash
git clone https://github.com/zhouquancheng-dev/FFmpegKit.git
```

### 2. Build FFmpeg .so files

The prebuilt .so files are not included in the repository. You need to build them on a Linux machine:

```bash
# Install dependencies
sudo apt install -y make pkg-config yasm nasm gcc g++ meson ninja-build

# Run the build script (requires Android NDK r29 at /opt/android-ndk-r29)
cd /path/to/FFmpegSourceCode
bash build_android.sh
```

This will compile: freetype → fribidi → harfbuzz → libass → FFmpeg

### 3. Copy .so files

Copy the built .so files into the project:

```bash
# arm64-v8a
cp android_build/arm64-v8a/lib/*.so \
   FFmpegKit/app/src/main/jniLibs/arm64-v8a/

# armeabi-v7a
cp android_build/armeabi-v7a/lib/*.so \
   FFmpegKit/app/src/main/jniLibs/armeabi-v7a/
```

### 4. Build & Run

Open the project in Android Studio and run, or:

```bash
./gradlew assembleDebug
```

## Project Structure

```
app/src/main/
├── java/dev/zqc/ffmpegkit/
│   ├── MainActivity.kt          # Compose UI + JNI declarations
│   ├── MyApp.kt                 # Application class
│   └── theme/                   # Material3 theme
├── cpp/
│   ├── CMakeLists.txt            # CMake config, imports FFmpeg libs
│   ├── native-lib.cpp            # JNI implementation
│   └── libav*/                   # FFmpeg headers
├── jniLibs/{arm64-v8a,armeabi-v7a}/  # FFmpeg .so (gitignored)
└── res/                          # Android resources
```

## Tech Stack

| Component | Version |
|-----------|---------|
| Kotlin | 2.3.10 |
| Jetpack Compose | BOM 2026.03.00 |
| Material Design | 3 |
| FFmpeg | 8.0.1 |
| NDK | r29 |
| Min SDK | 26 (Android 8.0) |
| Target SDK | 36 |

## Branches

| Branch | Purpose |
|--------|---------|
| `main` | Stable, production-ready |
| `develop` | Integration branch |
| `feature/video-processing` | Video encoding/decoding |
| `feature/subtitle-rendering` | ASS/SSA subtitle rendering |

## License

MIT
