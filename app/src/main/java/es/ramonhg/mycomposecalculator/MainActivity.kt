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
        ElevatedButton(onClick = { screen = processLetter(screen, '1') }) {
            Text("1")
        }
        ElevatedButton(onClick = { screen = processLetter(screen, '2') }) {
            Text("2")
        }
        ElevatedButton(onClick = { screen = processLetter(screen, '3') }) {
            Text("3")
        }
        ElevatedButton(onClick = { screen = processLetter(screen, 'x') }) {
            Text("X")
        }
    }
    Row(horizontalArrangement = Arrangement.Center) {
        ElevatedButton(onClick = { screen = processLetter(screen, '4') }) {
            Text("4")
        }
        ElevatedButton(onClick = { screen = processLetter(screen, '5') }) {
            Text("5")
        }
        ElevatedButton(onClick = { screen = processLetter(screen, '6') }) {
            Text("6")
        }
        ElevatedButton(onClick = { screen = processLetter(screen, '/') }) {
            Text("÷")
        }
    }
    Row(horizontalArrangement = Arrangement.Center) {
        ElevatedButton(onClick = { screen = processLetter(screen, '7') }) {
            Text("7")
        }
        ElevatedButton(onClick = { screen = processLetter(screen, '8') }) {
            Text("8")
        }
        ElevatedButton(onClick = { screen = processLetter(screen, '9') }) {
            Text("9")
        }
        ElevatedButton(onClick = { screen = processLetter(screen, '+') }) {
            Text("+")
        }
    }
    Row(horizontalArrangement = Arrangement.Center) {
        ElevatedButton(onClick = { screen = processLetter(screen, '.') }) {
            Text(".")
        }
        ElevatedButton(onClick = { screen = processLetter(screen, '0') }) {
            Text("0")
        }
        ElevatedButton(onClick = { screen = solveCalculus(screen) }) {
            Text("=")
        }
        ElevatedButton(onClick = { screen = processLetter(screen, '-') }) {
            Text("-")
        }
    }
    Row(horizontalArrangement = Arrangement.Center) {
        ElevatedButton(onClick = { screen = "" }) {
            Text(text = "Clear")
        }
    }
}

fun processLetter(currentScreen: String, newLetter: Char): String {
    return if (currentScreen == "ERROR" || currentScreen == "Result of the calculation") "$newLetter" else "$currentScreen$newLetter"
}

fun resolveSigns(calculus: String): String {
    var result: String = calculus
    while ("++" in result || "--" in result || "+-" in result || "-+" in result) {
        result = result.replace("++", "+").replace("--", "+").replace("+-", "-").replace("-+", "-")
    }
    return result
}

fun removeLeadingSign(calculus: String, sign: Char): String {
    return if (calculus[0] == sign) calculus.drop(1) else calculus
}

fun calculateSimpleEquation(calculus: String, num1: String, num2: String, operation: Char): String {
    var result = 0.0
    val double1 = num1.toDouble()
    val double2 = num2.toDouble()

    // Get the result
    when (operation) {
        'x' -> result = double1 * double2
        '/' -> result = double1 / double2
        '+' -> result = double1 + double2
        '-' -> result = double1 - double2
    }

    // Replace result in the original string
    return calculus.replace("${num1}${operation}${num2}", "${if (result >= 0.0) '+' else ""}${result}")
}

fun switchMinusAndPlus(calculus: String, toPlus: Boolean): String {
    return if (toPlus)
        removeLeadingSign(calculus.replace('-', '+'), '+')
    else
        (if (calculus[0].isDigit()) "+$calculus" else calculus).replace('+', '-')
}

fun removeDotZero(calculus: String): String {
    return calculus.replace(".0", "")
}

fun solveCalculus(calculusStr: String): String {
    val symbols = arrayOf('/', 'x', '+', '-')
    var calculus = calculusStr
    try {
        val oldCalculus = calculus
        // Remove leading + sign to avoid treating it as the middle of an operation
        calculus = removeLeadingSign(resolveSigns(calculus), '+')
        // Remove first '-' to know if we have solved the equation
        var calculusPositive = removeLeadingSign(calculus, '-')
        var notSolved = !calculusPositive.replace('.', '0').isDigitsOnly()
        while (notSolved) {
            var currentSymbol = ' '
            // Get the symbol we will process in this iteration
            for (symbol in symbols) {
                if (symbol in calculusPositive) {
                    currentSymbol = symbol
                    break
                }
            }

            if (currentSymbol == ' ') {
                return "ERROR"
            }

            // Treat the subtracts of negative numbers as the negative of sums and then turn the
            // result into negative.
            var negativeMode = false
            if (currentSymbol == '-' && calculus[0] == '-') {
                currentSymbol = '+'
                calculus = switchMinusAndPlus(calculus = calculus, toPlus = true)
                negativeMode = true
            }

            var gettingNum = 1
            var num1 = ""
            var num2 = ""
            var calculatedInCurrentIteration = false
            for (letter in calculus) {
                // We only want to save the '-' sign if we are not processing that symbol and if we
                // are getting the 1st number or if we are going to process the 2nd number
                val saveMinus = currentSymbol != '-' && (gettingNum == 1 || num2 == "")
                if (letter.isDigit() || letter == '.' || (letter == '-' && saveMinus)) {
                    if (gettingNum == 1)
                        num1 = if (saveMinus && letter == '-') letter.toString() else num1 + letter
                    else
                        num2 += letter
                } else {
                    if (gettingNum == 1 && letter == currentSymbol) {
                        // We found an interesting symbol. We continue processing the 2nd number
                        num2 = ""
                        gettingNum = 2
                    } else if (gettingNum == 1) {
                        // We found a non-relevant symbol. Discard the current 1st number and
                        // keep scanning the next number
                        num1 = ""
                    } else {
                        // We finish here calculating the expression and replacing it by its result
                        calculus = calculateSimpleEquation(calculus, num1, num2, currentSymbol)

                        calculatedInCurrentIteration = true
                    }
                }
            }

            if (!calculatedInCurrentIteration && gettingNum == 2) {
                calculus = calculateSimpleEquation(calculus, num1, num2, currentSymbol)
            }

            if (negativeMode) {
                calculus = switchMinusAndPlus(calculus = calculus, toPlus = false)
            }

            calculus = removeLeadingSign(resolveSigns(calculus), '+')
            calculusPositive = removeLeadingSign(calculus, '-')
            notSolved = !calculusPositive.replace('.', '0').isDigitsOnly()
            if (calculus == oldCalculus)
                return "ERROR"
        }
        return removeDotZero(calculus)
    } catch (e: Exception) {
        return "ERROR"
    }
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