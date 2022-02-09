package vulong.book_app.model.remote_api

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Book(
    var id: String,
    var name: String,
    var author: String,
    var description: String,
    var imageUrl: String,
    var category: ArrayList<String>,
    var chapterNumber: Int,
) : Parcelable

@Parcelize
data class Chapters(
    var id: String,
    var listChapter: ArrayList<Chapter>,
) : Parcelable

@Parcelize
data class Chapter(
    var name: String,
    var content: String,
) : Parcelable

