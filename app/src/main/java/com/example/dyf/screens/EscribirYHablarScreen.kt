package com.example.dyf.screens

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.example.dyf.MenuActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*


data class Message(
    val content: String = "",
    val email: String = "",
    val type: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EscribirYHablarScreen() {
    val context = LocalContext.current
    var text by remember { mutableStateOf("") }
    var textToSpeech by remember { mutableStateOf<TextToSpeech?>(null) }


    val chatHistory = remember { mutableStateListOf<Message>() }
    val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("messages")

    val sharedPreferences = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE)
    val userEmail = sharedPreferences.getString("userEmail", "usuario@ejemplo.com") ?: "usuario@ejemplo.com"



    fun sendMessage(text: String) {
        val message = Message(content = text, email = userEmail, type = "envío")
        database.push().setValue(message)
    }

    fun receiveMessage(text: String) {
        val message = Message(content = text, email = userEmail, type = "recibo")
        database.push().setValue(message)
    }


    val speechToTextLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val data = result.data
        if (result.resultCode == android.app.Activity.RESULT_OK && data != null) {
            val resultText = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.get(0)
            if (resultText != null) {

                receiveMessage(resultText)
            }
        }
    }


    LaunchedEffect(Unit) {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Limpiar el historial y cargar nuevos mensajes
                chatHistory.clear()

                for (messageSnapshot in snapshot.children) {
                    val message = messageSnapshot.getValue(Message::class.java)
                    message?.let {
                        // Filtrar los mensajes para que solo se muestren los del usuario logueado
                        if (message.email == userEmail) {
                            chatHistory.add(it)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error al cargar el historial", Toast.LENGTH_SHORT).show()
            }
        })
    }


    LaunchedEffect(Unit) {
        textToSpeech = TextToSpeech(context) { status ->
            if (status != TextToSpeech.ERROR) {
                textToSpeech?.language = Locale("es", "ES")
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            textToSpeech?.stop()
            textToSpeech?.shutdown()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Botón Volver
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.TopStart
        ) {
            IconButton(
                onClick = {
                    val intent = Intent(context, MenuActivity::class.java)
                    context.startActivity(intent)
                },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Volver al Menú",
                    tint = Color(0xFFFFC107),
                    modifier = Modifier.size(48.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))


        val listState = rememberLazyListState()

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.Bottom,
            reverseLayout = false
        ) {
            items(chatHistory) { message ->
                ChatBubble(message)
            }
        }



        LaunchedEffect(chatHistory.size) {
            listState.animateScrollToItem(0)
        }


        Text(
            text = "Escribe un mensaje o usa el micrófono.",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))


        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Escribe algo") },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            maxLines = 8,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFFFFC107),
                cursorColor = Color(0xFFFFC107)
            )
        )

        Spacer(modifier = Modifier.height(16.dp))


        Button(
            onClick = {
                if (text.isNotBlank()) {
                    textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
                    sendMessage(text)
                    text = ""
                } else {
                    Toast.makeText(context, "Escribe algo para hablar", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107))
        ) {
            Icon(
                imageVector = Icons.Filled.Campaign,
                contentDescription = "Play",
                modifier = Modifier.size(24.dp),
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Habla por Mí", fontSize = 18.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(8.dp))


        Button(
            onClick = {
                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                try {
                    speechToTextLauncher.launch(intent)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(context, "Reconocimiento de voz no soportado", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107))
        ) {
            Icon(
                imageVector = Icons.Default.Mic,
                contentDescription = "Micrófono",
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Escuchar por Mí", fontSize = 18.sp, color = Color.White)
        }
    }
}

@Composable
fun ChatBubble(message: Message) {

    val isSentMessage = message.type == "envío"


    val backgroundColor = if (isSentMessage) Color(0xFF2196F3) else Color(0xFFFFC107)
    val textColor = if (isSentMessage) Color.White else Color.Black
    val alignment = if (isSentMessage) Alignment.CenterEnd else Alignment.CenterStart


    val icon = if (isSentMessage) Icons.Default.Campaign else Icons.Default.Mic

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        contentAlignment = alignment
    ) {
        Surface(
            modifier = Modifier
                .padding(8.dp)
                .wrapContentSize(),
            color = backgroundColor,
            shape = MaterialTheme.shapes.medium
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(8.dp)
            ) {

                Icon(
                    imageVector = icon,
                    contentDescription = if (isSentMessage) "Habla por Mí" else "Escucha por Mí",
                    tint = textColor,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = message.content,
                    color = textColor,
                    fontSize = 16.sp,
                    maxLines = 5,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
