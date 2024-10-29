package com.kote.numfacts.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kote.numfacts.model.NumberFact
import com.kote.numfacts.ui.screens.MainScreen
import com.kote.numfacts.ui.screens.MainScreenDestination
import com.kote.numfacts.ui.screens.SecondaryScreen
import com.kote.numfacts.ui.screens.SecondaryScreenDestination

interface NavigationDestination {
    val rout: String
    val titleRes: Int
}

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = MainScreenDestination.rout
    ) {
        composable(route = MainScreenDestination.rout) {
            MainScreen(
                navigateToDetails = {
                    navController.navigate("${SecondaryScreenDestination.rout}/$it")
                }
            )
        }

        composable(
            route = SecondaryScreenDestination.routeWithArgs,
            arguments = listOf(navArgument(SecondaryScreenDestination.itemIdArg){
                type = NavType.IntType
            })
        ) {
            SecondaryScreen(
                onNavigateUp = {
                    navController.navigateUp()
                }
            )
        }
    }
}