package vulong.book_app.ui.book_detail_screen.screen.chapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vulong.book_app.databinding.ItemChapterBinding


class ChapterAdapter(
    private val numberChapter: Int,
    val onChapterClick: (Int) -> Unit,
) : RecyclerView.Adapter<ChapterAdapter.ViewHolder>() {

    inner class ViewHolder(
        val binding: ItemChapterBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun setData(position: Int) {
            binding.textChapter.text = "Chương ${position + 1}."
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemChapterBinding.inflate(LayoutInflater.from(parent.context),
                parent,
                false)
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(position)
        holder.itemView.setOnClickListener {
            onChapterClick(position)
        }
    }

    override fun getItemCount(): Int {
        return numberChapter
    }
}