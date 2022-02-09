package vulong.book_app.retrofit

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import vulong.book_app.model.remote_api.Book
import vulong.book_app.model.remote_api.Chapters

interface BookApi {

    @GET("/getAllBook")
    suspend fun getAllBook(): Response<ArrayList<Book>>

    @GET("/getAllBook")
    suspend fun getBooksByLimit(
        @Query("skip") skip: String,
        @Query("take") take: String,
    ): Response<ArrayList<Book>>

    @GET("/getBooksByCategory")
    suspend fun getBooksByCategory(
        @Query("category") category: String,
    ): Response<ArrayList<Book>>

    @GET("/booksByCategory")
    suspend fun getBook(
        @Query("id") id: String,
    ): Response<Book>

    @GET("/getAllChapter")
    suspend fun getAllChapter(
        @Query("id") id: String,
    ): Response<Chapters>

    @GET("/search")
    suspend fun searchBooks(
        @Query("query") query: String,
    ): Response<ArrayList<Book>>

}