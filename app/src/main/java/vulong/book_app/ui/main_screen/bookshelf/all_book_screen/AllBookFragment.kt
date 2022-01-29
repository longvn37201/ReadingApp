package vulong.book_app.ui.main_screen.bookshelf.all_book_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import vulong.book_app.databinding.FragmentMainBookshelfAllBookBinding
import vulong.book_app.ui.main_screen.MainFragmentDirections
import vulong.book_app.util.State.*

class AllBookFragment : Fragment() {


    private val viewModel: AllBookViewModel by viewModels()
    private var binding: FragmentMainBookshelfAllBookBinding? = null
    private var isSetupView = false
    private lateinit var adapter: BookAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        if (binding == null) {
            binding = FragmentMainBookshelfAllBookBinding.inflate(inflater, container, false)
        }
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!isSetupView) {

            isSetupView = true
            binding!!.buttonReload.setOnClickListener {
                viewModel.getBooks()
            }

            viewModel.state.observe(viewLifecycleOwner) {
                setData()
            }
        }


    }

    private fun setData() {
        binding!!.apply {
            when (viewModel.state.value) {
                is Loading -> {
                    layoutLoadingShimmer.visibility = View.VISIBLE
                    recyclerViewAllBook.visibility = View.GONE
                    layoutError.visibility = View.GONE
                }
                is Success -> {
                    adapter = BookAdapter(viewModel.allBooks.value!!, onBookItemClick)
                    recyclerViewAllBook.adapter = adapter
                    recyclerViewAllBook.layoutManager =
                        LinearLayoutManager(requireContext())
                    recyclerViewAllBook.visibility = View.VISIBLE
                    layoutError.visibility = View.GONE
                    layoutLoadingShimmer.visibility = View.GONE
                }
                is Error -> {
                    val state = viewModel.state.value as Error
                    textError.text = state.errorMessage
                    layoutError.visibility = View.VISIBLE
                    recyclerViewAllBook.visibility = View.GONE
                    layoutLoadingShimmer.visibility = View.GONE
                }
                else -> {}
            }

        }
    }

    private val onBookItemClick: (Int) -> Unit = { index ->
        val currentBook = viewModel.allBooks.value!![index]
        val action = MainFragmentDirections.actionMainFragmentToBookDetailFragment(currentBook)
        findNavController().navigate(action)
    }


}