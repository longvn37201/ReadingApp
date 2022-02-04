package vulong.book_app.model.remote_api

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Read(
    val book: Book,
    var currentChapter: Int,
) : Parcelable
