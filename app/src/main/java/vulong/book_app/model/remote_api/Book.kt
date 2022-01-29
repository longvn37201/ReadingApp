package vulong.book_app.model.remote_api

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Book(
    val publicSource: String,
    val name: String,
    val author: String,
    val chapterNumber: Int,
    val category: ArrayList<String>,
) : Parcelable
