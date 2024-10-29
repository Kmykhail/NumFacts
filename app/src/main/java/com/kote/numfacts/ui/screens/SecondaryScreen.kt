package com.kote.numfacts.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kote.numfacts.navigation.NavigationDestination
import com.kote.numfacts.ui.AppViewModelProvider
import com.kote.numfacts.ui.components.NumFactsTopBar

object SecondaryScreenDestination: NavigationDestination {
    override val rout = "secondary_screen"
    override val titleRes = 0
    const val itemIdArg = "itemIdArg"
    val routeWithArgs = "${rout}/{$itemIdArg}"
}

@Composable
fun SecondaryScreen(
    onNavigateUp: () -> Unit,
    secondaryViewModel: SecondaryViewModel = viewModel(factory = AppViewModelProvider.FACTORY),
    modifier: Modifier = Modifier
) {
    val numberFact by secondaryViewModel.numberFact.collectAsState()
    if (numberFact != null) {
        Scaffold(
            topBar = {
                NumFactsTopBar(
                    title = numberFact!!.number.toString(),
                    canNavigateBack = true,
                    navigateUp = onNavigateUp
                )
            }
        ) {innerPadding ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Text(text = numberFact!!.fact)
            }
        }
    }
}