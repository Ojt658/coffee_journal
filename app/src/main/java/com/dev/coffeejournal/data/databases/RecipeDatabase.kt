package com.dev.coffeejournal.data.databases

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * Represents a single recipe entry in the database.
 *
 * @property id The unique identifier for the recipe.
 * @property coffeeId The ID of the coffee used in this recipe.
 * @property brewMethod The brew method used for this recipe.
 * @property dose The dose of coffee used, in grams.
 * @property yield The yield of the brew, in grams.
 * @property duration The duration of the brew, in seconds.
 * @property review A review of the recipe.
 * @property coffeeWheelScoreSweetness The sweetness score from the coffee wheel.
 * @property coffeeWheelScoreBody The body score from the coffee wheel.
 * @property coffeeWheelScoreAcidity The acidity score from the coffee wheel.
 * @property coffeeWheelScoreChocolate The chocolate score from the coffee wheel.
 * @property coffeeWheelScoreFruity The fruity score from the coffee wheel.
 * @property coffeeWheelScoreFloral The floral score from the coffee wheel.
 */
@Entity(tableName = "recipes")
data class Recipe (
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    var coffeeId: Int,
    var brewMethod: String,
    var dose: Int,
    var yield: Int,
    var duration: Int,
    var review: String,
    var coffeeWheelScoreSweetness: Int,
    var coffeeWheelScoreBody: Int,
    var coffeeWheelScoreAcidity: Int,
    var coffeeWheelScoreChocolate: Int,
    var coffeeWheelScoreFruity: Int,
    var coffeeWheelScoreFloral: Int
)

/**
 * Data Access Object (DAO) for the 'recipes' table.
 */
@Dao
interface RecipeDao {
    /**
     * Inserts a recipe into the database.
     * @param recipe The recipe to insert.
     */
    @Insert
    suspend fun insert(recipe: Recipe)

    /**
     * Updates an existing recipe in the database.
     * @param recipe The recipe to update.
     */
    @Update
    suspend fun update(recipe: Recipe)

    /**
     * Deletes a recipe from the database.
     * @param recipe The recipe to delete.
     */
    @Delete
    suspend fun delete(recipe: Recipe)

    /**
     * Retrieves all recipes from the database.
     * @return A [Flow] of a list of all recipes.
     */
    @Query("SELECT * FROM recipes")
    fun getAllRecipes(): Flow<List<Recipe>>
}

/**
 * The Room database for this app.
 */
@Database(entities = [Recipe::class], version = 1, exportSchema = false)
abstract class RecipeDatabase : RoomDatabase() {
    /**
     * Returns the DAO for the 'recipes' table.
     */
    abstract fun recipeDao(): RecipeDao

    companion object {
        @Volatile
        private var INSTANCE: RecipeDatabase? = null

        /**
         * Returns a singleton instance of the database.
         *
         * @param context The application context.
         * @return The singleton instance of the database.
         */
        fun getDatabase(context: Context): RecipeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RecipeDatabase::class.java,
                    "recipe_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}