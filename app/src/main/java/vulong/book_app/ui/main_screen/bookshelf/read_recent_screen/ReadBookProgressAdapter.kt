package vulong.book_app.ui.main_screen.bookshelf.read_recent_screen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vulong.book_app.R
import vulong.book_app.databinding.ItemBookRecentBinding
import vulong.book_app.model.local_db.ReadBookProgress
import vulong.book_app.model.remote_api.Book

class ReadBookProgressAdapter(
    val listItems: ArrayList<Book>,
    private val listReadBookProgress: List<ReadBookProgress>,
    var isEdit: Boolean = false,
    val clickListener: (Int) -> Unit,
    val deleteItemListener: (Int) -> Unit,
) : RecyclerView.Adapter<ReadBookProgressAdapter.ViewHolder>() {

    inner class ViewHolder(
        private val binding: ItemBookRecentBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun setData(book: Book, position: Int) {
            Glide
                .with(binding.root.context)
                .load(book.imageUrl)
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.image_error)
                .into(binding.imageBook)
            binding.textBookName.text = book.name
            binding.textChapter.text =
                "Đang đọc chương ${listReadBookProgress[position].page + 1}/${book.chapterNumber}"
            if (isEdit) {
                binding.buttonDelete.visibility = View.VISIBLE
            } else {
                binding.buttonDelete.visibility = View.GONE
            }
            binding.buttonDelete.tag = "recent_item_delete_$position"
            binding.buttonDelete.setOnClickListener {
                deleteItemListener(position)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemBookRecentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(listItems[position], position)
        holder.itemView.setOnClickListener {
            clickListener(position)
        }
    }

    override fun getItemCount(): Int {
        return listItems.size
    }
}