package com.example.dyf.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class AyudaActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AyudaScreen()
        }
    }
}

@Composable
fun AyudaScreen() {
    val ayudaItems = listOf(
        "Inicio de sesión" to "Inicia sesión con tu correo y contraseña. Si olvidas tu contraseña, usa el botón 'Recuperar Contraseña'.",
        "Habla por Mí" to "Escribe un mensaje y presiona el botón para que el dispositivo hable por ti. Útil para comunicarte en entornos ruidosos.",
        "Escucha por Mí" to "Presiona el botón de micrófono para que el dispositivo transcriba lo que escucha en tu entorno.",
        "Copiar Ubicación" to "Obtén tu ubicación actual y cópiala para usarla en otras aplicaciones como mapas o mensajería.",
        "Accesibilidad" to "La aplicación es compatible con funciones de accesibilidad como ampliación de texto y lectura de pantalla."
    )

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Área de Ayuda", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        ayudaItems.forEach { (titulo, descripcion) ->
            AyudaItem(titulo, descripcion)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun AyudaItem(titulo: String, descripcion: String) {
    Column {
        Text(titulo, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text(descripcion, fontSize = 16.sp)
    }
}
