package vulong.book_app.ui.main_screen.bookshelf.all_book_screen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vulong.book_app.R
import vulong.book_app.databinding.ItemBookBinding
import vulong.book_app.model.remote_api.Book
import vulong.book_app.util.Helper.convertPublicSourceToImageUrl
import vulong.book_app.util.Helper.handleCategoryTextInHomeScreen

class BookAdapter(
    private val listItems: ArrayList<Book>,
    val clickListener: (Int) -> Unit,
) : RecyclerView.Adapter<BookAdapter.ViewHolder>() {

    inner class ViewHolder(
        private val binding: ItemBookBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun setData(book: Book) {
            Glide
                .with(binding.root.context)
                .load(convertPublicSourceToImageUrl(book.publicSource))
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.image_error)
                .into(binding.imageBook)
            binding.textBookName.text = book.name
            binding.textAuthor.text = "Tác giả: ${book.author}"
            binding.textCategory.text = "Thể loại: ${handleCategoryTextInHomeScreen(book.category)}"
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemBookBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(listItems[position])
        holder.itemView.setOnClickListener {
            clickListener(position)
        }
    }

    override fun getItemCount(): Int {
        return listItems.size
    }
}