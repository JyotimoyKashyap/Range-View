package com.jyotimoykashyap.bpmrangeview.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp


class BpmValueContainerShape() :Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val radius = size.width.div(2)
        val height = size.height - radius.times(2)
        return Outline.Generic(
            // draw your custom path here
            path = drawBpmContainerPath(height = height, radius = radius)
        )
    }

    private fun drawBpmContainerPath(
        height: Float,
        radius: Float
    ) :Path {
        return Path().apply {
            reset()
            val centreOffsetAbove = Offset(radius, radius)
            // draw the top semi circle
            arcTo(
                rect = Rect(
                    centreOffsetAbove, radius
                ),
                sweepAngleDegrees = 180f,
                startAngleDegrees = -180f,
                forceMoveTo = false
            )
            lineTo(radius*2, height)
            val centreOffsetBelow = Offset(radius, radius.plus(height))
            arcTo(
                rect = Rect(
                    centreOffsetBelow, radius
                ),
                sweepAngleDegrees = 180f,
                startAngleDegrees = 0f,
                forceMoveTo = false
            )
            lineTo(0f, -height)
            close()
        }
    }

}

@Preview
@Composable
fun BpmValueContainerShapePreview() {
    Row (
        modifier = Modifier
            .height(370.dp)
            .width(200.dp)
            .graphicsLayer {
                shape = BpmValueContainerShape()
                clip = true
            }
            .background(color = Color.Red)
    ){

    }
}