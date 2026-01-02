# Настройка Firebase для FitForm AI

## Шаги для настройки Firebase:

### 1. Создание проекта Firebase

1. Перейдите на [Firebase Console](https://console.firebase.google.com/)
2. Нажмите "Add project" (Добавить проект)
3. Введите название проекта (например, "FitForm AI")
4. Следуйте инструкциям для создания проекта

### 2. Добавление Android приложения

1. В Firebase Console выберите ваш проект
2. Нажмите на иконку Android
3. Введите:
   - **Package name**: `com.fitform.ai`
   - **App nickname**: FitForm AI (опционально)
   - **Debug signing certificate SHA-1**: (опционально, для Google Sign In)
4. Нажмите "Register app"

### 3. Загрузка google-services.json

1. Скачайте файл `google-services.json`
2. Поместите его в папку `app/` проекта (на том же уровне, что и `build.gradle.kts`)
3. **Важно**: Не коммитьте этот файл в git, если он содержит реальные ключи

### 4. Настройка Firebase Authentication

1. В Firebase Console перейдите в **Authentication**
2. Нажмите "Get started"
3. Включите следующие методы входа:
   - **Email/Password**
   - **Google** (опционально, требует дополнительной настройки)

### 5. Настройка Cloud Firestore

1. В Firebase Console перейдите в **Firestore Database**
2. Нажмите "Create database"
3. Выберите режим:
   - **Production mode** (для продакшена)
   - **Test mode** (для разработки)
4. Выберите регион (например, `us-central1`)
5. Нажмите "Enable"

### 6. Правила безопасности Firestore

Для разработки можно использовать следующие правила:

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // Пользователи могут читать/писать только свои данные
    match /users/{userId} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
    }
    
    // Достижения доступны всем для чтения
    match /achievements/{achievementId} {
      allow read: if request.auth != null;
      allow write: if false; // Только через админ-панель
    }
    
    // Достижения пользователей
    match /user_achievements/{userAchievementId} {
      allow read, write: if request.auth != null && 
        resource.data.userId == request.auth.uid;
    }
    
    // Сессии тренировок
    match /workout_sessions/{sessionId} {
      allow read, write: if request.auth != null && 
        resource.data.userId == request.auth.uid;
    }
  }
}
```

### 7. Структура данных в Firestore

#### Коллекция `users`
```
users/{userId}
  - userId: string
  - weight: number (optional)
  - height: number (optional)
  - age: number (optional)
  - fitnessGoal: string (optional)
  - activityLevel: string (optional)
  - createdAt: timestamp
  - updatedAt: timestamp
```

#### Коллекция `achievements`
```
achievements/{achievementId}
  - id: string
  - title: string
  - description: string
  - icon: string
  - type: string
  - requirement: number
  - rarity: string
  - category: string
```

#### Коллекция `user_achievements`
```
user_achievements/{userId}_{achievementId}
  - achievementId: string
  - userId: string
  - unlockedAt: timestamp
  - progress: number
  - isUnlocked: boolean
```

#### Коллекция `workout_sessions`
```
workout_sessions/{sessionId}
  - id: string
  - userId: string
  - programId: string (optional)
  - workoutId: string (optional)
  - exerciseId: string (optional)
  - startTime: timestamp
  - endTime: timestamp (optional)
  - exerciseResults: array
  - totalScore: number
  - caloriesBurned: number
  - reps: number
  - duration: number
  - notes: string (optional)
```

### 8. Настройка Google Sign In (опционально)

1. В Firebase Console перейдите в **Authentication** > **Sign-in method**
2. Включите **Google**
3. Укажите email поддержки проекта
4. Сохраните изменения
5. Получите SHA-1 отпечаток:
   ```bash
   keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android
   ```
6. Добавьте SHA-1 в настройки Android приложения в Firebase Console

### 9. Тестирование

После настройки:
1. Запустите приложение
2. Попробуйте зарегистрироваться с email/password
3. Проверьте, что данные сохраняются в Firestore
4. Проверьте синхронизацию данных между устройствами

## Примечания

- Для продакшена обязательно настройте правила безопасности Firestore
- Не коммитьте `google-services.json` с реальными ключами в публичный репозиторий
- Используйте `.gitignore` для исключения `google-services.json` из git



