package com.kote.numfacts.ui.screens

import android.telecom.Call.Details
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kote.numfacts.ui.AppViewModelProvider
import com.kote.numfacts.ui.components.NumberField
import com.kote.numfacts.ui.theme.NumFactsTheme
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.kote.numfacts.R
import com.kote.numfacts.model.NumberFact
import com.kote.numfacts.navigation.NavigationDestination
import com.kote.numfacts.ui.components.NumFactsTopBar

object MainScreenDestination: NavigationDestination {
    override val rout = "main_screen"
    override val titleRes = R.string.app_name
}

@Composable
fun MainScreen(
    navigateToDetails: (Int) -> Unit,
    mainViewModel: MainViewModel = viewModel(factory = AppViewModelProvider.FACTORY),
    modifier: Modifier = Modifier
) {
    val uiState by mainViewModel.uiState.collectAsState()
    var number by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            NumFactsTopBar(title = stringResource(R.string.app_name),canNavigateBack = false)
        }
    ) { innerPadding ->
        println("InnerPadding is $innerPadding")
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 6.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            )
            {
                NumberField(
                    value = number,
                    onValueChanged = { number = it },
                    modifier = Modifier.weight(3f)
                )

                Button(
                    onClick = { mainViewModel.fetchNumberFact(number) },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = "Get fact")
                }

                Button(
                    onClick = { mainViewModel::fetchNumberFact },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(imageVector = Icons.Default.QuestionMark, contentDescription = "Random fact")
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(6.dp),) {
                items(uiState.historyRequestedFacts) {fact ->
                    HistoryRequestedFact(fact = fact,navigateToDetails = {navigateToDetails(fact.id)})
                }
            }
        }
    }
}

@Composable
private fun HistoryRequestedFact(
    fact: NumberFact,
    navigateToDetails: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier.clickable {
            navigateToDetails()
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Fact Icon",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = fact.number.toString(),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = fact.fact,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Justify,
                    maxLines = 1,
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(top = 2.dp)
                )
            }
        }
    }
}


@Preview
@Composable
fun HistoryRequestedFactPreview() {
    NumFactsTheme {
        HistoryRequestedFact(fact = NumberFact(number = 42, fact = "fact about number 42"), navigateToDetails = {})
    }
}