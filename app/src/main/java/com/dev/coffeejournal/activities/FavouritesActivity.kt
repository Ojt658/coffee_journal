package com.dev.coffeejournal.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.dev.coffeejournal.ux.AppBar
import com.dev.coffeejournal.R
import com.dev.coffeejournal.ui.theme.CoffeeJournalTheme

class FavouritesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CoffeeJournalTheme {
                Scaffold(modifier = Modifier.fillMaxSize(),
                    topBar = {
                        AppBar(
                            "Favourites",
                            canNavigateBack = true,
                            navigateUp = { finish() })
                    }
                ) { innerPadding ->
                    FavouritesPage(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun FavouritesPage(modifier: Modifier = Modifier) {
    Box (modifier = modifier.fillMaxSize()) {
        Image (
            painter = painterResource(id = R.drawable.coffee_beans_background),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}