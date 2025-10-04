package com.dev.coffeejournal.ux

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dev.coffeejournal.data.Coffee
import com.dev.coffeejournal.ui.CoffeeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(title: String, modifier: Modifier = Modifier, canNavigateBack: Boolean = false, navigateUp: () -> Unit = {}) {
    CenterAlignedTopAppBar(
        title = { Text(title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis)},
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back Button"
                    )
                }
            }
        }
    )
}

@Composable
fun CoffeeCardList(coffee: Coffee, modifier: Modifier = Modifier, onCoffeeSelected: (Coffee) -> Unit, viewModel: CoffeeViewModel?) {
    Box(modifier = modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp)
        .background(
            color = MaterialTheme.colorScheme.tertiary,
            shape = MaterialTheme.shapes.medium
        )
        .clickable { onCoffeeSelected(coffee) }) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "${ coffee.name } from ${ coffee.origin }",
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary)

            when {
                coffee.isFavorite ->
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Favorite",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(16.dp).size(32.dp).clickable {
                            viewModel!!.updateCoffee(coffee.copy(isFavorite = !coffee.isFavorite))
                        }
                    )
                !coffee.isFavorite ->
                    Icon(
                        imageVector = Icons.Filled.StarBorder,
                        contentDescription = "Not Favorite",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(16.dp).size(32.dp)
                            .clickable {
                                viewModel!!.updateCoffee(coffee.copy(isFavorite = !coffee.isFavorite))
                            }
                    )
            }
        }

    }
}

@Composable
fun CoffeeCardSheet (coffee: Coffee, modifier: Modifier = Modifier) {
    Column(modifier = modifier
        .fillMaxWidth()
        .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = coffee.name,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(16.dp))
        Text(text = "Origin: ${ coffee.origin }",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(16.dp))
        Text(text = "Process: ${ coffee.process }",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(16.dp))
        Text(text = "Roast Profile: ${ coffee.roastProfile }",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(16.dp))
        Text(text = "Flavour Profile: ${ coffee.flavourProfile }",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(16.dp))
    }
}
