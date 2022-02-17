package vulong.book_app.ui.main_screen.offline

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import vulong.book_app.databinding.FragmentMainOfflineBinding
import vulong.book_app.model.remote_api.Book
import vulong.book_app.ui.main_screen.MainFragmentDirections
import vulong.book_app.ui.main_screen.MainScreenViewModel
import vulong.book_app.ui.shared_component.BookAdapter
import vulong.book_app.util.Helper
import vulong.book_app.util.Helper.setMarginTop

class OfflineFragment : Fragment() {
    private var binding: FragmentMainOfflineBinding? = null
    val viewModel by activityViewModels<MainScreenViewModel>()
    private lateinit var listBook: ArrayList<Book>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        if (binding == null)
            binding = FragmentMainOfflineBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listBook = Helper.readListBookInternal(requireContext())
        binding!!.recyclerViewOffline.adapter = BookAdapter(listBook, onBookItemClick)
        binding!!.recyclerViewOffline.layoutManager = LinearLayoutManager(requireContext())
        binding!!.recyclerViewOffline.setHasFixedSize(true)
        binding!!.recyclerViewOffline.setMarginTop(viewModel.insetTop)
    }

    private val onBookItemClick: (Int) -> Unit = { index ->
        val action =
            MainFragmentDirections.actionMainFragmentToBookDetailFragment(listBook[index])
        findNavController().navigate(action)
    }
}