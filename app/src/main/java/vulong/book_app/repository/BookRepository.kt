package vulong.book_app.repository

import vulong.book_app.retrofit.RetrofitInstance

class BookRepository {

    suspend fun getAllBooks(
    ) = RetrofitInstance.api.getAllBooks()

    suspend fun getBooksByLimit(
        skip: String,
        take: String,
    ) = RetrofitInstance.api.getBooksByLimit(skip, take)

    suspend fun getBooksByCategory(
        category: String,
    ) = RetrofitInstance.api.getBooksByCategory(category)

    suspend fun getBook(
        publicSource: String,
    ) = RetrofitInstance.api.getBook(publicSource)

    suspend fun getDescription(
        publicSource: String,
    ) = RetrofitInstance.api.getDescription(publicSource)

    suspend fun getChapter(
        publicSource: String,
        chapterNumber: String,
    ) = RetrofitInstance.api.getChapter(publicSource, chapterNumber)

    suspend fun searchBooks(
        query: String,
    ) = RetrofitInstance.api.searchBooks(query)

}