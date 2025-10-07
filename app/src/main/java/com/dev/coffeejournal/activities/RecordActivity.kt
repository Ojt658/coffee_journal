package com.dev.coffeejournal.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.dev.coffeejournal.ux.AppBar
import com.dev.coffeejournal.R
import com.dev.coffeejournal.ui.theme.CoffeeJournalTheme

class RecordActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CoffeeJournalTheme {
                Scaffold(modifier = Modifier.fillMaxSize(),
                    topBar = {
                        AppBar(
                            "Record a Brew",
                            canNavigateBack = true,
                            navigateUp = { finish() })
                    }, floatingActionButton = {
                        FloatingActionButton(
                            onClick = {},
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.primary,
                            shape = CircleShape,
                            modifier = Modifier.padding(16.dp),
                            content = {
                                Icon(
                                    imageVector = Icons.Filled.Add,
                                    contentDescription = "Add Button"
                                )
                            }
                        )
                    }
                ) { innerPadding ->
                    RecordPage(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordPage(modifier: Modifier = Modifier) {


    Box (modifier = modifier.fillMaxSize()) {
        Image (
            painter = painterResource(id = R.drawable.coffee_beans_background),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}