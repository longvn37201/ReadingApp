package vulong.book_app.ui.book_detail_screen

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import vulong.book_app.ui.book_detail_screen.screen.chapter.ChapterFragment
import vulong.book_app.ui.book_detail_screen.screen.description.DescriptionFragment

//class BookDetailAdapter(
//    val fragmentManager: FragmentManager,
//    val behavior: Int,
//) : FragmentStatePagerAdapter(fragmentManager, behavior) {
//
//    override fun getCount(): Int = 2
//
//    override fun getItem(position: Int): Fragment {
//        return if (position == 0) {
//            DescriptionFragment()
//        } else {
//            ChapterFragment()
//        }
//    }
//
//    override fun getPageTitle(position: Int): CharSequence? {
//        if (position == 0) {
//            return "Giới Thiệu"
//        } else {
//            return "Chương"
//        }
//    }
//}

class BookDetailAdapter(f: Fragment) : FragmentStateAdapter(f) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> DescriptionFragment()
        else -> ChapterFragment()
    }
}