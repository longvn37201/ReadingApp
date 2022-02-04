package vulong.book_app.ui.read_screen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import vulong.book_app.model.remote_api.Chapters
import vulong.book_app.model.remote_api.Read
import vulong.book_app.repository.BookRepository
import vulong.book_app.util.State

class ReadBookViewModel(
    private val repository: BookRepository = BookRepository(),
) : ViewModel() {

    val currentRead = MutableLiveData<Read>()
    val state = MutableLiveData<State>()
    val chapters = MutableLiveData<Chapters>()
    val isShowSystemBar = MutableLiveData<Boolean>(false)

    fun getAllChapters() {
        viewModelScope.launch {
            state.value = State.Loading
            try {
                val response = repository.getAllChapters(
                    currentRead.value!!.book.publicSource,
                )
                if (response.isSuccessful) {
                    chapters.value = response.body()
                    state.value = State.Success
                } else {
                    state.value = State.Error("Lỗi tải chương")
                }
            } catch (e: Exception) {
                state.value = State.Error("Lỗi tải chương")
            }
        }
    }
}