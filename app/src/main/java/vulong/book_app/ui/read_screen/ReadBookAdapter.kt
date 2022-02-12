package vulong.book_app.ui.read_screen

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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

        fun setData(chapter: Chapter, position: Int) {
            binding.scrollView.fullScroll(ScrollView.FOCUS_UP);
            binding.textContent.text = "${chapter.name}\n\n${chapter.content}"
            binding.scrollView.tag = position
        }

        fun scrollToSavedY() {
            CoroutineScope(IO).launch {
                delay(300)
//                withContext(Dispatchers.Main) {
                binding.scrollView.smoothScrollTo(0, binding.scrollView.scrollY + scrollYSaved)
                scrollYSaved = 0
//                }
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
        holder.setData(chapters[position], position)
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
