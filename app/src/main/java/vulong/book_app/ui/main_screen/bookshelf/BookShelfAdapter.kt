package vulong.book_app.ui.main_screen.bookshelf

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import vulong.book_app.ui.book_detail_screen.screen.chapter.ChapterFragment
import vulong.book_app.ui.book_detail_screen.screen.description.DescriptionFragment

class BookShelfAdapter(
    list: ArrayList<Fragment>,
    fragment: Fragment,
) : FragmentStateAdapter(fragment) {

    private val fragmentList = list

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}

