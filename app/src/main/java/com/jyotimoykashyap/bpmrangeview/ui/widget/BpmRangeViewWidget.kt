package com.jyotimoykashyap.bpmrangeview.ui.widget


import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import androidx.compose.animation.VectorConverter
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jyotimoykashyap.bpmrangeview.ui.theme.*
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@Composable
fun BpmRangeView(
    bpmValue: Int, // range from 30 to 220
    maxBpmValue: Int = 220,
    minBpmValue: Int = 30,
    width: Dp = 350.dp,
    textColor: Int = if(isSystemInDarkTheme()) Color.WHITE else Color.BLACK
) {
    val allowedWidth = LocalConfiguration.current.screenWidthDp.dp
    var allowedWithValue by remember {
        mutableStateOf(
            allowedWidth
        )
    }
    allowedWithValue = if(width > allowedWidth) allowedWithValue else width
    val isDarkTheme = isSystemInDarkTheme()
    val maxRange by remember {
        mutableStateOf(allowedWithValue - 70.dp)
    }
    var allowedBpmValue by remember {
        mutableStateOf(maxBpmValue)
    }
    allowedBpmValue =
        if(bpmValue > maxBpmValue) maxBpmValue
        else if(bpmValue < minBpmValue) minBpmValue
        else bpmValue
    var animatedBpmValue by remember {
        mutableStateOf(30)
    }
    LaunchedEffect(key1 = allowedBpmValue) {
        animatedBpmValue = allowedBpmValue
    }
    Log.i("bpmvalues" , "BPM Values : $animatedBpmValue")

    // animation values
    val offsetAnimation = remember {
        Animatable(0f)
    }
    val valueAnimation = remember {
        Animatable(30f)
    }
    val colorAnimation = remember {
        Animatable(
            lowBpmColorWhite,
            typeConverter =
            androidx.compose.ui.graphics.
            Color.VectorConverter(ColorSpaces.LinearSrgb)
        )
    }

    val offsetAnimation2 by animateFloatAsState(
        targetValue = convertBpmToFloat(
            bpmValue = animatedBpmValue,
            maxDp = maxRange
        ),
        animationSpec = tween(1000)
    )

    LaunchedEffect(key1 = offsetAnimation, key2 = valueAnimation, key3 = colorAnimation) {
        launch {
            offsetAnimation.animateTo(
                targetValue = convertBpmToFloat(
                    bpmValue = allowedBpmValue,
                    maxDp = maxRange
                ),
                animationSpec = tween(1000)
            )
        }
        launch {
            valueAnimation.animateTo(
                targetValue = allowedBpmValue.toFloat(),
                animationSpec = tween(1000)
            )
        }
        launch {
            colorAnimation.animateTo(
                targetValue = getTargetColor(
                    value = allowedBpmValue,
                    isDarkTheme = isDarkTheme
                ),
                animationSpec = tween(1000)
            )
        }
    }


    Column {
        Row(
            modifier = Modifier
                .width(allowedWithValue)
                .height(100.dp)
                .padding(
                    start = 10.dp,
                    end = 10.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // this is the actual container that will contain the number
            Box(
                modifier = Modifier
                    .width(55.dp)
                    .height(80.dp)
                    .offset(
                        convertFloatToDp(value = offsetAnimation.value),
                        0.dp
                    )
                    .graphicsLayer {
                        shape = BpmValueContainerShape()
                        clip = true
                    }
                    .background(
                        color = colorAnimation.value
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = valueAnimation.value.toInt().toString(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 19.sp
                )
            }
        }

        // This is for the range meter
        Row(
            modifier = Modifier
                .width(allowedWithValue)
                .height(70.dp)
                .padding(
                    start = 20.dp,
                    top = 0.dp,
                    end = 20.dp,
                    bottom = 10.dp
                )
                .drawBehind {
                    drawRange(
                        width = size.width,
                        textColor = textColor
                    )
                },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Column(
                modifier = Modifier
                    .wrapContentHeight()
            ) {

                // row for state of bpm health text
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(text = "Low", color = lowBpmColor)
                    Text(text = "Healthy", color = healthyBpmColor)
                    Text(text = "High", color = highBpmColor)
                    Text(text = "Danger", color = dangerBpmColor)
                }
            }
        }
    }
}

fun DrawScope.drawRange(
    width: Float,
    textColor: Int
) {
    // y offset
    val yOffsetValue = 20f
    val yTextOffsetValue = 80f
    // start and end offset
    val startOffsetLow = Offset(0f, yOffsetValue)
    val endOffsetLow = Offset(width.times((3.toFloat()/19.toFloat())), yOffsetValue)
    drawContext.canvas.nativeCanvas.drawText(
        "30",
        0f,
        yTextOffsetValue,
        Paint().apply {
            textSize = 30f
            color = textColor
        }
    )
    // low bpm line
    drawLine(
        color = lowBpmColor,
        start = startOffsetLow,
        end = endOffsetLow,
        strokeWidth = 30f,
        cap = StrokeCap.Round
    )
    drawContext.canvas.nativeCanvas.drawText(
        "60",
        width.absoluteValue.times((3.toFloat()/19.toFloat())) - 20f,
        yTextOffsetValue,
        Paint().apply {
            textSize = 30f
            color = textColor
        }
    )
    // start and end offset
    val startOffsetHealthy = Offset(width.times((3.toFloat()/19.toFloat())), yOffsetValue)
    val endOffsetHealthy = Offset(width.times((7.toFloat()/19.toFloat())), yOffsetValue)
    drawContext.canvas.nativeCanvas.drawText(
        "100",
        width.absoluteValue.times((7.toFloat()/19.toFloat())) - 20f,
        yTextOffsetValue,
        Paint().apply {
            textSize = 30f
            color = textColor
        }
    )
    // healthy bpm line
    drawLine(
        color = healthyBpmColor,
        start = startOffsetHealthy,
        end = endOffsetHealthy,
        strokeWidth = 30f,
        cap = StrokeCap.Butt
    )
    // start and end offset
    val startOffsetDanger = Offset(width.times((11.toFloat()/19.toFloat())), yOffsetValue)
    val endOffsetDanger = Offset(width, yOffsetValue)
    drawContext.canvas.nativeCanvas.drawText(
        "220",
        width.absoluteValue - 30f,
        yTextOffsetValue,
        Paint().apply {
            textSize = 30f
            color = textColor
        }
    )
    // danger bpm line
    drawLine(
        color = dangerBpmColor,
        start = startOffsetDanger,
        end = endOffsetDanger,
        strokeWidth = 30f,
        cap = StrokeCap.Round
    )
    // start and end offset
    val startOffsetHigh = Offset(width.times((7.toFloat()/19.toFloat())), yOffsetValue)
    val endOffsetHigh = Offset(width.times((11.toFloat()/19.toFloat())), yOffsetValue)
    drawContext.canvas.nativeCanvas.drawText(
        "140",
        width.absoluteValue.times((11.toFloat()/19.toFloat())) - 20f,
        yTextOffsetValue,
        Paint().apply {
            textSize = 30f
            color = textColor
        }
    )
    // high bpm line
    drawLine(
        color = highBpmColor,
        start = startOffsetHigh,
        end = endOffsetHigh,
        strokeWidth = 30f,
        cap = StrokeCap.Butt
    )
}

@Preview(showBackground = true)
@Composable
fun BpmRangeViewPreview() {
    Column {
        BpmRangeView(
            bpmValue = 140
        )
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun BpmRangeViewPreviewDark() {
    Column {
        BpmRangeView(
            bpmValue = 140
        )
    }
}