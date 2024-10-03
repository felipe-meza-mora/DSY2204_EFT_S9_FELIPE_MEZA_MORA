package com.example.dyf.screens

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dyf.MenuActivity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AyudaScreen() {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        // Botón Volver al menú
        IconButton(
            onClick = {
                val intent = Intent(context, MenuActivity::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Volver al menú",
                tint = Color(0xFFFFC107)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Área de Ayuda",
            fontSize = 24.sp,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        HelpItem(
            title = "Habla y Escucha por Mí",
            description = """
                Esta funcionalidad permite la comunicación asistida. Aquí te mostramos cómo usarla:
                
                1. **Habla por Mí**: Escribe un mensaje y presiona el botón para que el dispositivo lo lea en voz alta. 
                   - Ideal para usuarios que desean expresar algo verbalmente pero prefieren escribir.
                2. **Escucha por Mí**: Activa el micrófono para que el dispositivo transcriba lo que escucha. El texto aparecerá en pantalla para que el usuario lo lea.
                   - Útil para conversaciones donde el usuario necesita leer lo que la otra persona está diciendo.
                3. Todo lo que escribes o escuchas se guardará como un historial de chat para que puedas revisarlo más tarde.
                
                **Consejo**: Si el reconocimiento de voz no es preciso, asegúrate de estar en un entorno silencioso y de hablar claramente.
            """.trimIndent()
        )

        HelpItem(
            title = "Obtener Mi Ubicación",
            description = """
                Utiliza esta opción para encontrar tu ubicación actual.
                
                1. Presiona el botón "Obtener Mi Ubicación".
                2. La aplicación accederá a los servicios de geolocalización del dispositivo.
                3. Verás la latitud, longitud y la dirección aproximada de tu ubicación.
                
                **Consejo**: Asegúrate de que los permisos de ubicación estén habilitados y que estés en un lugar con buena recepción de GPS.
            """.trimIndent()
        )

        HelpItem(
            title = "Cambiar Contraseña",
            description = """
                Para mantener tu cuenta segura, puedes cambiar la contraseña siguiendo estos pasos:
                
                1. Accede al menú y selecciona "Cambiar Contraseña".
                2. Ingresa tu contraseña actual y la nueva contraseña que deseas utilizar.
                3. Confirma la nueva contraseña y presiona "Guardar".
                
                **Consejo**: Asegúrate de elegir una contraseña fuerte que incluya letras, números y caracteres especiales.
            """.trimIndent()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Para más información o asistencia, por favor contacta al soporte.",
            fontSize = 16.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun HelpItem(title: String, description: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = title, fontSize = 20.sp, color = Color.Black, fontWeight = FontWeight.Bold)
        Text(text = description, fontSize = 16.sp, color = Color.Gray)
    }
}
