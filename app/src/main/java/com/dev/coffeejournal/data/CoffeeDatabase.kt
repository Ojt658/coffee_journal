package com.dev.coffeejournal.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Represents a single coffee entry in the database.
 *
 * @property id The unique identifier for the coffee.
 * @property name The name of the coffee.
 * @property origin The origin of the coffee beans.
 * @property process The processing method used for the coffee.
 * @property roastProfile The roast profile of the coffee.
 * @property flavourProfile A description of the coffee's flavour profile.
 * @property isFavorite Whether the coffee is marked as a favorite.
 */
@Entity(tableName = "coffees")
data class Coffee (
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    val name: String,
    val origin: String,
    val process: String,
    val roastProfile: String,
    val flavourProfile: String,
    val isFavorite: Boolean = false
)

/**
 * Data Access Object (DAO) for the 'coffees' table.
 */
@Dao
interface CoffeeDao {
    /**
     * Inserts a coffee into the database.
     * @param coffee The coffee to insert.
     */
    @Insert
    suspend fun insert(coffee: Coffee)

    /**
     * Updates an existing coffee in the database.
     * @param coffee The coffee to update.
     */
    @Update
    suspend fun update(coffee: Coffee)

    /**
     * Retrieves all coffees from the database, ordered by name.
     * @return A [Flow] of a list of all coffees.
     */
    @Query("SELECT * FROM coffees ORDER BY name ASC")
    fun getAllCoffees(): Flow<List<Coffee>>

    /**
     * Retrieves all favorite coffees from the database.
     * @return A [Flow] of a list of favorite coffees.
     */
    @Query("SELECT * FROM coffees WHERE isFavorite = 1")
    fun getAllFavourites(): Flow<List<Coffee>>

    /**
     * Retrieves all coffees from the database, ordered by origin.
     * @return A [Flow] of a list of all coffees.
     */
    @Query("SELECT * FROM coffees ORDER BY origin ASC")
    fun getAllCoffeesByOrigin(): Flow<List<Coffee>>

    /**
     * Retrieves all coffees from the database, ordered by process.
     * @return A [Flow] of a list of all coffees.
     */
    @Query("SELECT * FROM coffees ORDER BY process ASC")
    fun getAllCoffeesByProcess(): Flow<List<Coffee>>

    /**
     * Retrieves all coffees from the database, ordered by roast profile.
     * @return A [Flow] of a list of all coffees.
     */
    @Query("SELECT * FROM coffees ORDER BY roastProfile ASC")
    fun getAllCoffeesByRoastProfile(): Flow<List<Coffee>>

    /**
     * Retrieves a single coffee from the database by its ID.
     * @param id The ID of the coffee to retrieve.
     * @return A [Flow] of the coffee.
     */
    @Query("SELECT * FROM coffees WHERE id = :id")
    fun getCoffee(id: Int): Flow<Coffee>
}

/**
 * The Room database for this app.
 */
@Database(entities = [Coffee::class], version = 1, exportSchema = false)
abstract class CoffeeDatabase : RoomDatabase() {
    /**
     * Returns the DAO for the 'coffees' table.
     */
    abstract fun coffeeDao(): CoffeeDao

    companion object {
        @Volatile
        private var INSTANCE: CoffeeDatabase? = null

        /**
         * Returns a singleton instance of the database.
         *
         * @param context The application context.
         * @return The singleton instance of the database.
         */
        fun getDatabase(context: Context): CoffeeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CoffeeDatabase::class.java,
                    "coffee_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}