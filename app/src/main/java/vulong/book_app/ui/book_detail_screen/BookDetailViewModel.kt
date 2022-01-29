package vulong.book_app.ui.book_detail_screen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import vulong.book_app.model.remote_api.Book
import vulong.book_app.repository.BookRepository
import vulong.book_app.util.State

class BookDetailViewModel(
    private val repository: BookRepository = BookRepository(),
) : ViewModel() {

    val currentBook = MutableLiveData<Book>()
    val textDescription = MutableLiveData<String?>(null)
    val state = MutableLiveData<State>(State.Loading)

    fun getTextDescription() {
        viewModelScope.launch {
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