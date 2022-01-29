package vulong.book_app.ui.main_screen.bookshelf.all_book_screen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import vulong.book_app.model.remote_api.Book
import vulong.book_app.repository.BookRepository
import vulong.book_app.util.State
import vulong.book_app.util.State.*

class AllBookViewModel(
    private val repository: BookRepository = BookRepository(),
) : ViewModel() {

    val allBooks: MutableLiveData<ArrayList<Book>> = MutableLiveData(arrayListOf())
    val state: MutableLiveData<State> = MutableLiveData(Loading)

    init {
        getBooks()
    }

    fun getBooks() {
        viewModelScope.launch {
            state.value = Loading
            try {
                val response = repository.getAllBooks()
                if (response.isSuccessful) {
                    allBooks.value = response.body()
                    state.value = Success
                } else {
                    state.value = Error("Lỗi kết nối đến server")
                }
            } catch (e: Exception) {
                state.value = Error("Lỗi kết nối đến server")
            }
        }
    }

}