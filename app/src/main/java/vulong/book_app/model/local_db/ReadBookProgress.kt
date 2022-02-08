package vulong.book_app.model.local_db

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class ReadBookProgress(
    @PrimaryKey
    val idBook: String,
    var page: Int,
    var scrollY: Int,
    var time: Long? = null,
) : Parcelable

