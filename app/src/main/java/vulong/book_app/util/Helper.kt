package vulong.book_app.util

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import vulong.book_app.util.Constant.BOOK_API_URL
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

object Helper {

    fun convertidToImageUrl(
        id: String,
    ) = "$BOOK_API_URL/public/$id/anh.png"

    fun convertidToTextIntroUrl(
        id: String,
    ) = "$BOOK_API_URL/public/$id/gioi_thieu.txt"

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


    fun readFile(
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
}