package com.fitform.ai.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.fitform.ai.ui.screens.calendar.CalendarScreen
import com.fitform.ai.ui.screens.exercise.ExerciseListScreen
import com.fitform.ai.ui.screens.home.HomeScreen
import com.fitform.ai.ui.screens.programs.ProgramDetailScreen
import com.fitform.ai.ui.screens.programs.ProgramsScreen
import com.fitform.ai.ui.screens.result.ResultScreen
import com.fitform.ai.ui.screens.settings.SettingsScreen
import com.fitform.ai.ui.screens.workout.WorkoutScreen
import com.fitform.ai.ui.theme.Background
import com.fitform.ai.ui.theme.Primary
import com.fitform.ai.ui.theme.TextSecondary

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Exercises : Screen("exercises")
    object Programs : Screen("programs")
    object ProgramDetail : Screen("program/{programId}") {
        fun createRoute(programId: String) = "program/$programId"
    }
    object Calendar : Screen("calendar")
    object Settings : Screen("settings")
    object Workout : Screen("workout/{exerciseId}") {
        fun createRoute(exerciseId: String) = "workout/$exerciseId"
    }
    object Result : Screen("result/{score}/{reps}/{calories}/{duration}") {
        fun createRoute(score: Int, reps: Int, calories: Int, duration: Int) = 
            "result/$score/$reps/$calories/$duration"
    }
}

data class BottomNavItem(
    val title: String,
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

fun getBottomNavItems(context: android.content.Context): List<BottomNavItem> = listOf(
    BottomNavItem(context.getString(com.fitform.ai.R.string.nav_home), Screen.Home.route, Icons.Filled.Home, Icons.Outlined.Home),
    BottomNavItem(context.getString(com.fitform.ai.R.string.nav_exercises), Screen.Exercises.route, Icons.Filled.FitnessCenter, Icons.Outlined.FitnessCenter),
    BottomNavItem(context.getString(com.fitform.ai.R.string.nav_programs), Screen.Programs.route, Icons.Filled.List, Icons.Outlined.List),
    BottomNavItem(context.getString(com.fitform.ai.R.string.nav_calendar), Screen.Calendar.route, Icons.Filled.CalendarMonth, Icons.Outlined.CalendarMonth),
    BottomNavItem(context.getString(com.fitform.ai.R.string.nav_settings), Screen.Settings.route, Icons.Filled.MoreHoriz, Icons.Outlined.MoreHoriz)
)

private val routesWithoutBottomBar = listOf(
    "workout/",
    "result/"
)

@Composable
fun FitFormNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val showBottomBar = routesWithoutBottomBar.none { currentRoute?.startsWith(it) == true }
    
    Scaffold(
        containerColor = Background,
        bottomBar = {
            if (showBottomBar) {
                FitFormBottomBar(navController = navController)
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(paddingValues),
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(300)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(300)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(300)
                )
            }
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    onNavigateToExercises = {
                        navController.navigate(Screen.Exercises.route)
                    },
                    onNavigateToPrograms = {
                        navController.navigate(Screen.Programs.route)
                    },
                    onStartWorkout = { exerciseId ->
                        navController.navigate(Screen.Workout.createRoute(exerciseId))
                    }
                )
            }
            
            composable(Screen.Exercises.route) {
                ExerciseListScreen(
                    onExerciseClick = { exerciseId ->
                        navController.navigate(Screen.Workout.createRoute(exerciseId))
                    },
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
            
            composable(Screen.Programs.route) {
                ProgramsScreen(
                    onProgramClick = { programId ->
                        navController.navigate(Screen.ProgramDetail.createRoute(programId))
                    },
                    onCreateProgram = { }
                )
            }
            
            composable(
                route = Screen.ProgramDetail.route,
                arguments = listOf(
                    navArgument("programId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val programId = backStackEntry.arguments?.getString("programId") ?: ""
                ProgramDetailScreen(
                    programId = programId,
                    onStartWorkout = { exerciseId ->
                        navController.navigate(Screen.Workout.createRoute(exerciseId))
                    },
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
            
            composable(Screen.Calendar.route) {
                CalendarScreen()
            }
            
            composable(Screen.Settings.route) {
                SettingsScreen()
            }
            composable(
                route = Screen.Workout.route,
                arguments = listOf(
                    navArgument("exerciseId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val exerciseId = backStackEntry.arguments?.getString("exerciseId") ?: "squat"
                WorkoutScreen(
                    exerciseId = exerciseId,
                    onFinish = { score, reps, calories, duration ->
                        navController.navigate(Screen.Result.createRoute(score, reps, calories, duration)) {
                            popUpTo(Screen.Workout.route) { inclusive = true }
                        }
                    },
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
            
            composable(
                route = Screen.Result.route,
                arguments = listOf(
                    navArgument("score") { type = NavType.IntType },
                    navArgument("reps") { type = NavType.IntType },
                    navArgument("calories") { type = NavType.IntType },
                    navArgument("duration") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                ResultScreen(
                    score = backStackEntry.arguments?.getInt("score") ?: 0,
                    reps = backStackEntry.arguments?.getInt("reps") ?: 0,
                    calories = backStackEntry.arguments?.getInt("calories") ?: 0,
                    duration = backStackEntry.arguments?.getInt("duration") ?: 0,
                    onDone = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    },
                    onRepeat = { exerciseId ->
                        navController.navigate(Screen.Workout.createRoute(exerciseId)) {
                            popUpTo(Screen.Home.route)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun FitFormBottomBar(navController: NavHostController) {
    val context = LocalContext.current
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val bottomNavItems = remember(context) { getBottomNavItems(context) }
    
    NavigationBar(
        containerColor = Background,
        contentColor = TextSecondary
    ) {
        bottomNavItems.forEach { item ->
            val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true
            
            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.title,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = { 
                    Text(
                        text = item.title,
                        fontSize = 11.sp,
                        maxLines = 1
                    ) 
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Primary,
                    selectedTextColor = Primary,
                    unselectedIconColor = TextSecondary,
                    unselectedTextColor = TextSecondary,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

