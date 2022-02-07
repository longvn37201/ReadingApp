package vulong.book_app.ui.book_detail_screen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import vulong.book_app.model.remote_api.Book
import vulong.book_app.repository.BookRepository
import vulong.book_app.util.model.SavedStatus
import vulong.book_app.util.model.State

class BookDetailViewModel(
    private val repository: BookRepository = BookRepository(),
) : ViewModel() {

    val state = MutableLiveData<State>(State.Loading)

    val currentBook = MutableLiveData<Book>()
    val savedStatus = MutableLiveData<SavedStatus>()

    val textDescription = MutableLiveData<String?>(null)

    fun getTextDescription() {
        viewModelScope.launch {
            state.value = State.Loading
            try {
                val response = repository.getDescription(currentBook.value!!.publicSource)
                if (response.isSuccessful) {
                    val bookDescription = response.body()!!
                    textDescription.value = bookDescription.description
                    state.value = State.Success
                } else {
                    state.value = State.Error("Lỗi tải mô tả")
                }
            } catch (e: Exception) {
                state.value = State.Error("Lỗi tải mô tả")
            }
        }
    }
}