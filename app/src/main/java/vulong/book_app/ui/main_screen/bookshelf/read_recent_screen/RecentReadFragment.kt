package vulong.book_app.ui.main_screen.bookshelf.read_recent_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import vulong.book_app.databinding.FragmentMainBookshelfRecentReadBinding
import vulong.book_app.ui.main_screen.MainFragmentDirections
import vulong.book_app.ui.main_screen.MainScreenViewModel
import vulong.book_app.util.model.State

class RecentReadFragment : Fragment() {

    private var binding: FragmentMainBookshelfRecentReadBinding? = null
    private val viewModel: MainScreenViewModel by activityViewModels()
    private var adapter: ReadBookProgressAdapter? = null
    private var isSetupView = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        if (binding == null)
            binding = FragmentMainBookshelfRecentReadBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.allBookState.observe(viewLifecycleOwner) {
            when (it) {
                is State.Success -> viewModel.getAllReadBookProgress(requireContext())
            }
        }

        setData()

        if (!isSetupView) {
            isSetupView = true
        } else {
            viewModel.getAllReadBookProgress(requireContext())
        }
    }

    private fun setData() {
        binding!!.apply {
            viewModel.allProgressState.observe(viewLifecycleOwner) {
                when (it) {
                    is State.Loading -> {
                        loadingProgressBar.visibility = View.VISIBLE
                        textNoBookRecent.visibility = View.GONE
                        recyclerView.visibility = View.GONE
                    }
                    is State.Success -> {
                        loadingProgressBar.visibility = View.GONE
                        if (viewModel.listProgress.value.isNullOrEmpty()) {
                            textNoBookRecent.visibility = View.VISIBLE
                            recyclerView.visibility = View.GONE
                        } else {
                            recyclerView.visibility = View.VISIBLE
                            textNoBookRecent.visibility = View.GONE
                            viewModel.getListBookForProgressAdapter()
                            adapter = ReadBookProgressAdapter(
                                viewModel.listBookRecent.value!!,
                                viewModel.listProgress.value!!,
                                onBookItemClick
                            )
                            recyclerView.adapter = adapter
                            recyclerView.layoutManager = LinearLayoutManager(requireContext())
                        }
                    }
                }

            }

        }
    }

    private val onBookItemClick: (Int) -> Unit = { index ->
        val action =
            MainFragmentDirections.actionMainFragmentToBookDetailFragment(viewModel.listBookRecent.value!![index])
        findNavController().navigate(action)
    }


}