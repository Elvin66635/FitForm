package com.fitform.ai.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import org.koin.compose.getKoin
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitform.ai.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onSignOut: () -> Unit = {}
) {
    var soundEnabled by remember { mutableStateOf(true) }
    var vibrationEnabled by remember { mutableStateOf(true) }
    var notificationsEnabled by remember { mutableStateOf(true) }
    
    val authRepository: com.fitform.ai.domain.repository.IAuthRepository = 
        org.koin.compose.getKoin().get()
    val currentUser by authRepository.currentUser.collectAsState(initial = null)
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        // Top Bar
        TopAppBar(
            title = {
                Text(
                    text = "Настройки",
                    fontWeight = FontWeight.Bold
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Background,
                titleContentColor = TextPrimary
            )
        )
        
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Profile Section
            item {
                ProfileCard(
                    user = currentUser,
                    onSignOut = onSignOut
                )
            }
            
            // PRO Banner
            item {
                ProBanner()
            }
            
            // Settings Groups
            item {
                SettingsGroup(title = "Тренировки") {
                    SettingsToggleItem(
                        icon = Icons.Default.VolumeUp,
                        title = "Звуки",
                        subtitle = "Звуковые подсказки во время тренировки",
                        isChecked = soundEnabled,
                        onCheckedChange = { soundEnabled = it }
                    )
                    SettingsToggleItem(
                        icon = Icons.Default.Vibration,
                        title = "Вибрация",
                        subtitle = "Тактильная обратная связь",
                        isChecked = vibrationEnabled,
                        onCheckedChange = { vibrationEnabled = it }
                    )
                }
            }
            
            item {
                SettingsGroup(title = "Уведомления") {
                    SettingsToggleItem(
                        icon = Icons.Default.Notifications,
                        title = "Push-уведомления",
                        subtitle = "Напоминания о тренировках",
                        isChecked = notificationsEnabled,
                        onCheckedChange = { notificationsEnabled = it }
                    )
                    SettingsClickableItem(
                        icon = Icons.Default.Schedule,
                        title = "Расписание напоминаний",
                        subtitle = "Настроить время уведомлений",
                        onClick = { /* Open schedule settings */ }
                    )
                }
            }
            
            item {
                SettingsGroup(title = "Данные") {
                    SettingsClickableItem(
                        icon = Icons.Default.BarChart,
                        title = "Статистика",
                        subtitle = "Подробная статистика тренировок",
                        onClick = { /* Open stats */ }
                    )
                    SettingsClickableItem(
                        icon = Icons.Default.CloudSync,
                        title = "Синхронизация",
                        subtitle = "Резервное копирование данных",
                        onClick = { /* Open sync settings */ }
                    )
                    SettingsClickableItem(
                        icon = Icons.Default.DeleteOutline,
                        title = "Очистить данные",
                        subtitle = "Удалить историю тренировок",
                        onClick = { /* Confirm and clear */ },
                        textColor = Error
                    )
                }
            }
            
            item {
                SettingsGroup(title = "О приложении") {
                    SettingsClickableItem(
                        icon = Icons.Default.Info,
                        title = "Версия",
                        subtitle = "1.0.0",
                        onClick = { }
                    )
                    SettingsClickableItem(
                        icon = Icons.Default.Policy,
                        title = "Политика конфиденциальности",
                        onClick = { /* Open privacy policy */ }
                    )
                    SettingsClickableItem(
                        icon = Icons.Default.Description,
                        title = "Условия использования",
                        onClick = { /* Open terms */ }
                    )
                    SettingsClickableItem(
                        icon = Icons.Default.Help,
                        title = "Поддержка",
                        subtitle = "Связаться с нами",
                        onClick = { /* Open support */ }
                    )
                }
            }
            
            // Footer
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "FitForm AI",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Primary
                        )
                        Text(
                            text = "Сделано с ❤️ для вашего здоровья",
                            fontSize = 12.sp,
                            color = TextHint
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileCard(
    user: com.fitform.ai.domain.model.User?,
    onSignOut: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Surface),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(Primary.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Primary,
                    modifier = Modifier.size(32.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = user?.displayName ?: user?.email ?: "Гость",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = if (user != null) user.email else "Войти для синхронизации",
                    fontSize = 14.sp,
                    color = TextSecondary
                )
            }
            
            if (user != null) {
                IconButton(onClick = onSignOut) {
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = "Выйти",
                        tint = Error
                    )
                }
            } else {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = TextSecondary
                )
            }
        }
    }
}

@Composable
private fun ProBanner() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        shape = RoundedCornerShape(20.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Primary, Accent)
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "⭐",
                            fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "FitForm PRO",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Разблокируйте все функции и программы",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
                
                Button(
                    onClick = { /* Open subscription */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Primary
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Купить",
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingsGroup(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextSecondary,
            modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
        )
        
        Card(
            colors = CardDefaults.cardColors(containerColor = Surface),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column {
                content()
            }
        }
    }
}

@Composable
private fun SettingsToggleItem(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!isChecked) }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Primary,
            modifier = Modifier.size(24.dp)
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 16.sp,
                color = TextPrimary
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    fontSize = 13.sp,
                    color = TextSecondary
                )
            }
        }
        
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Primary,
                uncheckedThumbColor = TextSecondary,
                uncheckedTrackColor = SurfaceLight
            )
        )
    }
}

@Composable
private fun SettingsClickableItem(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit,
    textColor: Color = TextPrimary
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (textColor == Error) Error else Primary,
            modifier = Modifier.size(24.dp)
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 16.sp,
                color = textColor
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    fontSize = 13.sp,
                    color = if (textColor == Error) Error.copy(alpha = 0.7f) else TextSecondary
                )
            }
        }
        
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = TextSecondary
        )
    }
}




