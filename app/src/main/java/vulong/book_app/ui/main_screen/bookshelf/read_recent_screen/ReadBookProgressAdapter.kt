package vulong.book_app.ui.main_screen.bookshelf.read_recent_screen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vulong.book_app.R
import vulong.book_app.databinding.ItemBookRecentBinding
import vulong.book_app.model.local_db.ReadBookProgress
import vulong.book_app.model.remote_api.Book

class ReadBookProgressAdapter(
    private val listItems: ArrayList<Book>,
    private val listReadBookProgress: List<ReadBookProgress>,
    val clickListener: (Int) -> Unit,
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