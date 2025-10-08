package com.dev.coffeejournal.data

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

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

@Dao
interface RecipeDao {
    @Insert
    suspend fun insert(recipe: Recipe)

    @Update
    suspend fun update(recipe: Recipe)

    @Delete
    suspend fun delete(recipe: Recipe)

    @Query("SELECT * FROM recipes")
    fun getAllRecipes(): Flow<List<Recipe>>
}

@Database(entities = [Recipe::class], version = 1, exportSchema = false)
abstract class RecipeDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao

    companion object {
        @Volatile
        private var INSTANCE: RecipeDatabase? = null

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