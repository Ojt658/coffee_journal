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
import com.dev.coffeejournal.ui.AppBar
import com.dev.coffeejournal.R
import com.dev.coffeejournal.ui.theme.CoffeeJournalTheme

/**
 * This activity displays the SCAA flavour wheel.
 */
class FlavourWheelActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CoffeeJournalTheme {
                Scaffold(modifier = Modifier.fillMaxSize(),
                    topBar = {
                        AppBar(
                            "Flavour Wheel",
                            canNavigateBack = true,
                            navigateUp = { finish() })
                    }
                ) { innerPadding ->
                    FlavourWheelPage(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

/**
 * This composable displays the main content of the Flavour Wheel screen.
 *
 * @param modifier The modifier to be applied to the composable.
 */
@Composable
fun FlavourWheelPage(modifier: Modifier = Modifier) {
    Box (modifier = modifier.fillMaxSize()) {
        Image (
            painter = painterResource(id = R.drawable.coffee_beans_background),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}