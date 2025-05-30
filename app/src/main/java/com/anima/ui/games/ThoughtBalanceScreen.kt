package com.anima.ui.games

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.anima.data.therapeutic.TherapeuticGameConfigs
import com.anima.data.therapeutic.TherapeuticGameType
import com.anima.games.therapeutic.TherapeuticGameFactory
import com.anima.games.therapeutic.games.Thought
import com.anima.games.therapeutic.games.ThoughtType
import com.anima.games.therapeutic.games.ThoughtBalanceGame

@Composable
fun ThoughtBalanceScreen(
    onGameComplete: (Int) -> Unit
) {
    val game = remember { TherapeuticGameFactory.createGame(
        TherapeuticGameType.THOUGHT_BALANCE,
        TherapeuticGameConfigs.thoughtBalance
    ) as ThoughtBalanceGame }
    
    val balanceState by game.balanceState.collectAsState()
    val remainingTime by game.remainingTime.collectAsState()
    
    var balancedThought by remember { mutableStateOf("") }
    
    LaunchedEffect(Unit) {
        game.start()
    }
    
    LaunchedEffect(game.isFinished()) {
        if (game.isFinished()) {
            onGameComplete(game.getScore())
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Верхняя панель с информацией
            Text(
                text = "Баланс мыслей",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            Text(
                text = "Счет: ${balanceState.score}",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // Список мыслей
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                balanceState.thoughts.forEach { thought ->
                    ThoughtCard(
                        thought = thought,
                        isSelected = thought.id == balanceState.currentThought?.id,
                        onClick = { game.onThoughtSelected(thought.id) }
                    )
                }
            }
            
            // Поле для ввода сбалансированной мысли
            if (balanceState.currentThought != null) {
                OutlinedTextField(
                    value = balancedThought,
                    onValueChange = { balancedThought = it },
                    label = { Text("Введите сбалансированную мысль") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )
                
                Button(
                    onClick = {
                        game.onBalanceSelected(balancedThought)
                        balancedThought = ""
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Text("Сбалансировать мысль")
                }
            }
            
            // Нижняя панель с прогрессом
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (balanceState.feedback.isNotEmpty()) {
                    Text(
                        text = balanceState.feedback,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                
                LinearProgressIndicator(
                    progress = game.getProgress(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Осталось времени: ${remainingTime / 1000} сек",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Composable
private fun ThoughtCard(
    thought: Thought,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp)),
        colors = CardDefaults.cardColors(
            containerColor = when {
                thought.isBalanced -> Color(0xFF81C784)
                isSelected -> MaterialTheme.colorScheme.primaryContainer
                else -> when (thought.type) {
                    ThoughtType.NEGATIVE -> Color(0xFFE57373)
                    ThoughtType.POSITIVE -> Color(0xFF81C784)
                    ThoughtType.NEUTRAL -> Color(0xFF90CAF9)
                }
            }
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = thought.text,
                style = MaterialTheme.typography.bodyLarge,
                color = if (thought.isBalanced) Color.White else MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
            
            if (!thought.isBalanced && !isSelected) {
                Text(
                    text = when (thought.type) {
                        ThoughtType.NEGATIVE -> "Негативная мысль"
                        ThoughtType.POSITIVE -> "Позитивная мысль"
                        ThoughtType.NEUTRAL -> "Нейтральная мысль"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
} 