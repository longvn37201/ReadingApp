package vulong.book_app.ui.read_screen

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.recyclerview.widget.RecyclerView
import vulong.book_app.databinding.ItemReadBookChapterBinding
import vulong.book_app.model.remote_api.Chapters


class ReadBookAdapter(
    private val chapters: Chapters,
    val clickListener: (Int) -> Unit,
    val scrollListener: () -> Unit,
) : RecyclerView.Adapter<ReadBookAdapter.ViewHolder>() {

    inner class ViewHolder(
        val binding: ItemReadBookChapterBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun setData(content: String, position: Int) {
            binding.scrollView.fullScroll(ScrollView.FOCUS_UP);
            binding.textContent.text = "CHƯƠNG ${position + 1}\n$content"
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemReadBookChapterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(chapters.arrayChapter[position], position)
        holder.binding.textContent.setOnClickListener {
            clickListener(position)
        }
        holder.binding.scrollView.viewTreeObserver.addOnScrollChangedListener {
            scrollListener()
        }
//        holder.itemView.setOnClickListener {
//            clickListener(position)
//        }
    }

    override fun getItemCount(): Int {
        return chapters.arrayChapter.size
    }
}

fun Context.dpToPx(dp: Int): Int {
    return (dp * resources.displayMetrics.density).toInt()
}