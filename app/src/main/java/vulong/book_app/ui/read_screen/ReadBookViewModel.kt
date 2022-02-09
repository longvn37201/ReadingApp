package vulong.book_app.ui.read_screen

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import vulong.book_app.local_db.BookRecentDatabase
import vulong.book_app.model.local_db.ReadBookProgress
import vulong.book_app.model.remote_api.Book
import vulong.book_app.model.remote_api.Chapters
import vulong.book_app.retrofit.RetrofitInstance
import vulong.book_app.util.model.State

class ReadBookViewModel(
) : ViewModel() {

    val isShowSystemBar = MutableLiveData<Boolean>(false)
    var currentBookProcess: ReadBookProgress? = null
    var currentBook: Book? = null

    val chaptersOfBook = MutableLiveData<Chapters?>()
    val chaptersOfBookLoadingState = MutableLiveData<State>(State.Loading)

    fun saveReadBookProgress(context: Context) {
        viewModelScope.launch {
            BookRecentDatabase.getInstance(context).bookRecentDAO().insert(currentBookProcess!!)
        }
    }

    fun deleteReadBookProgress(context: Context) {
        viewModelScope.launch {
            BookRecentDatabase.getInstance(context).bookRecentDAO().delete(currentBookProcess!!)
        }
    }

    fun getAllChapter() {
        viewModelScope.launch {
            chaptersOfBookLoadingState.value = State.Loading
            try {
                val response = RetrofitInstance.api.getAllChapter(currentBook!!.id)
                if (response.isSuccessful) {
                    chaptersOfBookLoadingState.value = State.Success
                    chaptersOfBook.value = response.body()
                } else {
                    chaptersOfBookLoadingState.value = State.Error("lỗi tải chương")
                    chaptersOfBook.value = null
                }
            } catch (e: Exception) {
                chaptersOfBookLoadingState.value = State.Error("lỗi tải chương")
                chaptersOfBook.value = null
            }
        }
    }

}