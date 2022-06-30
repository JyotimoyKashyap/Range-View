package com.jyotimoykashyap.bpmrangeview.ui.widget


import android.util.Log
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jyotimoykashyap.bpmrangeview.ui.theme.*

@Composable
fun convertBpmToDp(
    bpmValue: Int,
    maxDp: Dp,
    minBpm: Int = 30,
    maxBpm: Int = 220
) :Dp {
    val density = LocalDensity.current.density
    val maxFloatValue = maxDp.value
    val bpmPercent = (bpmValue.toFloat()/(maxBpm - minBpm).toFloat()).toFloat()
    val bpmFloat = bpmPercent.times(maxFloatValue)
    return (bpmFloat/density).dp
}


fun convertBpmToFloat(
    bpmValue: Int,
    maxDp: Dp,
    minBpm: Int = 30,
    maxBpm: Int = 220
) :Float {
    val maxFloatValue = maxDp.value
    Log.i("conversion" , "max float value : $maxFloatValue")
    val bpmPercent = ((bpmValue-30).toFloat()/(maxBpm-minBpm).toFloat())
    Log.i("conversion" , "bpm percent : $bpmPercent")
    val bpmFloat = bpmPercent.times(maxFloatValue)
    Log.i("conversion" , "bpm float : $bpmFloat")
    return bpmFloat
}

@Composable
fun convertFloatToDp(
    value: Float
):Dp{
    val density = LocalDensity.current.density
    Log.i("conversion" , "value : $value, density: $density")
    return (value).dp
}


fun getContainerColor(
    value: Int,
    isDarkTheme: Boolean
): Color {
    if(value in 30..60) {
        return lowBpmColorWhite
    }
    if(value in 61..100) {
        return healthyBpmColorWhite
    }
    if(value in 101..140) {
        return highBpmColorWhite
    }
    if(value in 140..220) {
        return dangerBpmColorWhite
    }
    return if(isDarkTheme) Color.DarkGray else Color.White
}

fun getTextColorPerCondition(
    value: Int,
    isDarkTheme: Boolean
): Color {
    if(value in 30..60) {
        return lowBpmColor
    }
    if(value in 61..100) {
        return healthyBpmColor
    }
    if(value in 101..140) {
        return highBpmColor
    }
    if(value in 140..220) {
        return dangerBpmColor
    }
    return if(isDarkTheme) Color.DarkGray else Color.White
}

fun getBpmCondition(
    value: Int
): String {
    if(value in 30..60) {
        return "Low"
    }
    if(value in 61..100) {
        return "Healthy"
    }
    if(value in 101..140) {
        return "High"
    }
    if(value in 140..220) {
        return "Danger"
    }
    return "null"
}

fun createSpannableText(
    condition: String,
    color: Color
) = buildAnnotatedString {
        append("Your Heartbeat is ")
        withStyle(style = SpanStyle(color = color)) {
            append(condition)
        }
        append(" for Resting State")
    }



