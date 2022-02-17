package vulong.book_app.ui.read_screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ChapterPagerAdapter(
    f: Fragment,
    private val count: Int,
    private val idBook: String,
    private val isDownload: Boolean = false,
    private val callBack: (Int) -> Int,
) : FragmentStateAdapter(f) {
    override fun getItemCount(): Int = count
    override fun createFragment(position: Int): Fragment {
        val fragment = ReadBookChapterFragment(isDownload) {
            callBack(it)
        }
        fragment.arguments = Bundle().apply {
            putInt("chapter", position + 1)
            putString("idBook", idBook)
        }
        return fragment
    }
}
