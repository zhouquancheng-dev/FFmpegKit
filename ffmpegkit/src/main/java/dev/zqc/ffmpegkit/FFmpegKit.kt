package dev.zqc.ffmpegkit

/**
 * FFmpegKit - Android FFmpeg wrapper library.
 *
 * Usage:
 * ```kotlin
 * val version = FFmpegKit.getVersion()
 * val config = FFmpegKit.getBuildConfiguration()
 * val libassAvailable = FFmpegKit.isLibassAvailable()
 * ```
 */
object FFmpegKit {

    init {
        System.loadLibrary("ffmpegkit")
    }

    /**
     * Print all FFmpeg integration info to Logcat (tag: FFmpegKit).
     */
    @JvmStatic
    external fun printInfo()

    /**
     * Get FFmpeg version string (e.g. "8.0.1").
     */
    @JvmStatic
    external fun getVersion(): String

    /**
     * Get FFmpeg build configuration (./configure flags).
     */
    @JvmStatic
    external fun getBuildConfiguration(): String

    /**
     * Check if libass subtitle rendering is available.
     */
    @JvmStatic
    external fun checkLibassAvailability(): String

    /**
     * Returns true if libass was enabled in this FFmpeg build.
     */
    @JvmStatic
    fun isLibassAvailable(): Boolean {
        return getBuildConfiguration().contains("--enable-libass")
    }
}
