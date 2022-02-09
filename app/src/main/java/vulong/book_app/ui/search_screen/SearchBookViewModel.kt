package vulong.book_app.ui.search_screen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import vulong.book_app.model.remote_api.Book
import vulong.book_app.retrofit.RetrofitInstance
import vulong.book_app.util.model.State

class SearchBookViewModel : ViewModel() {

    val listBook = MutableLiveData<ArrayList<Book>>(ArrayList<Book>())
    val loadingState = MutableLiveData<State>(State.None)
    var category: String? = null

    fun getBooksByCategory() {
        viewModelScope.launch {
            loadingState.value = State.Loading
            try {
                val response = RetrofitInstance.api.getBooksByCategory(category!!)
                if (response.isSuccessful) {
                    listBook.value = response.body()
                    loadingState.value = State.Success
                } else {
                    loadingState.value = State.Error("Lỗi kết nối")
                }
            } catch (e: Exception) {
                loadingState.value = State.Error("Lỗi kết nối")
            }
        }
    }

    fun searchBooks(query: String) {
        viewModelScope.launch {
            loadingState.value = State.Loading
            try {
                val response = RetrofitInstance.api.searchBooks(query)
                if (response.isSuccessful) {
                    listBook.value = response.body()
                    loadingState.value = State.Success
                } else {
                    loadingState.value = State.Error("Lỗi kết nối")
                }
            } catch (e: Exception) {
                loadingState.value = State.Error("Lỗi kết nối")
            }
        }
    }
}