package vulong.book_app.ui.book_detail_screen

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import vulong.book_app.local_db.BookRecentDatabase
import vulong.book_app.model.local_db.ReadBookProgress
import vulong.book_app.model.remote_api.Book
import vulong.book_app.util.model.State

class BookDetailViewModel() : ViewModel() {

    var currentBook: Book? = null
    var readBookProcess: ReadBookProgress? = null
    var readBookProcessState = MutableLiveData<State>()

    fun getCurrentBookProgress(context: Context) {
        viewModelScope.launch {
            readBookProcessState.value = State.Loading
            readBookProcess =
                BookRecentDatabase.getInstance(context)
                    .bookRecentDAO()
                    .getBookProgress(currentBook!!.id)
            readBookProcessState.value = State.Success
        }
    }

}