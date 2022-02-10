package vulong.book_app.ui.main_screen.bookshelf

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.tabs.TabLayoutMediator
import vulong.book_app.databinding.FragmentMainBookshelfBinding
import vulong.book_app.ui.main_screen.MainScreenViewModel
import vulong.book_app.ui.main_screen.bookshelf.all_book_screen.AllBookFragment
import vulong.book_app.ui.main_screen.bookshelf.read_recent_screen.RecentReadFragment
import vulong.book_app.util.Helper.setMarginTop

class BookshelfFragment : Fragment() {


    private var binding: FragmentMainBookshelfBinding? = null
    val viewModel by activityViewModels<MainScreenViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMainBookshelfBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = BookShelfAdapter(
            arrayListOf(AllBookFragment(), RecentReadFragment()),
            this
        )
        binding!!.pager.adapter = adapter
        TabLayoutMediator(binding!!.layoutTab, binding!!.pager, true) { tab, position ->
            if (position == 0) {
                tab.text = "Tất Cả"
            } else {
                tab.text = "Đang Đọc"
            }
        }.attach()
        viewModel.hasRecentBook.observe(viewLifecycleOwner) {
            if (it) {
                binding!!.pager.setCurrentItem(1, false)
            }
        }
        //margin for top inset
        binding!!.layoutTab.setMarginTop(viewModel.insetTop)
    }

}