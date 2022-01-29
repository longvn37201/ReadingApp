package vulong.book_app.util

sealed class State {
    object Success : State()
    object Loading : State()
    class Error(val errorMessage: String) : State()
}
