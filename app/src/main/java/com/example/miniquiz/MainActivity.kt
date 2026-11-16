package com.example.miniquiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Question(
    val text: String,
    val options: List<String>,
    val correctIndex: Int
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                QuizScreen()
            }
        }
    }
}

@Composable
fun QuizScreen() {
    val questions = remember {
        listOf(
            Question(
                text = "Какой язык используется для разработки Android приложений?",
                options = listOf("Kotlin", "Python"),
                correctIndex = 0
            ),
            Question(
                text = "Какой базовый компонент представляет экран в Android?",
                options = listOf("Activity", "Service"),
                correctIndex = 0
            ),
            Question(
                text = "Какая система сборки используется в Android по умолчанию?",
                options = listOf("Gradle", "Maven"),
                correctIndex = 0
            )
        )
    }

    var currentIndex by remember { mutableIntStateOf(0) }
    var score by remember { mutableIntStateOf(0) }
    var selectedOption by remember { mutableStateOf<Int?>(null) }

    val currentQuestion = questions[currentIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Вопрос ${currentIndex + 1} из 3",
            color = Color.Gray,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = currentQuestion.text,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            currentQuestion.options.forEachIndexed { index, option ->
                val isSelected = selectedOption == index
                val isCorrect = index == currentQuestion.correctIndex

                val containerColor = when {
                    selectedOption == null -> Color(0xFFE9EDF5)
                    isSelected && isCorrect -> Color(0xFF34C759)
                    isSelected && !isCorrect -> Color(0xFFFF3B30)
                    else -> Color(0xFFE9EDF5)
                }

                val contentColor = when {
                    selectedOption == null -> Color(0xFF1C1C1E)
                    isSelected -> Color.White
                    else -> Color(0xFF1C1C1E)
                }

                Button(
                    onClick = {
                        if (selectedOption == null) {
                            selectedOption = index
                            if (index == currentQuestion.correctIndex) {
                                score++
                            }
                        }
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = containerColor,
                        contentColor = contentColor
                    )
                ) {
                    Text(text = option)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (selectedOption != null) {
            val isCorrect = selectedOption == currentQuestion.correctIndex
            val cardBg = if (isCorrect) Color(0xFFE6F7EA) else Color(0xFFFDE7E7)

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = cardBg)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (isCorrect) "Правильно!" else "Неправильно!",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Ваш счёт: $score",
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        Button(
            onClick = {
                if (currentIndex < 2) {
                    currentIndex++
                    selectedOption = null
                } else {
                    currentIndex = 0
                    score = 0
                    selectedOption = null
                }
            },
            enabled = selectedOption != null,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF007AFF),
                contentColor = Color.White,
                disabledContainerColor = Color(0xFFB3D4FF),
                disabledContentColor = Color.White
            )
        ) {
            Text(
                text = if (currentIndex < 2) "Следующий вопрос" else "Начать заново"
            )
        }
    }
}
