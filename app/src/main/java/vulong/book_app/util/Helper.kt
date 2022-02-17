package vulong.book_app.util

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import vulong.book_app.model.remote_api.Book
import vulong.book_app.model.remote_api.Chapter
import vulong.book_app.model.remote_api.Chapters
import java.io.*

object Helper {

    fun readListBookInternal(context: Context): ArrayList<Book> {
        val file = File(context.filesDir, "listBook.json")
        return if (file.exists()) {
            val bufferedReader: BufferedReader =
                file.bufferedReader()
            //file json String
            val jsonString = bufferedReader.use { it.readText() }
            Gson().fromJson(jsonString, object : TypeToken<ArrayList<Book>>() {}.type)
        } else {
            ArrayList()
        }
    }

    fun writeBookToInternal(context: Context, book: Book) {
        val listBook = readListBookInternal(context)
        listBook.add(book)
        val jsonString = Gson().toJson(listBook)
        val file = File(context.filesDir, "listBook.json")
        file.printWriter().use { out ->
            out.println(jsonString)
        }
    }

    fun deleteBookFromInternal(context: Context, id: String) {
        val listBook = readListBookInternal(context)
        //fix ConcurrentModificationException khi xoas phan tu bang forEach
        var pos = -1;
        listBook.forEachIndexed { index, item ->
            if (item.id == id) {
                pos = index
                return@forEachIndexed
            }
        }
        if (pos != -1) {
            listBook.removeAt(pos)
            val jsonString = Gson().toJson(listBook)
            val file = File(context.filesDir, "listBook.json")
            file.printWriter().use { out ->
                out.println(jsonString)
            }
        }
    }

    fun writeChaptersOfBookToInternal(context: Context, chapters: Chapters) {
        chapters.listChapter.forEach {
            val jsonString = Gson().toJson(it)
            val file = File(context.filesDir, "${it.idChapter}.json")
            file.printWriter().use { out ->
                out.println(jsonString)
            }
        }
    }

    fun readChapterOfBookFromInternal(context: Context, idChapter: String): Chapter? {
        val file = File(context.filesDir, "$idChapter.json")
        return if (file.exists()) {
            val bufferedReader: BufferedReader =
                file.bufferedReader()
            //file json String
            val jsonString = bufferedReader.use { it.readText() }

            //jsonString to object (or List)
            val typeToken = object : TypeToken<Chapter>() {}.type
            Gson().fromJson(jsonString, typeToken)

        } else {
            null
        }
    }

    fun deleteChaptersOfBookFromInternal(context: Context, id: String, chapterNumber: Int) {
        for (i in 1..chapterNumber) {
            try {
                val file = File(context.filesDir, "${id}_chuong$i.json")
                file.delete()
            } catch (e: Exception) {

            }
        }

    }

    fun saveBitmapToInternal(context: Context, bitmap: Bitmap, id: String): String {
        // path to /data/data/yourapp/files/img.jpg
        val myPath = File(context.filesDir, "$id.jpg")
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(myPath)
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                fos?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return myPath.toString()
    }

    fun deleteBitmapFromInternal(context: Context, id: String) {
        try {
            val file = File(context.filesDir, "$id.jpg")
            file.delete()
        } catch (e: Exception) {

        }
    }

    fun handleCategoryTextInHomeScreen(
        listCategory: List<String>,
    ) = if (listCategory.size > 1) "${listCategory[0]}, ${listCategory[1]}" else listCategory[0]

    fun handleCategoryTextInDetailScreen(
        listCategory: List<String>,
    ): String {
        var result = ""
        listCategory.forEach {
            result += "$it, "
        }
        return result.substring(0, result.length - 2)
    }


    fun readAssets(
        fileName: String,
        context: Context,
    ): ArrayList<String> {
        val listCategory = arrayListOf<String>()
        var reader: BufferedReader? = null
        try {
            reader = BufferedReader(
                InputStreamReader(
                    context.assets.open(fileName),
                    "UTF-8"
                )
            )
            var mLine: String? = reader.readLine()
            while (mLine != null) {
                listCategory.add(mLine.trim())
                mLine = reader.readLine()
            }
        } catch (e: IOException) {
            //exception
        } finally {
            if (reader != null) {
                try {
                    reader.close()
                } catch (e: IOException) {
                    //exception
                }
            }
        }
        return listCategory
    }

    fun systemBarInset(
        view: View,
    ) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            // Apply the insets as a margin to the view. Here the system is setting
            // only the bottom, left, and right dimensions, but apply whichever insets are
            // appropriate to your layout. You can also update the view padding
            // if that's more appropriate.
            val mlp = v.layoutParams as ViewGroup.MarginLayoutParams

            mlp.bottomMargin = insets.bottom
            mlp.topMargin = insets.top

            v.layoutParams = mlp

            // Return CONSUMED if you don't want want the window insets to keep being
            // passed down to descendant views.
            WindowInsetsCompat.CONSUMED
        }
    }

    fun View.setMarginTop(marginTop: Int) {
        val menuLayoutParams = this.layoutParams as ViewGroup.MarginLayoutParams
        menuLayoutParams.setMargins(0, marginTop, 0, 0)
        this.layoutParams = menuLayoutParams
    }

    fun View.setMarginBottom(margin: Int) {
        val menuLayoutParams = this.layoutParams as ViewGroup.MarginLayoutParams
        menuLayoutParams.setMargins(0, 0, 0, margin)
        this.layoutParams = menuLayoutParams
    }

    fun Context.dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }

    fun toIdChapter(idBook: String, chapter: Int) = "${idBook}_chuong$chapter"


}