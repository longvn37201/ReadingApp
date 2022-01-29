package vulong.book_app.ui.main_screen.bookshelf

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import vulong.book_app.databinding.FragmentMainBookshelfBinding
import vulong.book_app.ui.main_screen.bookshelf.all_book_screen.AllBookFragment
import vulong.book_app.ui.main_screen.bookshelf.category_screen.CategoryFragment

class BookshelfFragment : Fragment() {


    private lateinit var binding: FragmentMainBookshelfBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMainBookshelfBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = BookShelfAdapter(
            arrayListOf(AllBookFragment(), CategoryFragment()),
            this
        )
        binding.pager.adapter = adapter
        TabLayoutMediator(binding.layoutTab, binding.pager) { tab, position ->
            if (position == 0) {
                tab.text = "Tất Cả"
            } else {
                tab.text = "Thể Loại"
            }
        }.attach()

    }

}