package com.example.randomchallenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.randomchallenge.ui.theme.RandomChallengeTheme

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
                    RandomChallengeApp(
                        titleCN = stringResource(id = R.string.title_cn),
                        titleEN = stringResource(id = R.string.title_en)
                    )
                }
            }
        }
    }
}

@Composable
fun RandomChallengeApp(titleCN: String, titleEN: String, modifier: Modifier = Modifier) {
    Column (
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF073042))
    ) {
        Box(
            modifier = modifier
                .weight(0.7f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            MainInformation(
                titleCN = titleCN,
                titleEN = titleEN
            )
        }
    }
}

@Composable
fun MainInformation(titleCN: String, titleEN: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(R.drawable.baseline_android_24),
            contentDescription = null,
            tint = Color(0xFF3DDC84),
        )
        Text(
            text = titleCN,
            fontSize = 40.sp,
            color = Color.White,
            modifier = modifier.padding(16.dp)
        )
        Text(
            text = titleEN,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF3ddc84),
            modifier = modifier.padding(16.dp)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RandomChallengeTheme() {
        RandomChallengeApp(
            titleCN = stringResource(id = R.string.title_cn),
            titleEN = stringResource(id = R.string.title_en)
        )
    }
}