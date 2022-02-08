package vulong.book_app.local_db


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import vulong.book_app.model.local_db.ReadBookProgress
import vulong.book_app.util.Constant.BOOK_RECENT_DB_NAME


@Database(entities = [ReadBookProgress::class], version = 1, exportSchema = false)
abstract class BookRecentDatabase : RoomDatabase() {

    abstract fun bookRecentDAO(): ReadBookProgressDAO

    companion object {
        @Volatile
        private var INSTANCE: BookRecentDatabase? = null

        fun getInstance(context: Context): BookRecentDatabase {
            if (INSTANCE != null) return INSTANCE!!
            synchronized(this) {
                INSTANCE = Room
                    .databaseBuilder(
                        context,
                        BookRecentDatabase::class.java,
                        BOOK_RECENT_DB_NAME
                    )
                    .build()
                return INSTANCE!!
            }
        }
    }

}