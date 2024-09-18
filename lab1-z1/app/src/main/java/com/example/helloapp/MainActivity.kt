package com.example.helloapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.helloapp.ui.theme.HelloAppTheme
import androidx.compose.foundation.background
import androidx.compose.material3.Button
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.saveable.rememberSaveable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HelloAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(modifier: Modifier = Modifier)
{
    Column (modifier = modifier.fillMaxSize().background(Color(0xFF2196F3)),
        horizontalAlignment = Alignment.CenterHorizontally) {
        var MyName by remember{mutableStateOf("Elina")}
        var textValue by remember{mutableStateOf("")}
        Text(
            text = "Hello $MyName!",
            modifier = modifier.padding(16.dp),
            fontSize = 48.sp
        )
        Image(
            painter = painterResource(id = R.drawable.student_img),
            contentDescription = "Hello Student Image"
        )
        TextField(
            value = textValue,
            onValueChange = {textValue = it},
            label = { Text(text = "Введите имя")},
            placeholder = { Text(text = "Student") },
            modifier = modifier.padding(4.dp)
        )
        Button(
            onClick =
            {
                MyName = textValue
            }
        ) {
            Text(text = "Приветствовать")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HelloAppTheme {
        Greeting()
    }
}