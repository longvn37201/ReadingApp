package vulong.book_app.util.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import vulong.book_app.model.remote_api.Book

@Parcelize
data class SavedStatus(
    val book: Book,
    var page: Int,
    var scrollY: Int,
) : Parcelable

