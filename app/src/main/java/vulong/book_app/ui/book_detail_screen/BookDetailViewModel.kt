package vulong.book_app.ui.book_detail_screen

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import vulong.book_app.local_db.BookRecentDatabase
import vulong.book_app.model.local_db.ReadBookProgress
import vulong.book_app.model.remote_api.Book
import vulong.book_app.retrofit.BookApiServiceInstance
import vulong.book_app.util.Helper
import vulong.book_app.util.SharedPrefUtils
import vulong.book_app.util.model.DownloadState
import vulong.book_app.util.model.State

class BookDetailViewModel() : ViewModel() {

    var currentBook: Book? = null
    var readBookProcess: ReadBookProgress? = null
    var readBookProcessState = MutableLiveData<State>()

    var downloadState = MutableLiveData<DownloadState>()

    fun getDownloadState(context: Context) {
        downloadState.value = DownloadState.Loading
        downloadState.value =
            if (SharedPrefUtils.getBooleanData(context, currentBook!!.id, false))
                DownloadState.Downloaded
            else DownloadState.None
    }


    fun getCurrentBookProgress(context: Context) {
        viewModelScope.launch {
            readBookProcessState.value = State.Loading
            readBookProcess =
                BookRecentDatabase
                    .getInstance(context)
                    .bookRecentDAO()
                    .getBookProgress(currentBook!!.id)
            readBookProcessState.value = State.Success
        }
    }

    fun downloadBook(context: Context) {
        viewModelScope.launch {
            downloadState.value = DownloadState.Loading
            try {
                //save image
                val job1 = async {

                    Glide.with(context)
                        .asBitmap()
                        .load(currentBook!!.imageUrl)
                        .into(object : CustomTarget<Bitmap?>() {
                            override fun onResourceReady(
                                bitmap: Bitmap,
                                transition: Transition<in Bitmap?>?,
                            ) {
                                Helper.saveBitmapToInternal(context, bitmap, currentBook!!.id)
                            }

                            override fun onLoadCleared(placeholder: Drawable?) {
                            }
                        })
                    true
                }
                //save book des
                val job2 = async {
                    Helper.writeBookToInternal(context, currentBook!!)
                    System.currentTimeMillis()
                    true
                }
                //save chapters
                val job3 = async {
                    val response = BookApiServiceInstance.api.getAllChapter(currentBook!!.id)
                    if (response.isSuccessful) {
                        val chapters = response.body()
                        //save chapter
                        Helper.writeChaptersOfBookToInternal(context, chapters!!)
                        true
                    } else {
                        false
                    }

                }
                if (job1.await() && job2.await() && job3.await()) {
                    downloadState.value = DownloadState.Downloaded
                    SharedPrefUtils.saveBoolean(context, currentBook!!.id, true)
                } else {
                    downloadState.value = DownloadState.None
                }

            } catch (e: Exception) {
                downloadState.value = DownloadState.None
            }

        }
    }

    fun deleteDownloadBook(context: Context) {
        viewModelScope.launch {
            downloadState.value = DownloadState.Loading
            val job1 = async {
                Helper.deleteBitmapFromInternal(context, currentBook!!.id)
                true
            }
            val job2 = async {
                Helper.deleteBookFromInternal(context, currentBook!!.id)
                true
            }
            val job3 = async {
                Helper.deleteChaptersOfBookFromInternal(context, currentBook!!.id,currentBook!!.chapterNumber)
                true
            }
            if (job1.await() && job2.await() && job3.await()) {
                SharedPrefUtils.saveBoolean(context, currentBook!!.id, false)
                downloadState.value = DownloadState.None
            }
        }
    }

}