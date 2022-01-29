package vulong.book_app.ui.main_screen

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import vulong.book_app.ui.main_screen.bookshelf.BookshelfFragment
import vulong.book_app.ui.main_screen.setting.SettingFragment

class MainScreenAdapter(fa: Fragment) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> BookshelfFragment()
        else -> {
            SettingFragment()
        }
    }
}