package com.example.randomchallenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.randomchallenge.ui.theme.RandomChallengeTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RandomChallengeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RandomChallengeApp()
                }
            }
        }
    }
}

@Composable
fun RandomChallengeApp(modifier: Modifier = Modifier) {
    var minValueInput by remember { mutableStateOf("1") }
    val minValue = minValueInput.toIntOrNull() ?: 0

    var maxValueInput by remember { mutableStateOf("") }
    val maxValue = maxValueInput.toIntOrNull() ?: 0

    var generationCountInput by remember { mutableStateOf("1") }
    val generationCount = generationCountInput.toIntOrNull() ?: 1

    val isValidMinValue = isValidInteger(minValueInput)
    val isValidMaxValue = isValidInteger(maxValueInput)
    val isValidGenerationCount = isValidInteger(generationCountInput)
    val allInputsValid = isValidMinValue && isValidMaxValue && isValidGenerationCount

    var advanced by remember { mutableStateOf(false) }

    var nonRepetitive by remember { mutableStateOf(false) }

    var generationResult by remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AppTitle()
        Spacer(modifier = Modifier.height(16.dp))
        EditNumberField(
            label = R.string.max_value,
            value = maxValueInput,
            onValueChange = { maxValueInput = it },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = when (advanced) {
                    true -> ImeAction.Next
                    false -> ImeAction.Done
                }
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )
        if (advanced) {
            Row {
                EditNumberField(
                    label = R.string.min_value,
                    value = minValueInput,
                    onValueChange = { minValueInput = it },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = modifier.weight(0.1f))
                EditNumberField(
                    label = R.string.generation_count,
                    value = generationCountInput,
                    onValueChange = { generationCountInput = it },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.clearFocus() }
                    ),
                    modifier = Modifier.weight(1f)
                )
            }
        }
        AdvancedOptionRow(advanced = advanced, onAdvancedChange = { advanced = it })
        NonRepetitiveOptionRow(
            nonRepetitive = nonRepetitive,
            onNonRepetitiveChange = { nonRepetitive = it })
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                generationResult =
                    generate(minValue, maxValue, generationCount, allInputsValid, nonRepetitive)
            },
            enabled = generationButtonEnabled(
                minValue,
                maxValue,
                generationCount,
                allInputsValid,
                nonRepetitive
            )
        ) {
            Text(stringResource(id = R.string.generate))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(id = R.string.result, generationResult),
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun AppTitle(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.title_cn),
            fontSize = 40.sp,
            modifier = modifier.padding(top = 12.dp)
        )
        Text(
            text = stringResource(id = R.string.title_en),
            fontWeight = FontWeight.Bold,
            color = Color.LightGray
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNumberField(
    @StringRes label: Int,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = stringResource(id = label),
                modifier = modifier.fillMaxWidth()
            )
        },
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = true,
        modifier = modifier
    )
}

@Composable
fun AdvancedOptionRow(
    advanced: Boolean,
    onAdvancedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .size(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = stringResource(id = R.string.advanced_option))
        Switch(
            checked = advanced,
            onCheckedChange = onAdvancedChange,
            modifier = modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End),
            colors = SwitchDefaults.colors(uncheckedThumbColor = Color.DarkGray)
        )
    }
}

@Composable
fun NonRepetitiveOptionRow(
    nonRepetitive: Boolean,
    onNonRepetitiveChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .size(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = stringResource(id = R.string.non_repetitive))
        Switch(
            checked = nonRepetitive,
            onCheckedChange = onNonRepetitiveChange,
            modifier = modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End),
            colors = SwitchDefaults.colors(uncheckedThumbColor = Color.DarkGray)
        )
    }
}

private fun generate(
    minValue: Int,
    maxValue: Int,
    generationCount: Int,
    allInputsValid: Boolean,
    nonRepetitive: Boolean // 添加一个参数，用于接收用户是否选择了不重复选项的可变状态变量
): String {
    if (maxValue == 0) return ""

    if (!allInputsValid || minValue > maxValue || generationCount <= 0) {
        return "请检查输入错误！"
    }

    val random = Random(System.currentTimeMillis())

    // 使用一个 MutableList 来存储生成的随机数
    val numbers = mutableListOf<String>()

    // 生成指定个数的随机数，并将其转换为字符串
    repeat(generationCount) {
        var number: Int // 定义一个变量，用于存储生成的随机数
        do {
            // 生成一个随机数
            number = random.nextInt(minValue, maxValue + 1)
        } while (nonRepetitive && numbers.contains(number.toString())) // 如果用户选择了不重复选项，就检查生成的随机数是否已经在 MutableList 中存在，如果存在，就重新生成一个随机数
        // 将生成的随机数转换为字符串，并添加到 MutableList 中
        numbers.add(number.toString())
    }

    // 将 MutableList 用逗号和空格连接起来，并返回结果
    return "随机结果：" + numbers.joinToString(", ")
}

private fun generationButtonEnabled(
    minValue: Int,
    maxValue: Int,
    generationCount: Int,
    allInputsValid: Boolean,
    nonRepetitive: Boolean
): Boolean {
    if (allInputsValid && minValue <= maxValue && generationCount > 0) {
        if (nonRepetitive && generationCount > (maxValue - minValue + 1)) {
            return false
        }
        return true
    }
    return false
}

// 使用正则表达式，验证输入是否为整数
private fun isValidInteger(input: String): Boolean {
    val regex = Regex("-?\\d+")
    return regex.matches(input)
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    RandomChallengeTheme {
        RandomChallengeApp()
    }
}