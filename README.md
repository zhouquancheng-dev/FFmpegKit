# FFmpegKit

[![Release](https://img.shields.io/github/v/release/zhouquancheng-dev/FFmpegKit)](https://github.com/zhouquancheng-dev/FFmpegKit/releases)
[![JitPack](https://jitpack.io/v/zhouquancheng-dev/FFmpegKit.svg)](https://jitpack.io/#zhouquancheng-dev/FFmpegKit)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

An Android library wrapping FFmpeg 8.0.1 with Kotlin API and C++ JNI bridge. Provides video/audio processing, format conversion, and ASS/SSA subtitle rendering via libass.

## Installation

### Step 1: Add JitPack repository

In your root `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

### Step 2: Add dependency

In your module `build.gradle.kts`:

```kotlin
dependencies {
    implementation("com.github.zhouquancheng-dev:FFmpegKit:v1.0.0")
}
```

### Step 3: Download prebuilt .so files

The FFmpeg .so files are not bundled in the library artifact. Download from [GitHub Releases](https://github.com/zhouquancheng-dev/FFmpegKit/releases) and place them in your project:

```bash
tar xzf ffmpeg-8.0.1-android-libs.tar.gz

# Copy to YOUR project's jniLibs (not FFmpegKit's)
mkdir -p app/src/main/jniLibs/{arm64-v8a,armeabi-v7a}
cp ffmpeg-android-libs/arm64-v8a/*.so   app/src/main/jniLibs/arm64-v8a/
cp ffmpeg-android-libs/armeabi-v7a/*.so app/src/main/jniLibs/armeabi-v7a/
```

## Usage

```kotlin
import dev.zqc.ffmpegkit.FFmpegKit

// Get FFmpeg version
val version = FFmpegKit.getVersion()  // "8.0.1"

// Get build configuration (./configure flags)
val config = FFmpegKit.getBuildConfiguration()

// Check libass availability
val available = FFmpegKit.isLibassAvailable()  // true/false
val detail = FFmpegKit.checkLibassAvailability()  // detailed string

// Print all info to Logcat (tag: FFmpegKit)
FFmpegKit.printInfo()
```

## Features

- FFmpeg 8.0.1 (libavcodec, libavformat, libavfilter, libavutil, libswresample, libswscale)
- ASS/SSA subtitle rendering (libass + libfreetype + libfribidi)
- Android MediaCodec hardware acceleration
- Kotlin-friendly singleton API (`FFmpegKit` object)
- Support arm64-v8a and armeabi-v7a

## Build from Source

### Build FFmpeg .so files (Linux)

```bash
sudo apt install -y make pkg-config yasm nasm gcc g++ meson ninja-build

# NDK must be at /opt/android-ndk-r29 (or modify build_android.sh)
bash build_android.sh
```

Build chain: freetype → fribidi → harfbuzz → libass → FFmpeg

### Build the project

```bash
./gradlew assembleDebug
```

## Project Structure

```
FFmpegKit/
├── ffmpegkit/                        # Library module (published to JitPack)
│   └── src/main/
│       ├── java/dev/zqc/ffmpegkit/
│       │   └── FFmpegKit.kt          # Public API (singleton object)
│       ├── cpp/
│       │   ├── CMakeLists.txt         # CMake config
│       │   ├── native-lib.cpp         # JNI implementation
│       │   └── libav*/                # FFmpeg headers
│       └── jniLibs/                   # FFmpeg .so files (gitignored)
├── app/                              # Demo application (sample usage)
│   └── src/main/java/.../
│       └── MainActivity.kt           # Demo UI using FFmpegKit API
├── build.gradle.kts
├── settings.gradle.kts
└── jitpack.yml                       # JitPack build config
```

## Requirements

| Requirement | Version |
|-------------|---------|
| Min SDK | 26 (Android 8.0) |
| Target SDK | 36 |
| JDK | 17 |
| NDK | r29 (for building .so only) |
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
3. Commit with conventional messages (`feat:`, `fix:`, `refactor:`, `docs:`)
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
