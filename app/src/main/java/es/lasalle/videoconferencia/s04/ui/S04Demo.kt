package es.lasalle.videoconferencia.s04.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import es.lasalle.videoconferencia.s04.ui.screens.CharacterDetailScreen
import es.lasalle.videoconferencia.s04.ui.screens.CharacterListScreen

sealed class S04Routes(val route: String) {
    object CharacterList : S04Routes("character_list")
    object CharacterDetail : S04Routes("character_detail/{characterId}") {
        fun createRoute(characterId: Int) = "character_detail/$characterId"
    }
}

@Composable
fun S04Demo(
    onNavigateBack: () -> Unit
) {
    val navController = rememberNavController()
    
    S04Navigation(
        navController = navController,
        onNavigateBack = onNavigateBack
    )
}

@Composable
private fun S04Navigation(
    navController: NavHostController,
    onNavigateBack: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = S04Routes.CharacterList.route
    ) {
        composable(S04Routes.CharacterList.route) {
            S04CharacterListWrapper(
                onNavigateBack = onNavigateBack,
                onNavigateToDetail = { characterId ->
                    navController.navigate(S04Routes.CharacterDetail.createRoute(characterId))
                }
            )
        }
        
        composable(
            route = S04Routes.CharacterDetail.route,
            arguments = listOf(
                navArgument("characterId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val characterId = backStackEntry.arguments?.getInt("characterId") ?: 0

            CharacterDetailScreen(
                characterId = characterId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun S04CharacterListWrapper(
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("S04 - Retrofit + Room")
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            CharacterListScreen(
                onCharacterClick = { characterId ->
                    onNavigateToDetail(characterId)
                }
            )
        }
    }
}