package vulong.book_app.ui.read_screen

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import vulong.book_app.local_db.BookRecentDatabase
import vulong.book_app.model.local_db.ReadBookProgress
import vulong.book_app.model.remote_api.Book
import vulong.book_app.util.SharedPrefUtils
import vulong.book_app.util.model.State

class ReadBookViewModel : ViewModel() {

    var isAuToScroll = false

    var isDownload = false

    val isShowSystemBar = MutableLiveData<Boolean>(false)
    var currentBookProcess: ReadBookProgress? = null
    var currentBook: Book? = null

    var isFirstScrollToSaved = true

    val loadingChapterState = MutableLiveData<State>(State.None)

    fun getDownloadState(context: Context) {
        isDownload = SharedPrefUtils.getBooleanData(context, currentBook!!.id, false)
    }

    fun saveReadBookProgress(context: Context) {
        viewModelScope.launch {
            BookRecentDatabase.getInstance(context).bookRecentDAO().insert(currentBookProcess!!)
        }
    }

    fun saveReadBookProgress(context: Context, readBookProgress: ReadBookProgress) {
        viewModelScope.launch {
            BookRecentDatabase.getInstance(context).bookRecentDAO().insert(readBookProgress)
        }
    }

    fun deleteReadBookProgress(context: Context) {
        viewModelScope.launch {
            BookRecentDatabase.getInstance(context).bookRecentDAO().delete(currentBookProcess!!)
        }
    }


}