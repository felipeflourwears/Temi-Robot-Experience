package com.example.mediatemigo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mediatemigo.ui.theme.MediaTemiGoTheme
import com.robotemi.sdk.Robot
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener
import com.robotemi.sdk.TtsRequest

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch



val LOCATION_P1 = "p1".lowercase()
val LOCATION_P2 = "p2".lowercase()


class MainActivity : ComponentActivity() {
    private val robot = Robot.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        gotoP1ThenP2()
    }

    private fun gotoP1ThenP2() {
        // Definir el listener para el cambio de estado de goToLocation
        val goToLocationListener = object : OnGoToLocationStatusChangedListener {
            override fun onGoToLocationStatusChanged(
                location: String,
                status: String,
                descriptionId: Int,
                description: String
            ) {
                if (status == OnGoToLocationStatusChangedListener.COMPLETE) {
                    if (location == LOCATION_P1) {
                        // El robot ha llegado a "Punto 1", ahora puedes enviarlo a "Punto 2" después de una demora
                        val ttsRequest = TtsRequest.create("Punto uno", true)
                        robot.speak(ttsRequest)
                        GlobalScope.launch(Dispatchers.Main) {
                            delay(10000) // Esperar 5 segundos (ajusta el valor según tus necesidades)
                            robot.goTo(LOCATION_P2)

                        }
                    } else if (location == LOCATION_P2) {

                        val ttsRequest = TtsRequest.create("Punto dos", true)
                        robot.speak(ttsRequest)
                        // El robot ha llegado a "Punto 2", ahora puedes darle tiempo para calcular su posición
                        GlobalScope.launch(Dispatchers.Main) {
                            delay(10000) // Esperar 5 segundos (ajusta el valor según tus necesidades
                        }
                    }
                }
            }
        }

        // Registrar el listener
        robot.addOnGoToLocationStatusChangedListener(goToLocationListener)

        // Ir al "Punto 1" (P1)
        robot.goTo(LOCATION_P1)
    }


}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MediaTemiGoTheme {
        Greeting("Android")
    }
}