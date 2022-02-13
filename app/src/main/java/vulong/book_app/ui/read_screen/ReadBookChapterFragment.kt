package vulong.book_app.ui.read_screen

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import vulong.book_app.R
import vulong.book_app.databinding.FragmentReadBookChapterBinding
import vulong.book_app.retrofit.RetrofitInstance
import vulong.book_app.util.Helper


class ReadBookChapterFragment(
    private val callBack: (Int) -> (Int),
) : Fragment() {

    private lateinit var binding: FragmentReadBookChapterBinding
    private var chapter: Int? = null
    private var idBook: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentReadBookChapterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.takeIf { it.containsKey("idBook") }?.apply {
            chapter = getInt("chapter")
            idBook = getString("idBook")
        }
        binding.chapterContainer.tag = chapter!! - 1
        loadData()
        binding.buttonReload.setOnClickListener {
            loadData()
        }
    }

    private fun loadData() {
        CoroutineScope(Dispatchers.Main).launch {
            binding.loadingProgressBar.visibility = View.VISIBLE
            binding.scrollViewChapter.visibility = View.GONE
            binding.layoutError.visibility = View.GONE
            binding.loadingProgressBar.setBackgroundResource(R.drawable.book_flip_animation)
            val animationDrawable = binding.loadingProgressBar.background as AnimationDrawable
            animationDrawable.start()
            try {

                val response =
                    RetrofitInstance.api.getChapter(Helper.toIdChapter(idBook!!, chapter!!))
                if (response.isSuccessful) {
                    binding.scrollViewChapter.visibility = View.VISIBLE
                    binding.layoutError.visibility = View.GONE

                    val data = response.body()
                    binding.scrollViewChapter.fullScroll(ScrollView.FOCUS_UP)
                    binding.textContent.text = "${data!!.name}\n\n${data.content}"
                    binding.textContent.setOnClickListener {
                        callBack(-1)
                    }
                    val result = callBack(chapter!! - 1)
                    delay(300)
                    if (result > 0) {
                        binding.scrollViewChapter.smoothScrollTo(0, result)
                    }

                } else {
                    binding.scrollViewChapter.visibility = View.GONE
                    binding.layoutError.visibility = View.VISIBLE
                }
            } catch (e: Exception) {
                binding.scrollViewChapter.visibility = View.GONE
                binding.layoutError.visibility = View.VISIBLE
            }

            animationDrawable.stop()
            binding.loadingProgressBar.visibility = View.GONE
        }
    }


}