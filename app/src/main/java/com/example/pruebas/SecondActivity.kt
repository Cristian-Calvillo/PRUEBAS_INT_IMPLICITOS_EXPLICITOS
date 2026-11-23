
package com.example.pruebas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pruebas.ui.theme.PruebasTheme

class SecondActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PruebasTheme {
                SegundaPantalla(
                    onRegresar = {
                        finish()
                    }
                )
            }
        }
    }
}

@Composable
fun SegundaPantalla(onRegresar: () -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "¡Bienvenido a mi segunda actividad!",
            fontSize = 24.sp
        )

        Text(
            text = "Práctica de Intents en Android",
            modifier = Modifier.padding(top = 16.dp)
        )

        Button(
            onClick = onRegresar,
            modifier = Modifier.padding(top = 20.dp)
        ) {
            Text("Regresar")
        }
    }
}
