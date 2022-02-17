package vulong.book_app.ui.main_screen.offline

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vulong.book_app.R
import vulong.book_app.databinding.ItemBookOfflineBinding
import vulong.book_app.model.remote_api.Book

class OfflineAdapter(
    val listItems: ArrayList<Book>,
    var isEdit: Boolean = false,
    val clickListener: (Int) -> Unit,
    val deleteItemListener: (Int) -> Unit,
) : RecyclerView.Adapter<OfflineAdapter.ViewHolder>() {

    inner class ViewHolder(
        private val binding: ItemBookOfflineBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun setData(book: Book, position: Int) {
            binding.apply {
                Glide
                    .with(root.context)
                    .load(book.imageUrl)
                    .placeholder(R.drawable.image_placeholder)
                    .error(R.drawable.image_error)
                    .into(imageBook)
                textBookName.text = book.name
                textAuthor.text = "Tác giả: ${book.author}"
                if (isEdit) {
                    buttonDelete.visibility = View.VISIBLE
                } else {
                    buttonDelete.visibility = View.GONE
                }
                buttonDelete.tag = "offline_item_delete_$position"
                buttonDelete.setOnClickListener {
                    deleteItemListener(position)
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemBookOfflineBinding.inflate(
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