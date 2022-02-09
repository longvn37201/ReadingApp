package vulong.book_app.ui.main_screen

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import vulong.book_app.ui.main_screen.bookshelf.BookshelfFragment
import vulong.book_app.ui.main_screen.category.CategoryFragment
import vulong.book_app.ui.main_screen.setting.SettingFragment

class MainScreenAdapter(f: Fragment) : FragmentStateAdapter(f) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> BookshelfFragment()
        1 -> CategoryFragment()
        2 -> SettingFragment()
        else -> {
            BookshelfFragment()
        }
    }
}