
package com.example.biowatchcollar.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlinx.coroutines.delay
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material3.AppScaffold
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.Text
import com.example.biowatchcollar.presentation.theme.BioWatchCollarTheme
import kotlin.time.Duration.Companion.milliseconds

// Material Design 500/600 palette
private val Green = Color(0xFF4CAF50)
private val Yellow = Color(0xFFFFC107)
private val Red = Color(0xFFE53935)
private val Blue = Color(0xFF2196F3)
private val CardBackground = Color(0xFF302B38)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WearApp()
        }
    }
}

@Composable
fun WearApp() {
    BioWatchCollarTheme {
        AppScaffold {
            val pagerState = rememberPagerState(
                initialPage = 0,
                pageCount = { 3 }
            )

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when (page) {
                    0 -> VestStatusScreen()
                    1 -> VestTestScreen()
                    2 -> CalibrationScreen()
                }
            }
        }
    }
}

// --------------------------------------------------
// SCREEN 1: VEST STATUS
// --------------------------------------------------

@Composable
fun VestStatusScreen() {
    val batteryPercentage = 85
    val signalStrength = 4
    val estimatedTime = "14h 30m"
    val isConnected = true

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 26.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "VEST STATUS",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
        )

        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(Green.copy(alpha = 0.20f))
                .padding(horizontal = 14.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(RoundedCornerShape(50))
                    .background(if (isConnected) Green else Red)
            )

            Spacer(modifier = Modifier.width(7.dp))

            Text(
                text = if (isConnected) "CONNECTED" else "DISCONNECTED",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (isConnected) Green else Red
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // BATTERY CARD

            Column(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(14.dp))
                    .background(CardBackground)
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("BATTERY", fontSize = 10.sp, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                    repeat(5) { index ->
                        Box(
                            modifier = Modifier
                                .width(9.dp)
                                .height(19.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(
                                    if (index < 4) Green
                                    else Color.Gray.copy(alpha = 0.3f)
                                )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(7.dp))

                Text(
                    "$batteryPercentage%",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // SIGNAL CARD

            Column(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(14.dp))
                    .background(CardBackground)
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("SIGNAL", fontSize = 10.sp, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(5.dp))

                Row(
                    modifier = Modifier.height(25.dp),
                    horizontalArrangement = Arrangement.spacedBy(3.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    repeat(4) { index ->
                        Box(
                            modifier = Modifier
                                .width(7.dp)
                                .height((7 + index * 5).dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(
                                    if (index < signalStrength) Green
                                    else Color.Gray.copy(alpha = 0.3f)
                                )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(7.dp))

                Text("Good", fontSize = 19.sp, fontWeight = FontWeight.Bold)
            }
        }

        // ESTIMATED BATTERY LIFE

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(CardBackground)
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "ESTIMATED TIME REMAINING",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                estimatedTime,
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                "Until charging is needed",
                fontSize = 10.sp,
                color = Color.LightGray
            )
        }
    }
}

// --------------------------------------------------
// SCREEN 2: VEST TEST
// --------------------------------------------------

@Composable
fun VestTestScreen() {

    // Temporary UI state.
    // Eventually these values will be connected to BLE.

    var vibrationStrength by remember { mutableIntStateOf(5) }
    var testStatus by remember { mutableStateOf("NONE") }

    val statusColor = when (testStatus) {
        "SENDING" -> Yellow
        "VIBRATING" -> Green
        "FAULT" -> Red
        else -> Color.Gray
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 14.dp, vertical = 26.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(9.dp)
    ) {

        // HEADER

        Text(
            text = "VEST TEST",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
        )

        // CURRENT TEST STATUS

        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(statusColor.copy(alpha = 0.18f))
                .padding(horizontal = 12.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(7.dp)
                    .clip(RoundedCornerShape(50))
                    .background(statusColor)
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = when (testStatus) {
                    "SENDING" -> "SENDING"
                    "VIBRATING" -> "VIBRATING"
                    "FAULT" -> "FAULT"
                    else -> "NO TEST"
                },
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = statusColor
            )
        }

        // VIBRATION STRENGTH DISPLAY

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(CardBackground)
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "VIBRATION STRENGTH",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                "$vibrationStrength / 10",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )

            // Strength adjustment buttons

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        vibrationStrength =
                            (vibrationStrength - 1).coerceAtLeast(1)
                    },
                    enabled = vibrationStrength > 1
                ) {
                    Text("-")
                }

                Button(
                    onClick = {
                        vibrationStrength =
                            (vibrationStrength + 1).coerceAtMost(10)
                    },
                    enabled = vibrationStrength < 10
                ) {
                    Text("+")
                }
            }
        }

        // TEST BUTTON

        Button(
            onClick = {
                testStatus = when (testStatus) {
                    "VIBRATING" -> "NONE"
                    else -> "VIBRATING"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                if (testStatus == "VIBRATING") "STOP TEST"
                else "TEST VIBRATION"
            )
        }
    }
}


// --------------------------------------------------
// SCREEN 3: CALIBRATION
// --------------------------------------------------

@Composable
fun CalibrationScreen() {

    var remainingSeconds by remember { mutableIntStateOf(15) }
    var isRunning by remember { mutableStateOf(false) }
    var isComplete by remember { mutableStateOf(false) }

    // Countdown timer
    LaunchedEffect(isRunning) {
        if (isRunning) {
            while (remainingSeconds > 0) {
                delay(1000.milliseconds)
                remainingSeconds--
            }

            isRunning = false
            isComplete = true
        }
    }

    val progress = remainingSeconds / 15f

    val statusColor = when {
        isComplete -> Green
        isRunning -> Blue
        else -> Color.Gray
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 14.dp, vertical = 26.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        // HEADER

        Text(
            text = "CALIBRATION",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
        )

        // STATUS

        Text(
            text = when {
                isComplete -> "CALIBRATION COMPLETE"
                isRunning -> "MEASURING BASELINE"
                else -> "READY"
            },
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = statusColor
        )

        // CIRCULAR COUNTDOWN

        Box(
            modifier = Modifier.size(145.dp),
            contentAlignment = Alignment.Center
        ) {

            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {

                // Background ring

                drawArc(
                    color = Color.Gray.copy(alpha = 0.25f),
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = 12.dp.toPx())
                )

                // Progress ring

                drawArc(
                    color = statusColor,
                    startAngle = -90f,
                    sweepAngle = 360f * progress,
                    useCenter = false,
                    style = Stroke(
                        width = 12.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = if (isComplete) "DONE"
                    else "$remainingSeconds",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = statusColor
                )

                Text(
                    text = if (isComplete) "SUCCESS"
                    else "SECONDS",
                    fontSize = 10.sp
                )
            }
        }

        // INSTRUCTIONS

        Text(
            text = when {
                isComplete ->
                    "Your baseline calibration is complete!"

                isRunning ->
                    "Stay still while the watch calibrates."

                else ->
                    "Stay still during calibration. Keep your body relaxed."
            },
            fontSize = 11.sp,
            color = Color.LightGray,
            lineHeight = 15.sp,
            textAlign = TextAlign.Center
        )

        // BEGIN / RESET BUTTON

        if (!isRunning && !isComplete) {

            Button(
                onClick = {
                    remainingSeconds = 15
                    isRunning = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("BEGIN CALIBRATION")
            }
        }

        if (isRunning) {

            Button(
                onClick = {
                    isRunning = false
                    remainingSeconds = 15
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("CANCEL")
            }
        }

        if (isComplete) {

            Button(
                onClick = {
                    remainingSeconds = 15
                    isComplete = false
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("CALIBRATE AGAIN")
            }
        }
    }
}
