
package com.example.biowatchcollar.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material3.AppScaffold
import androidx.wear.compose.material3.Text
import com.example.biowatchcollar.presentation.theme.BioWatchCollarTheme

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
            VestStatusScreen()
        }
    }
}

@Composable
fun VestStatusScreen() {
    // SIMULATED DATA
    // We will replace these values with BLE responses later.

    val batteryPercentage = 85
    val signalStrength = 4
    val estimatedTime = "14h 30m"
    val isConnected = true

    val green = Color(0xFF4CAF50)
    val red = Color(0xFFE53935)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        // HEADER

        Text(
            text = "VEST STATUS",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
        )

        // CONNECTION STATUS

        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(
                    if (isConnected)
                        green.copy(alpha = 0.20f)
                    else
                        red.copy(alpha = 0.20f)
                )
                .padding(horizontal = 14.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(RoundedCornerShape(50))
                    .background(if (isConnected) green else red)
            )

            Spacer(modifier = Modifier.width(7.dp))

            Text(
                text = if (isConnected) "CONNECTED" else "DISCONNECTED",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (isConnected) green else red
            )
        }

        // BATTERY AND SIGNAL CARDS

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            // BATTERY

            Column(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        androidx.wear.compose.material3.MaterialTheme
                            .colorScheme.surfaceContainer
                    )
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "BATTERY",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Battery bars

                Row(
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    repeat(5) { index ->
                        Box(
                            modifier = Modifier
                                .width(9.dp)
                                .height(19.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(
                                    if (index < 4) green
                                    else Color.Gray.copy(alpha = 0.3f)
                                )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(7.dp))

                Text(
                    text = "$batteryPercentage%",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // SIGNAL

            Column(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        androidx.wear.compose.material3.MaterialTheme
                            .colorScheme.surfaceContainer
                    )
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "SIGNAL",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )

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
                                    if (index < signalStrength) green
                                    else Color.Gray.copy(alpha = 0.3f)
                                )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(7.dp))

                Text(
                    text = when (signalStrength) {
                        4 -> "Good"
                        3 -> "Fair"
                        2 -> "Weak"
                        1 -> "Poor"
                        else -> "None"
                    },
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // ESTIMATED BATTERY LIFE

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(
                    androidx.wear.compose.material3.MaterialTheme
                        .colorScheme.surfaceContainer
                )
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "ESTIMATED TIME REMAINING",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = estimatedTime,
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Until charging is needed",
                fontSize = 10.sp,
                color = androidx.wear.compose.material3.MaterialTheme
                    .colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "BioWatch Vest System",
            fontSize = 10.sp,
            color = androidx.wear.compose.material3.MaterialTheme
                .colorScheme.onSurfaceVariant
        )
    }
}
