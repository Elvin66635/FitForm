# FitForm 🏋️‍♂️

Android fitness application with AI-powered pose detection and workout tracking.

## About

FitForm is an intelligent fitness app that uses machine learning to analyze your posture during workouts. The app helps you:
- ✅ Monitor correct exercise execution
- 📊 Track workout progress
- 📅 Plan training programs
- 🎯 Achieve fitness goals

## Technologies

- **Kotlin** - primary development language
- **Jetpack Compose** - modern UI toolkit
- **Firebase Authentication** - user authentication
- **Cloud Firestore** - cloud database for syncing progress
- **Room Database** - local data storage
- **CameraX** - camera integration
- **ML Kit Pose Detection** - AI-powered pose analysis
- **Koin** - dependency injection
- **Navigation Component** - app navigation
- **Clean Architecture** - architectural pattern

## Features

### 🏠 Home Screen
- Quick access to workouts
- Progress statistics
- Motivational goals

### 💪 Workouts
- Real-time AI pose analysis
- Repetition counting
- Technique feedback
- Support for various exercises

### 📅 Calendar
- Workout planning
- Session history
- Progress visualization

### 📋 Training Programs
- Pre-built workout programs
- Custom program creation
- Adaptation to fitness level

### 🔍 Exercise Library
- Exercise catalog with descriptions
- Video instructions
- Search and filtering

### 🔐 Authentication & Sync
- Email/Password authentication
- Google Sign In (optional)
- Cloud sync of workout progress
- Cross-device data synchronization

### 🏆 Achievements System
- Unlock achievements for milestones
- Track progress towards goals
- Gamification elements
- Progress visualization

## Project Structure

```
FitForm/
├── app/src/main/java/com/fitform/ai/
│   ├── data/          # Data layer (Room, Repository)
│   ├── domain/        # Domain layer (Use Cases, Models)
│   ├── ui/            # Presentation layer (Compose UI, ViewModels)
│   ├── di/            # Dependency Injection (Koin)
│   ├── FitFormApp.kt  # Application class
│   └── MainActivity.kt
├── build.gradle.kts
└── settings.gradle.kts
```

### Architecture

The project follows **Clean Architecture** principles:
- **Data Layer**: Repository, DAO, Entity
- **Domain Layer**: Use Cases, Business Models, Repository Interfaces
- **Presentation Layer**: ViewModels, UI (Jetpack Compose)

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/Elvin66635/FitForm.git
   ```

2. Open the project in Android Studio

3. **Setup Firebase** (Required):
   - Create a Firebase project at [Firebase Console](https://console.firebase.google.com/)
   - Add Android app with package name: `com.fitform.ai`
   - Download `google-services.json` and place it in `app/` folder
   - Enable Authentication (Email/Password)
   - Enable Cloud Firestore Database
   - See [FIREBASE_SETUP.md](FIREBASE_SETUP.md) for detailed instructions

4. Sync Gradle

5. Run the app on an emulator or real device

## Requirements

- **Min SDK**: 26 (Android 8.0)
- **Target SDK**: 35 (Android 15)
- **Kotlin**: 1.9.22
- **Gradle**: 8.2.2
- **Android Studio**: Hedgehog (2023.1.1) or newer
- **Camera**: Required for AI pose analysis

## Development

### Package Structure

- `data/` - data layer (Room, Repository)
  - `local/` - local database
  - `repository/` - repository implementations
- `domain/` - business logic
  - `model/` - business models
  - `repository/` - repository interfaces
  - `usecase/` - use cases
- `ui/` - UI layer
  - `screens/` - app screens
  - `navigation/` - navigation
  - `theme/` - themes and styles
- `di/` - dependency injection

### Running the Project

1. Open the project in Android Studio
2. Wait for Gradle sync
3. Select a device/emulator
4. Click Run ▶️

## Author

**Elvin Ibragimov**
- Email: elvin.ibragimov.9757@gmail.com
- GitHub: [@Elvin66635](https://github.com/Elvin66635)

## License

This project is created for educational purposes.
