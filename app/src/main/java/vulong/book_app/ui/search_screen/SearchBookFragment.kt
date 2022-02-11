package vulong.book_app.ui.search_screen

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import vulong.book_app.databinding.FragmentSearchBookBinding
import vulong.book_app.ui.shared_component.BookAdapter
import vulong.book_app.util.model.State


class SearchBookFragment : Fragment() {

    private var binding: FragmentSearchBookBinding? = null
    private var isSetupView = false
    private val viewModel: SearchBookViewModel by viewModels()

    private val args: SearchBookFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        if (binding == null)
            binding = FragmentSearchBookBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        super.onViewCreated(view, savedInstanceState)
        if (!isSetupView) {
            isSetupView = true
            binding!!.apply {
                //is search by category or query?
                if (args.category.isEmpty()) {
                    viewModel.category = null
                    textCategory.visibility = View.GONE
                    textQuery.visibility = View.VISIBLE
                    //focus edittext
                    textQuery.requestFocus()
                    val imm =
                        requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.showSoftInput(textQuery, InputMethodManager.SHOW_IMPLICIT)
                } else {
                    viewModel.category = args.category
                    viewModel.getBooksByCategory()
                    textCategory.visibility = View.VISIBLE
                    textQuery.visibility = View.GONE
                }
                //title for category
                textCategory.text = viewModel.category ?: ""
                //loading state
                viewModel.loadingState.observe(viewLifecycleOwner) {
                    when (it) {
                        is State.Loading -> {
                            loadingProgressBar.visibility = View.VISIBLE
                            recyclerView.visibility = View.GONE
                            layoutError.visibility = View.GONE
                            textNotFound.visibility = View.GONE
                        }
                        is State.Success -> {
                            recyclerView.adapter =
                                BookAdapter(viewModel.listBook.value!!, onBookClick)
                            recyclerView.layoutManager = LinearLayoutManager(requireContext())
                            recyclerView.visibility = View.VISIBLE
                            loadingProgressBar.visibility = View.GONE
                            layoutError.visibility = View.GONE
                            if (viewModel.listBook.value.isNullOrEmpty()) {
                                textNotFound.visibility = View.VISIBLE
                            }
                        }
                        is State.Error -> {
                            layoutError.visibility = View.VISIBLE
                            textNotFound.visibility = View.GONE
                            loadingProgressBar.visibility = View.GONE
                            recyclerView.visibility = View.GONE
                        }
                    }
                }

                //button search in keyboard
                textQuery.setOnEditorActionListener { textView, actionId, keyEvent ->
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        viewModel.searchBooks(textQuery.text.toString().trim())
                        true
                    }
                    false
                }
                //on edt change
                textQuery.addTextChangedListener {
                    if (it.toString().isNotEmpty()) {
                        buttonClear.visibility = View.VISIBLE
                        buttonClear.visibility = View.VISIBLE
                    } else {
                        buttonClear.visibility = View.GONE
                    }
                }
                //button clear text
                buttonClear.setOnClickListener {
                    textQuery.setText("")
                }
                //button reload
                buttonReload.setOnClickListener {
                    if (viewModel.category != null) {
                        viewModel.getBooksByCategory()
                    }
                }
                //insets toolbar
                ViewCompat.setOnApplyWindowInsetsListener(root) { v, windowInsets ->
                    val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
                    val mlp = v.layoutParams as ViewGroup.MarginLayoutParams
                    mlp.bottomMargin = insets.bottom
                    mlp.topMargin = insets.top
                    v.layoutParams = mlp
                    WindowInsetsCompat.CONSUMED
                }
                //button back
                buttonBack.setOnClickListener {
                    findNavController().popBackStack()
                    val currentFocusedView = requireActivity().currentFocus
                    if (currentFocusedView != null) {
                        val imm =
                            requireActivity()
                                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(
                            currentFocusedView.windowToken,
                            InputMethodManager.HIDE_NOT_ALWAYS
                        )
                    }
                }
            }
        }
    }


    private val onBookClick: (Int) -> Unit = { index ->
        val action =
            SearchBookFragmentDirections.actionSearchBookFragmentToBookDetailFragment(viewModel.listBook.value!![index])
        findNavController().navigate(action)
    }
}