package com.dev.coffeejournal.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dev.coffeejournal.ui.AppBar
import com.dev.coffeejournal.data.models.CoffeeViewModel
import com.dev.coffeejournal.data.models.RecipeViewModel
import com.dev.coffeejournal.R
import com.dev.coffeejournal.data.databases.Coffee
import com.dev.coffeejournal.data.databases.CoffeeDatabase
import com.dev.coffeejournal.data.databases.Recipe
import com.dev.coffeejournal.data.databases.RecipeDatabase
import com.dev.coffeejournal.data.models.CoffeeViewModelFactory
import com.dev.coffeejournal.data.models.RecipeViewModelFactory
import com.dev.coffeejournal.ui.theme.CoffeeJournalTheme
import com.dev.coffeejournal.ui.NewCoffeeForm

/**
 * Activity for recording a new coffee brew.
 *
 * This activity allows the user to record a new brew, including selecting an existing coffee
 * or adding a new one.
 */
class RecordActivity : ComponentActivity() {
    private val coffeeViewModel: CoffeeViewModel by viewModels {
        CoffeeViewModelFactory(
            CoffeeDatabase.getDatabase(applicationContext).coffeeDao()
        )
    }

    private val recipeViewModel: RecipeViewModel by viewModels {
        RecipeViewModelFactory(RecipeDatabase.getDatabase(applicationContext).recipeDao())
    }
    
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CoffeeJournalTheme {
                var showAddCoffeeSheet by remember { mutableStateOf(false) }
                val addSheetState = rememberModalBottomSheetState()

                var newCoffeeSelection by remember { mutableStateOf<Coffee?>(null) }

                Scaffold(modifier = Modifier.fillMaxSize(),
                    topBar = {
                        AppBar(
                            "Record a Brew",
                            canNavigateBack = true,
                            navigateUp = { finish() })
                    }/*, floatingActionButton = {
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
                    }*/
                ) { innerPadding ->
                    RecordPage(
                        coffeeViewModel, recipeViewModel,
                        newlyAddedCoffee = newCoffeeSelection,
                        onNewCoffeeClick = { showAddCoffeeSheet = true },
                        onNewCoffeeHandled = { newCoffeeSelection = null },
                        onNewBrewRecorded = { finish() },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                if (showAddCoffeeSheet) {
                    ModalBottomSheet(
                        onDismissRequest = { showAddCoffeeSheet = false },
                        sheetState = addSheetState
                    ) {
                        NewCoffeeForm(onAddCoffee = { newCoffee ->
                            coffeeViewModel.addCoffee(newCoffee)
                            showAddCoffeeSheet = false
                            newCoffeeSelection = newCoffee
                        },
                            modifier = Modifier.imePadding())
                    }

                }
            }
        }
    }
}

/**
 * Composable that displays the page for recording a new coffee brew.
 *
 * @param coffeeViewModel The [CoffeeViewModel] for accessing coffee data.
 * @param newlyAddedCoffee A newly added coffee to be pre-selected.
 * @param onNewCoffeeHandled Callback to be invoked when the newly added coffee has been handled.
 * @param onNewCoffeeClick Callback to be invoked when the user clicks on "Add a new Coffee".
 * @param modifier The modifier to be applied to the composable.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordPage(coffeeViewModel: CoffeeViewModel, recipeViewModel: RecipeViewModel,
               newlyAddedCoffee: Coffee?, onNewCoffeeHandled: () -> Unit,
               onNewCoffeeClick: () -> Unit, onNewBrewRecorded: () -> Unit,
               modifier: Modifier = Modifier) {
    val coffees by coffeeViewModel.allCoffees.collectAsState(initial = emptyList())
    var expandedCoffees by remember { mutableStateOf(false) }
    var selectedCoffee by remember { mutableStateOf<Coffee?>(null) }

    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(selectedCoffee) {
        if (selectedCoffee != null) {
            searchQuery = selectedCoffee?.name ?: ""
        }
    }

    val filteredCoffees = remember(searchQuery, coffees) {
        if (searchQuery.isBlank()) {
            coffees
        } else {
            coffees.filter { coffee ->
                coffee.name.contains(searchQuery, ignoreCase = true)
                }
        }
    }

    LaunchedEffect(newlyAddedCoffee) {
        if (newlyAddedCoffee != null) {
            selectedCoffee = newlyAddedCoffee
            onNewCoffeeHandled()
        }
    }
    
    val methods = listOf("Espresso", "Chemex", "V60", "AeroPress", "French Press", "Batch Brew", "Siphon")
    var expandedMethods by remember { mutableStateOf(false) }
    var selectedMethod by remember { mutableStateOf<String?>(null) }

    val textFieldColors = TextFieldDefaults.colors(
        focusedLabelColor = MaterialTheme.colorScheme.background,
        unfocusedLabelColor = MaterialTheme.colorScheme.background,
        focusedTextColor = MaterialTheme.colorScheme.background,
        unfocusedTextColor = MaterialTheme.colorScheme.background,
        focusedContainerColor = MaterialTheme.colorScheme.tertiary,
        unfocusedContainerColor = MaterialTheme.colorScheme.tertiary,
        cursorColor = MaterialTheme.colorScheme.primary
    )

    var dose by remember { mutableFloatStateOf(0f) }
    var yield by remember { mutableFloatStateOf(0f) }
    var extractionTime by remember { mutableStateOf("") }
    var tds by remember { mutableFloatStateOf(0f) }
    
    var sweetness by remember { mutableFloatStateOf(0f) }
    var body by remember { mutableFloatStateOf(0f) }
    var acidity by remember { mutableFloatStateOf(0f) }
    var chocolate by remember { mutableFloatStateOf(0f) }
    var fruity by remember { mutableFloatStateOf(0f) }
    var floral by remember { mutableFloatStateOf(0f) }
    var aroma by remember { mutableFloatStateOf(0f) }
    var overall by remember { mutableFloatStateOf(0f) }

    Box (modifier = modifier.fillMaxSize()
        .background(MaterialTheme.colorScheme.background)) {
//        Image (
//            painter = painterResource(id = R.drawable.coffee_beans_background),
//            contentDescription = "Background",
//            contentScale = ContentScale.Crop,
//            modifier = Modifier.fillMaxSize()
//        )

        Column (
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {

            Text(text = "Basic Information", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.tertiary)
            // Coffee selection
            ExposedDropdownMenuBox(
                expanded = expandedCoffees,
                onExpandedChange = { expandedCoffees = it }
            ) {
                TextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        if (it != selectedCoffee?.name) {
                            selectedCoffee = null
                        }
                        expandedCoffees = true
                    },
                    label = { Text("Select or search for a Coffee") },
                    readOnly = false,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCoffees) },
                    colors = textFieldColors,
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expandedCoffees,
                    onDismissRequest = { expandedCoffees = false }
                ) {
                    filteredCoffees.forEach { coffee ->
                        DropdownMenuItem(
                            text = { Text(text = coffee.name) },
                            onClick = {
                                selectedCoffee = coffee
                                expandedCoffees = false
                            }
                        )
                    }
                    DropdownMenuItem(
                        text = { Text(text = "Add a new Coffee") },
                        onClick = {
                            expandedCoffees = false
                            onNewCoffeeClick()
                        }
                    )
                }
            }

            // Brew method selection
            ExposedDropdownMenuBox(
                expanded = expandedMethods,
                onExpandedChange = { expandedMethods = it }
            ) {
                TextField(
                    value = selectedMethod ?: "",
                    onValueChange = { selectedMethod = it },
                    label = { Text("Select a Brew Method") },
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMethods) },
                    colors = textFieldColors,
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expandedMethods,
                    onDismissRequest = { expandedMethods = false }
                ) {
                    methods.forEach { method ->
                        DropdownMenuItem(
                            text = { Text(text = method) },
                            onClick = {
                                selectedMethod = method
                                expandedMethods = false
                            }
                        )
                    }
                }
            }

            // Recipe information
            Text(text = "Recipe Details", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.tertiary)

            Row (modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                TextField(
                    value = if (dose == 0f) "" else dose.toString(),
                    onValueChange = {
                        try {
                            dose = it.toFloat()
                        } catch (e: NumberFormatException) {
                            println("Invalid dose input: $it")
                        }},
                    label = { Text("Dose (g)") },
                    colors = textFieldColors,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
                TextField(
                    value = if (yield == 0f) "" else yield.toString(),
                    onValueChange = {
                        try {
                            yield = it.toFloat()
                        } catch (e: NumberFormatException) {
                            println("Invalid yield input: $it")
                        }},
                    label = { Text("Yield (g)") },
                    colors = textFieldColors,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
            }

            Row (modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                TextField(
                    value = extractionTime,
                    onValueChange = {extractionTime = it},
                    label = { Text("Extraction Time (min-s)") },
                    colors = textFieldColors,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
                TextField(
                    value = if (tds == 0f) "" else tds.toString(),
                    onValueChange = {
                        try {
                            tds = it.toFloat()
                        } catch (e: NumberFormatException) {
                            println("Invalid TDS input: $it")
                        } },
                    label = { Text("TDS") },
                    colors = textFieldColors,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
            }
            
            // Ratings information : coffee wheel
            Text(text = "Taste Ratings", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.tertiary)

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                RatingSlider(label = "Sweetness", value = sweetness, onValueChange = { sweetness = it })
                RatingSlider(label = "Body", value = body, onValueChange = { body = it })
                RatingSlider(label = "Acidity", value = acidity, onValueChange = { acidity = it })
                RatingSlider(label = "Chocolate", value = chocolate, onValueChange = { chocolate = it })
                RatingSlider(label = "Fruity", value = fruity, onValueChange = { fruity = it })
                RatingSlider(label = "Floral", value = floral, onValueChange = { floral = it })
                RatingSlider(label = "Aroma", value = aroma, onValueChange = { aroma = it })
                RatingSlider(label = "Overall", value = overall, onValueChange = { overall = it })
            }

            // Save button
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                FloatingActionButton(
                    onClick = {
                        val newRecipe = Recipe(
                            coffeeId = selectedCoffee?.id ?: 0,
                            brewMethod = selectedMethod ?: "",
                            dose = dose,
                            yield = yield,
                            extractionTime = extractionTime,
                            tds = tds,
                            coffeeWheelScoreSweetness = sweetness.toInt(),
                            coffeeWheelScoreBody = body.toInt(),
                            coffeeWheelScoreAcidity = acidity.toInt(),
                            coffeeWheelScoreChocolate = chocolate.toInt(),
                            coffeeWheelScoreFruity = fruity.toInt(),
                            coffeeWheelScoreFloral = floral.toInt(),
                            coffeeWheelScoreAroma = aroma.toInt(),
                            coffeeWheelScoreOverall = overall.toInt()
                        )

                        recipeViewModel.addRecipe(newRecipe)
                        onNewBrewRecorded()
                    },
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.primary,
                    shape = CircleShape,
                    content = {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Add Button"
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun RatingSlider(label: String, value: Float, onValueChange: (Float) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.width(85.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = 0f..10f,
            steps = 9,
            modifier = Modifier.weight(1f),
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.primaryContainer,
                activeTrackColor = MaterialTheme.colorScheme.tertiary,
                inactiveTrackColor = MaterialTheme.colorScheme.tertiary,
                activeTickColor = MaterialTheme.colorScheme.primaryContainer,
                inactiveTickColor = MaterialTheme.colorScheme.primary

            )
        )
    }
}
