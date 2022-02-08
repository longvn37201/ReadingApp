package vulong.book_app.local_db

import androidx.room.*
import vulong.book_app.model.local_db.ReadBookProgress

@Dao
interface ReadBookProgressDAO {
    @Query("SELECT * FROM ReadBookProgress ORDER BY time DESC")
    suspend fun getAllReadBookProgress(): List<ReadBookProgress>

    @Query("SELECT * FROM ReadBookProgress WHERE idBook=:id")
    suspend fun getBookProgress(id: String): ReadBookProgress?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(readBookProgress: ReadBookProgress)

    @Delete
    suspend fun delete(readBookProgress: ReadBookProgress)
}