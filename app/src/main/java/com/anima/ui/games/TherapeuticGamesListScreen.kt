package com.anima.ui.games

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.anima.data.therapeutic.TherapeuticGameConfigs
import com.anima.data.therapeutic.TherapeuticGameType

data class TherapeuticGameInfo(
    val type: TherapeuticGameType,
    val title: String,
    val description: String,
    val icon: ImageVector = Icons.Default.PlayArrow,
    val config: TherapeuticGameConfig = TherapeuticGameConfigs.dragonBreathing
)

@Composable
fun TherapeuticGamesListScreen(
    onGameSelected: (TherapeuticGameType) -> Unit
) {
    val games = listOf(
        TherapeuticGameInfo(
            type = TherapeuticGameType.DRAGON_BREATHING,
            title = "Дыхание дракона",
            description = "Управляйте дыханием дракона, чтобы контролировать размер пламени. " +
                         "Практикуйте технику дыхания 4-7-8 для снижения тревоги."
        ),
        TherapeuticGameInfo(
            type = TherapeuticGameType.THOUGHT_GARDEN,
            title = "Сад мыслей",
            description = "Поливайте позитивные мысли и вырывайте негативные. " +
                         "Тренируйте навыки когнитивно-поведенческой терапии."
        ),
        TherapeuticGameInfo(
            type = TherapeuticGameType.EMOTION_PUZZLE,
            title = "Эмоциональный пазл",
            description = "Собирайте лица, выражающие разные эмоции. " +
                         "Развивайте эмоциональный интеллект и эмпатию."
        ),
        TherapeuticGameInfo(
            type = TherapeuticGameType.SOCIAL_MAZE,
            title = "Лабиринт социальных ситуаций",
            description = "Выбирайте правильные ответы в социальных ситуациях. " +
                         "Улучшайте навыки общения и понимания других."
        ),
        TherapeuticGameInfo(
            type = TherapeuticGameType.SOUND_DETECTIVE,
            title = "Звуковой детектив",
            description = "Слушайте и определяйте успокаивающие звуки. " +
                         "Тренируйте концентрацию внимания."
        ),
        TherapeuticGameInfo(
            type = TherapeuticGameType.RHYTHM_MEDITATION,
            title = "Ритм-медитация",
            description = "Следуйте ритму музыки для достижения спокойствия. " +
                         "Снижайте уровень стресса через ритмичные действия."
        ),
        TherapeuticGameInfo(
            type = TherapeuticGameType.MEMORY_ISLAND,
            title = "Остров воспоминаний",
            description = "Стройте остров из приятных воспоминаний. " +
                         "Активируйте позитивные эмоции и воспоминания."
        ),
        TherapeuticGameInfo(
            type = TherapeuticGameType.THOUGHT_BALANCE,
            title = "Баланс мыслей",
            description = "Удерживайте баланс между позитивными и негативными мыслями. " +
                         "Тренируйте эмоциональную стабильность."
        ),
        TherapeuticGameInfo(
            type = TherapeuticGameType.COLOR_THERAPY,
            title = "Цветотерапия",
            description = "Раскрашивайте мандалы успокаивающими цветами. " +
                         "Снижайте тревогу через визуальную терапию."
        ),
        TherapeuticGameInfo(
            type = TherapeuticGameType.KINDNESS_QUEST,
            title = "Квест доброты",
            description = "Выполняйте задания, направленные на помощь другим. " +
                         "Развивайте эмпатию и социальные навыки."
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Терапевтические игры") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(games) { game ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onGameSelected(game.type) }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = game.icon,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        
                        Spacer(modifier = Modifier.width(16.dp))
                        
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = game.title,
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            
                            Spacer(modifier = Modifier.height(4.dp))
                            
                            Text(
                                text = game.description,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Start
                            )
                        }
                    }
                }
            }
        }
    }
} 