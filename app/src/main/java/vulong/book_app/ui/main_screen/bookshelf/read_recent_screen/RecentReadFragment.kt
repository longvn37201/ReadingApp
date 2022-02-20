package vulong.book_app.ui.main_screen.bookshelf.read_recent_screen

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import vulong.book_app.R
import vulong.book_app.databinding.FragmentMainBookshelfRecentReadBinding
import vulong.book_app.ui.main_screen.MainFragmentDirections
import vulong.book_app.ui.main_screen.MainScreenViewModel
import vulong.book_app.util.model.State

class RecentReadFragment(
    private val navigateToOfflineCallBack: () -> Unit,
) : Fragment() {

    private var binding: FragmentMainBookshelfRecentReadBinding? = null
    private val viewModel: MainScreenViewModel by activityViewModels()
    private var adapter: ReadBookProgressAdapter? = null

    private var isSetupView = false
    private var isEdit = false

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
                is State.Success -> {
                    viewModel.getAllReadBookProgress(requireContext())
                    binding!!.layoutError.visibility = View.GONE
                }
                is State.Loading -> {
                    binding!!.layoutError.visibility = View.GONE
                    binding!!.loadingProgressBar.visibility = View.VISIBLE
                }
                is State.Error -> {
                    binding!!.layoutError.visibility = View.VISIBLE
                    binding!!.loadingProgressBar.visibility = View.GONE
                    binding!!.buttonReadOffline.setOnClickListener {
                        navigateToOfflineCallBack()
                    }
                }
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
                                isEdit,
                                onBookItemClick,
                                onDeleteItem
                            )
                            recyclerView.adapter = adapter
                            recyclerView.layoutManager = LinearLayoutManager(requireContext())
                        }
                    }
                }
            }

            //edit button
            buttonEdit.setOnClickListener {
                if (viewModel.listProgress.value!!.isNotEmpty()) {
                    try {
                        if (isEdit) {
                            buttonEdit.setImageResource(R.drawable.ic_edt)
                            viewModel.listBookRecent.value!!.forEachIndexed { index, book ->
                                recyclerView
                                    .findViewWithTag<ImageButton>("recent_item_delete_$index")
                                    .visibility = View.GONE
                            }
                        } else {
                            buttonEdit.setImageResource(R.drawable.ic_done_18)
                            viewModel.listBookRecent.value!!.forEachIndexed { index, book ->
                                recyclerView
                                    .findViewWithTag<ImageButton>("recent_item_delete_$index")
                                    .visibility = View.VISIBLE
                            }
                        }
                        isEdit = !isEdit
                        adapter!!.isEdit = isEdit
                    } catch (e: Exception) {

                    }
                }
            }

            //reload
            buttonReload.setOnClickListener {
                viewModel.getAllBook()
            }

        }
    }

    private val onBookItemClick: (Int) -> Unit = { index ->
        if (!isEdit) {
            val action =
                MainFragmentDirections.actionMainFragmentToBookDetailFragment(viewModel.listBookRecent.value!![index])
            findNavController().navigate(action)
        }
    }

    private val onDeleteItem: (Int) -> Unit = { index ->
        val builder = AlertDialog.Builder(requireContext())
        builder
            .setMessage("Xóa khỏi lịch sử?")
            .setCancelable(true)
            .setPositiveButton("Xác nhận") { _, _ ->
                //confirm delete
                viewModel.deleteRecentBook(requireContext(), index)
                adapter!!.apply {
                    listItems.removeAt(index)
                    notifyItemRemoved(index)
                    notifyItemRangeChanged(index, adapter!!.listItems.size)
                    if (listItems.isEmpty()) {
                        isEdit = false
                        binding!!.buttonEdit.setImageResource(R.drawable.ic_edt)
                        binding!!.textNoBookRecent.visibility = View.VISIBLE
                    } else {
                        binding!!.textNoBookRecent.visibility = View.GONE
                    }
                }

            }
            .setNegativeButton("Hủy") { dialog, _ ->
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
    }

    override fun onPause() {
        super.onPause()
        if (isEdit) {
            isEdit = false
            binding!!.buttonEdit.setImageResource(R.drawable.ic_edt)
            viewModel.listBookRecent.value!!.forEachIndexed { index, book ->
                binding!!.recyclerView
                    .findViewWithTag<ImageButton>("recent_item_delete_$index")
                    .visibility = View.GONE
            }
        }
    }

}