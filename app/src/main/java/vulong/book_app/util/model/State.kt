package vulong.book_app.util.model

sealed class State {
    object None : State()
    object Loading : State()
    object Success : State()
    class Error(val errorMessage: String) : State()
}
