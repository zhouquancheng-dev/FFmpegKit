#include <jni.h>
#include <string>
#include <android/log.h>

#define TAG "FFmpegKit"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, TAG, __VA_ARGS__)

extern "C" {
#include "libavcodec/avcodec.h"
#include "libavformat/avformat.h"
#include "libavfilter/avfilter.h"
#include "libavutil/avutil.h"
#include "libswresample/swresample.h"
#include "libswscale/swscale.h"
}

extern "C" JNIEXPORT void JNICALL
Java_dev_zqc_ffmpegkit_MainActivity_printFFmpegInfo(
        JNIEnv* env,
        jobject /* this */) {

    LOGI("========== FFmpeg Integration Check ==========");

    // FFmpeg version
    LOGI("FFmpeg version: %s", av_version_info());

    // Individual library versions
    LOGI("libavcodec     %d.%d.%d",
         AV_VERSION_MAJOR(avcodec_version()),
         AV_VERSION_MINOR(avcodec_version()),
         AV_VERSION_MICRO(avcodec_version()));

    LOGI("libavformat    %d.%d.%d",
         AV_VERSION_MAJOR(avformat_version()),
         AV_VERSION_MINOR(avformat_version()),
         AV_VERSION_MICRO(avformat_version()));

    LOGI("libavfilter    %d.%d.%d",
         AV_VERSION_MAJOR(avfilter_version()),
         AV_VERSION_MINOR(avfilter_version()),
         AV_VERSION_MICRO(avfilter_version()));

    LOGI("libavutil      %d.%d.%d",
         AV_VERSION_MAJOR(avutil_version()),
         AV_VERSION_MINOR(avutil_version()),
         AV_VERSION_MICRO(avutil_version()));

    LOGI("libswresample  %d.%d.%d",
         AV_VERSION_MAJOR(swresample_version()),
         AV_VERSION_MINOR(swresample_version()),
         AV_VERSION_MICRO(swresample_version()));

    LOGI("libswscale     %d.%d.%d",
         AV_VERSION_MAJOR(swscale_version()),
         AV_VERSION_MINOR(swscale_version()),
         AV_VERSION_MICRO(swscale_version()));

    // Build configuration
    const char* config = avcodec_configuration();
    LOGI("Build configuration: %s", config);

    // libass check
    std::string configStr(config);
    bool hasLibass = configStr.find("--enable-libass") != std::string::npos;
    LOGI("libass: %s", hasLibass ? "AVAILABLE" : "NOT AVAILABLE");

    LOGI("========== FFmpeg Check Complete ==========");
}

extern "C" JNIEXPORT jstring JNICALL
Java_dev_zqc_ffmpegkit_MainActivity_getFFmpegVersion(
        JNIEnv* env,
        jobject /* this */) {
    std::string version = av_version_info();
    return env->NewStringUTF(version.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_dev_zqc_ffmpegkit_MainActivity_getFFmpegBuildConfiguration(
        JNIEnv* env,
        jobject /* this */) {
    const char* config = avcodec_configuration();
    return env->NewStringUTF(config);
}

extern "C" JNIEXPORT jstring JNICALL
Java_dev_zqc_ffmpegkit_MainActivity_checkLibassAvailability(
        JNIEnv* env,
        jobject /* this */) {
    const char* config = avcodec_configuration();
    std::string configStr(config);

    // Check if libass was enabled in FFmpeg build configuration
    bool hasLibass = configStr.find("--enable-libass") != std::string::npos;

    // Also check if ass subtitle filter is available in avfilter
    std::string result;
    if (hasLibass) {
        result = "libass: available (found --enable-libass in build configuration)";
    } else {
        result = "libass: NOT available (--enable-libass not found in build configuration)";
    }

    return env->NewStringUTF(result.c_str());
}
