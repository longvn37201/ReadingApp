package vulong.book_app.ui.main_screen.bookshelf

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

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

