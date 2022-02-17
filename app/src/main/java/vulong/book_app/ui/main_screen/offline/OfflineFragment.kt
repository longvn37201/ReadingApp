package vulong.book_app.ui.main_screen.offline

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
import vulong.book_app.databinding.FragmentMainOfflineBinding
import vulong.book_app.model.remote_api.Book
import vulong.book_app.ui.main_screen.MainFragmentDirections
import vulong.book_app.ui.main_screen.MainScreenViewModel
import vulong.book_app.util.Helper
import vulong.book_app.util.Helper.setMarginTop

class OfflineFragment : Fragment() {

    private var binding: FragmentMainOfflineBinding? = null
    val viewModel by activityViewModels<MainScreenViewModel>()

    private var adapter: OfflineAdapter? = null

    private lateinit var listBook: ArrayList<Book>
    private var isEdit = false


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
        if (listBook.isEmpty()) {
            binding!!.textEmpty.visibility = View.VISIBLE
        } else {
            binding!!.textEmpty.visibility = View.GONE
        }
        binding!!.apply {
            adapter = OfflineAdapter(listBook, isEdit, onBookItemClick, onDeleteItem)
            recyclerViewOffline.adapter = adapter
            recyclerViewOffline.layoutManager = LinearLayoutManager(requireContext())
            recyclerViewOffline.setHasFixedSize(true)
            //edit item
            buttonEdit.setOnClickListener {
                if (listBook.isNotEmpty()) {
                    try {
                        if (isEdit) {
                            buttonEdit.setImageResource(R.drawable.ic_edt)
                            listBook.forEachIndexed { index, book ->
                                recyclerViewOffline
                                    .findViewWithTag<ImageButton>("offline_item_delete_$index")
                                    .visibility = View.GONE
                            }
                        } else {
                            buttonEdit.setImageResource(R.drawable.ic_done_18)
                            listBook.forEachIndexed { index, book ->
                                recyclerViewOffline
                                    .findViewWithTag<ImageButton>("offline_item_delete_$index")
                                    .visibility = View.VISIBLE
                            }
                        }
                        isEdit = !isEdit
                        adapter!!.isEdit = isEdit
                    } catch (e: Exception) {

                    }
                }
            }
            top.setMarginTop(viewModel.insetTop)
        }
    }

    private val onBookItemClick: (Int) -> Unit = { index ->
        if (!isEdit) {
            val action =
                MainFragmentDirections.actionMainFragmentToBookDetailFragment(listBook[index])
            findNavController().navigate(action)
        }
    }
    private val onDeleteItem: (Int) -> Unit = { index ->
        val builder = AlertDialog.Builder(requireContext())
        builder
            .setMessage("Xóa truyện đã tải?")
            .setCancelable(true)
            .setPositiveButton("Xác nhận") { _, _ ->
                //confirm delete
                viewModel.deleteBookDownload(
                    requireContext(),
                    listBook[index].id,
                    listBook[index].chapterNumber
                )
                adapter!!.apply {
                    listBook.removeAt(index)
                    notifyItemRemoved(index)
                    notifyItemRangeChanged(index, adapter!!.listItems.size)
                }
                if (listBook.isEmpty()) {
                    isEdit = false
                    binding!!.buttonEdit.setImageResource(R.drawable.ic_edt)
                    binding!!.textEmpty.visibility = View.VISIBLE
                } else {
                    binding!!.textEmpty.visibility = View.GONE
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
            listBook.forEachIndexed { index, book ->
                binding!!.recyclerViewOffline
                    .findViewWithTag<ImageButton>("offline_item_delete_$index")
                    .visibility = View.GONE
            }
        }
    }
}