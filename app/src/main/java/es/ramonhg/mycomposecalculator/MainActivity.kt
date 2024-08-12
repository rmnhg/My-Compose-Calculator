package es.ramonhg.mycomposecalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import es.ramonhg.mycomposecalculator.ui.theme.MyComposeCalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyComposeCalculatorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(24.dp)
                                .fillMaxHeight()
                    ) {
                        SimpleFilledTextFieldSample(defaultName = "Android")
                        Calculator()
                    }
                }
            }
        }
    }
}

@Composable
fun Calculator() {
    var screen by remember { mutableStateOf("Result of the calculation")}
    Row(horizontalArrangement = Arrangement.Center) {
        Text(text = "This is my Calculator")
    }
    Text(text = screen)
    Row(horizontalArrangement = Arrangement.Center) {
        ElevatedButton(onClick = { screen += "1" }) {
            Text("1")
        }
        ElevatedButton(onClick = { screen += "2" }) {
            Text("2")
        }
        ElevatedButton(onClick = { screen += "3" }) {
            Text("3")
        }
        ElevatedButton(onClick = { screen += "x" }) {
            Text("X")
        }
    }
    Row(horizontalArrangement = Arrangement.Center) {
        ElevatedButton(onClick = { screen += "4" }) {
            Text("4")
        }
        ElevatedButton(onClick = { screen += "5" }) {
            Text("5")
        }
        ElevatedButton(onClick = { screen += "6" }) {
            Text("6")
        }
        ElevatedButton(onClick = { screen += "/" }) {
            Text("%")
        }
    }
    Row(horizontalArrangement = Arrangement.Center) {
        ElevatedButton(onClick = { screen += "7" }) {
            Text("7")
        }
        ElevatedButton(onClick = { screen += "8" }) {
            Text("8")
        }
        ElevatedButton(onClick = { screen += "9" }) {
            Text("9")
        }
        ElevatedButton(onClick = { screen += "+" }) {
            Text("+")
        }
    }
    Row(horizontalArrangement = Arrangement.Center) {
        ElevatedButton(onClick = { screen += "." }) {
            Text(".")
        }
        ElevatedButton(onClick = { screen += "0" }) {
            Text("0")
        }
        ElevatedButton(onClick = { screen = "" }) {
            Text("=")
        }
        ElevatedButton(onClick = { screen += "-" }) {
            Text("-")
        }
    }
    Row(horizontalArrangement = Arrangement.Center) {
        ElevatedButton(onClick = { screen = "" }) {
            Text(text = "Clear")
        }
    }
}

fun solveCalculus(calculus: String): String {
    val symbols = arrayOf('*', '/', '+', '-')
    if (!calculus[0].isDigit() || calculus == "ERROR")
        return "ERROR"
    if (calculus.isDigitsOnly())
        return calculus
    var gettingNum: String = "1"
    var num1: String = ""
    var num2: String = ""
    var operation: Char = ' '
    for (letter in calculus) {
        if (gettingNum != "" && (letter.isDigit() || letter == '.')) {
            if (gettingNum == "1")
                num1 += letter
            else
                num2 += letter
        } else {
            gettingNum = ""
        }
    }
    return "ERROR"
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleFilledTextFieldSample(defaultName: String) {
    var text by remember { mutableStateOf(defaultName) }
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(24.dp)
    ) {
        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Insert your name here") }
        )
        Greeting(name = text)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyComposeCalculatorTheme {
        Greeting("Android")
    }
}