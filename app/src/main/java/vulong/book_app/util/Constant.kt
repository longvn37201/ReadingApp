package vulong.book_app.util

import vulong.book_app.R

object Constant {
    const val SHARED_PREFERENCE_NAME = "PREF"
    const val BOOK_RECENT_DB_NAME = "READ_BOOK_PROGRESS_DB"
    const val CATEGORY_FILE_NAME = "category.txt"
    const val SETTING_LIST_FILE_NAME = "settingList.txt"
    const val SHOW_WELCOME_SCREEN = "welcome_screen"
    const val DB_URL = "https://book-app-961f6-default-rtdb.asia-southeast1.firebasedatabase.app"
    const val SERVER_CLIENT_ID =
        "1058647916038-41ln1vrp3rjl0fdq6sclk7jgcbokq4dt.apps.googleusercontent.com"

//    const val BOOK_API_URL = "http://10.0.2.2:8080"
    const val BOOK_API_URL = "https://vulong-bookserver.herokuapp.com"

    val LIST_SETTING_ITEM by lazy {
        arrayOf(
            "Cài Đặt" to R.drawable.ic_setting,
            "Chính Sách" to R.drawable.ic_question_mark,
            "Thông Tin Ứng Dụng" to R.drawable.ic_exclamation_mark,
            "Đăng Xuất" to R.drawable.ic_logout,
        )
    }
}