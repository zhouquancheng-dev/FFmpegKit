package dev.zqc.ffmpegkit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import dev.zqc.ffmpegkit.theme.FFmpegKitTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()
        printFFmpegInfo()
        setContent {
            FFmpegKitTheme {
                FFmpegInfoScreen(
                    version = getFFmpegVersion(),
                    buildConfig = getFFmpegBuildConfiguration(),
                    libassStatus = checkLibassAvailability()
                )
            }
        }
    }

    external fun printFFmpegInfo()
    external fun getFFmpegVersion(): String
    external fun getFFmpegBuildConfiguration(): String
    external fun checkLibassAvailability(): String

    companion object {
        init {
            System.loadLibrary("ffmpegkit")
        }
    }
}

@Composable
private fun FFmpegInfoScreen(
    version: String,
    buildConfig: String,
    libassStatus: String
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "FFmpeg Integration Test",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 48.dp, bottom = 8.dp)
            )
        }

        item {
            InfoSection(title = "FFmpeg Version", content = version)
            HorizontalDivider()
        }

        item {
            InfoSection(title = "libass Status", content = libassStatus)
            HorizontalDivider()
        }

        item {
            InfoSection(title = "Build Configuration", content = buildConfig)
        }
    }
}

@Composable
private fun InfoSection(title: String, content: String) {
    Text(
        text = title,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
    )
    Text(
        text = content,
        fontSize = 14.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(top = 4.dp)
    )
}
