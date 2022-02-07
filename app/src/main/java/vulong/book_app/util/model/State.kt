package vulong.book_app.util.model

sealed class State {
    object Success : State()
    object Loading : State()
    class Error(val errorMessage: String) : State()
}
