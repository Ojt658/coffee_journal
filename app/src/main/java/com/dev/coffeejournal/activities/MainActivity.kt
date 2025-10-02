package com.dev.coffeejournal.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev.coffeejournal.ux.AppBar
import com.dev.coffeejournal.R
import com.dev.coffeejournal.ui.theme.CoffeeJournalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CoffeeJournalTheme {
                Scaffold(modifier = Modifier.fillMaxSize(),
                        topBar = { AppBar("Coffee Journal") }
                ) { innerPadding ->
                    HomePage(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun HomePage(name: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Box (modifier = modifier.fillMaxSize()) {
        Image (
            painter = painterResource(id = R.drawable.coffee_beans_background),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column (modifier = Modifier.padding(32.0.dp),
            verticalArrangement = Arrangement.spacedBy(16.0.dp)) {
            // Record a brew row
            Row() {
                Button(modifier = Modifier
                    .fillMaxWidth()
                    .height(128.dp),
                    onClick = {
                        val intent = Intent(context, RecordActivity::class.java)
                        context.startActivity(intent)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.primary
                    )) {
                    Row (verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.0.dp)) {
                        Icon(imageVector = Icons.Filled.Coffee,
                            contentDescription = "Record a Brew")
                        Text(text = "Record a Brew")
                    }
                }
            }
            //Favourites row
            Row() {
                Button(modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 64.dp),
                    onClick = {
                        val intent = Intent(context, FavouritesActivity::class.java)
                        context.startActivity(intent)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.primary
                    )) {
                    Row (verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.0.dp)) {
                        Icon(imageVector = Icons.Filled.Star,
                            contentDescription = "Favourites")
                        Text(text = "Favourites")
                    }
                }
            }
            //Add Coffee info row
            Row() {
                Button(modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 64.dp),
                    onClick = {
                        val intent = Intent(context, CoffeeInfoActivity::class.java)
                        context.startActivity(intent)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.primary
                    )) {
                    Row (verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.0.dp)) {
                        Icon(imageVector = Icons.Filled.Add,
                            contentDescription = "Add coffee info")
                        Text(text = "Add Coffee Info")
                    }
                }
            }
            // View Recipes and Flavour Wheel row
            Row (Modifier.height(IntrinsicSize.Min)
                .defaultMinSize(minHeight = 64.dp),
                horizontalArrangement = Arrangement.spacedBy(16.0.dp)) {
                Button(modifier = Modifier.weight(1f).fillMaxHeight(),
                    onClick = {
                        val intent = Intent(context, RecipesActivity::class.java)
                        context.startActivity(intent)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.primary
                    )) {
                    Row (verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.0.dp)) {
                        Icon(imageVector = Icons.Filled.Menu,
                            contentDescription = "View Recipes")
                        Text(text = "View Recipes")
                    }
                }

                Button(modifier = Modifier.weight(1f).fillMaxHeight(),
                    onClick = {
                        val intent = Intent(context, FlavourWheelActivity::class.java)
                        context.startActivity(intent)
                    }, colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.primary
                )) {
                    Row (verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.0.dp)) {
                        Icon(
                            imageVector = Icons.Filled.PieChart,
                            contentDescription = "View Flavour Wheel"
                        )
                        Text(text = "View Flavour Wheel")
                    }
                }
            }
        }

    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CoffeeJournalTheme {
        HomePage("Android")
    }
}