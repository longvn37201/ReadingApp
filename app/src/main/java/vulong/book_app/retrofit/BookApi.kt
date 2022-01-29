package vulong.book_app.retrofit

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import vulong.book_app.model.remote_api.Book
import vulong.book_app.model.remote_api.BookDescription

interface BookApi {

    @GET("/books")
    suspend fun getAllBooks(): Response<ArrayList<Book>>

    @GET("/books")
    suspend fun getBooksByLimit(
        @Query("skip") skip: String,
        @Query("take") take: String,
    ): Response<ArrayList<Book>>

    @GET("/booksByCategory")
    suspend fun getBooksByCategory(
        @Query("category") category: String,
    ): Response<ArrayList<Book>>

    @GET("/booksByCategory")
    suspend fun getBook(
        @Query("publicSource") publicSource: String,
    ): Response<Book>

    @GET("/description")
    suspend fun getDescription(
        @Query("publicSource") publicSource: String,
    ): Response<BookDescription>

    @GET("/chapter")
    suspend fun getChapter(
        @Query("publicSource") publicSource: String,
        @Query("chapterNumber") chapterNumber: String,
    ): String

    @GET("/search")
    suspend fun searchBooks(
        @Query("query") query: String,
    ): Response<ArrayList<Book>>

}