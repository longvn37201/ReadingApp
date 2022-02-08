package vulong.book_app.ui.read_screen

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import vulong.book_app.local_db.BookRecentDatabase
import vulong.book_app.model.local_db.ReadBookProgress
import vulong.book_app.model.remote_api.Book

class ReadBookViewModel(
) : ViewModel() {

    val isShowSystemBar = MutableLiveData<Boolean>(false)
    var currentBookProcess: ReadBookProgress? = null
    var currentBook: Book? = null

    fun saveReadBookProgress(context: Context) {
        viewModelScope.launch {
            BookRecentDatabase.getInstance(context).bookRecentDAO().insert(currentBookProcess!!)
        }
    }

}