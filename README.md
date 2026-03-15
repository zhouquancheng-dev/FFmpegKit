# FFmpegKit

[![Release](https://img.shields.io/github/v/release/zhouquancheng-dev/FFmpegKit)](https://github.com/zhouquancheng-dev/FFmpegKit/releases)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

An Android library wrapping FFmpeg 8.0.1 with Kotlin API and C++ JNI bridge. Provides video/audio processing, format conversion, and ASS/SSA subtitle rendering via libass.

## Features

- FFmpeg 8.0.1 full libraries (avcodec, avformat, avfilter, avutil, swresample, swscale)
- ASS/SSA subtitle rendering (libass + libfreetype + libfribidi)
- Android MediaCodec hardware acceleration
- Kotlin-friendly API
- Support arm64-v8a and armeabi-v7a

## Quick Start

### Option 1: Download Prebuilt Libraries

Download from [GitHub Releases](https://github.com/zhouquancheng-dev/FFmpegKit/releases):

```bash
tar xzf ffmpeg-8.0.1-android-libs.tar.gz
cp ffmpeg-android-libs/arm64-v8a/*.so   app/src/main/jniLibs/arm64-v8a/
cp ffmpeg-android-libs/armeabi-v7a/*.so app/src/main/jniLibs/armeabi-v7a/
```

### Option 2: Build from Source

Requires a Linux machine with Android NDK r29:

```bash
sudo apt install -y make pkg-config yasm nasm gcc g++ meson ninja-build

# NDK must be at /opt/android-ndk-r29 (or modify build_android.sh)
bash build_android.sh
```

Build chain: freetype → fribidi → harfbuzz → libass → FFmpeg

### Build the Project

```bash
./gradlew assembleDebug
```

## Project Structure

```
FFmpegKit/
├── app/                              # Demo application (sample usage)
│   └── src/main/
│       ├── java/dev/zqc/ffmpegkit/
│       │   ├── MainActivity.kt       # Demo UI showing FFmpeg info
│       │   ├── MyApp.kt              # Application class
│       │   └── theme/                # Material3 theme
│       ├── cpp/
│       │   ├── CMakeLists.txt        # CMake build config
│       │   ├── native-lib.cpp        # JNI bridge implementation
│       │   └── libav*/               # FFmpeg headers (compile-time)
│       └── jniLibs/                  # FFmpeg .so files (gitignored)
├── build.gradle.kts                  # Root build config
├── gradle/libs.versions.toml         # Version catalog
└── settings.gradle.kts
```

## Requirements

| Requirement | Version |
|-------------|---------|
| Android Studio | 2024.x+ |
| JDK | 17 |
| Min SDK | 26 (Android 8.0) |
| Target SDK | 36 |
| NDK | r29 (for building .so) |
| CMake | 3.22.1 |

## FFmpeg Libraries

| Library | Version | Description |
|---------|---------|-------------|
| libavcodec | 62.11.100 | Encoding/Decoding |
| libavformat | 62.3.100 | Container formats (mp4, mkv...) |
| libavfilter | 11.4.100 | Filters (subtitle, scale...) |
| libavutil | 60.8.100 | Utility functions |
| libswresample | 6.1.100 | Audio resampling |
| libswscale | 9.1.100 | Image scaling/conversion |

## Contributing

1. Fork the repository
2. Create a feature branch from `develop`
   ```bash
   git checkout develop
   git checkout -b feature/your-feature
   ```
3. Commit with conventional messages
   ```
   feat: add video transcoding API
   fix: resolve memory leak in decoder
   refactor: simplify JNI bridge layer
   ```
4. Push and open a Pull Request to `develop`

### Branch Strategy

| Branch | Purpose |
|--------|---------|
| `main` | Stable releases, tagged with versions |
| `develop` | Integration branch, PRs merge here |
| `feature/*` | New features |
| `bugfix/*` | Bug fixes |
| `hotfix/*` | Urgent fixes from `main` |
| `release/*` | Release preparation |

## License

MIT
