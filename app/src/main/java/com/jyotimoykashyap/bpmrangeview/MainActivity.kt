package com.jyotimoykashyap.bpmrangeview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jyotimoykashyap.bpmrangeview.ui.theme.BPMRangeViewTheme
import com.jyotimoykashyap.bpmrangeview.ui.widget.BpmRangeView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BPMRangeViewTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        var bpmInputValue by remember {
                            mutableStateOf(220)
                        }
                        BpmRangeView(
                            bpmValue = bpmInputValue,
                            width = 500.dp,
                            user = "Jyotimoy"
                        )
                    }

                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview() {
    BPMRangeViewTheme {
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            var bpmInputValue by remember {
                mutableStateOf(30)
            }
            BpmRangeView(
                bpmValue = 210,
                width = 500.dp,
                user = "Jyotimoy"
            )
        }
    }
}