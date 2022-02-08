package vulong.book_app.ui.read_screen

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import vulong.book_app.databinding.ItemReadBookChapterBinding
import vulong.book_app.model.remote_api.Chapter


class ReadBookAdapter(
    private val chapters: ArrayList<Chapter>,
    val clickListener: (Int) -> Unit,
    val scrollListener: (Int) -> Unit,
    var pageSaved: Int,
    var scrollYSaved: Int,
) : RecyclerView.Adapter<ReadBookAdapter.ViewHolder>() {

    inner class ViewHolder(
        val binding: ItemReadBookChapterBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun setData(chapter: Chapter) {
            binding.scrollView.fullScroll(ScrollView.FOCUS_UP);
            binding.textContent.text = "${chapter.name}\n\n${chapter.content}"
        }

        fun scrollToSavedY() {
            CoroutineScope(Dispatchers.IO).launch {
                delay(500)
                withContext(Dispatchers.Main) {
                    binding.scrollView.smoothScrollTo(0, binding.scrollView.scrollY + scrollYSaved)
                    scrollYSaved = 0
                }
            }
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
        holder.setData(chapters[position])
        holder.binding.textContent.setOnClickListener {
            clickListener(position)
        }
        holder.binding.scrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            scrollListener(scrollY)
        }
        if (position == pageSaved && scrollYSaved != 0) {
            holder.scrollToSavedY()
        }
    }

    override fun getItemCount(): Int {
        return chapters.size
    }
}
