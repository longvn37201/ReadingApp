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
import vulong.book_app.retrofit.BookApiServiceInstance
import vulong.book_app.util.Helper


class ReadBookChapterFragment(
    private val isDownload: Boolean = false,
    private val callBack: (Int) -> (Int),
) : Fragment() {

    private lateinit var binding: FragmentReadBookChapterBinding
    private var chapterNum: Int? = null
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
            chapterNum = getInt("chapter")
            idBook = getString("idBook")
        }
        binding.chapterContainer.tag = chapterNum!! - 1
        loadData()
        binding.buttonReload.setOnClickListener {
            loadData()
        }
    }

    private fun loadData() {
        if (isDownload) {
            CoroutineScope(Dispatchers.Main).launch {
                binding.loadingProgressBar.visibility = View.VISIBLE
                binding.scrollViewChapter.visibility = View.GONE
                binding.layoutError.visibility = View.GONE
                binding.loadingProgressBar.setBackgroundResource(R.drawable.book_flip_animation)
                val animationDrawable = binding.loadingProgressBar.background as AnimationDrawable
                animationDrawable.start()
                try {
                    val chapter = Helper.readChapterOfBookFromInternal(
                        requireContext(),
                        Helper.toIdChapter(idBook!!, chapterNum!!),
                    )
                    animationDrawable.stop()
                    binding.loadingProgressBar.visibility = View.GONE
                    binding.scrollViewChapter.visibility = View.VISIBLE
                    binding.layoutError.visibility = View.GONE

                    binding.scrollViewChapter.fullScroll(ScrollView.FOCUS_UP)
                    binding.textContent.text = "${chapter!!.name}\n\n${chapter.content}"
                    binding.textContent.setOnClickListener {
                        callBack(-1)
                    }
                    val result = callBack(chapterNum!! - 1)
                    delay(300)
                    if (result > 0) {
                        binding.scrollViewChapter.smoothScrollTo(0, result)
                    }
                } catch (e: Exception) {
                    binding.scrollViewChapter.visibility = View.GONE
                    binding.loadingProgressBar.visibility = View.GONE
                    binding.layoutError.visibility = View.VISIBLE
                }
            }
        } else {
            CoroutineScope(Dispatchers.Main).launch {
                binding.loadingProgressBar.visibility = View.VISIBLE
                binding.scrollViewChapter.visibility = View.GONE
                binding.layoutError.visibility = View.GONE
                binding.loadingProgressBar.setBackgroundResource(R.drawable.book_flip_animation)
                val animationDrawable = binding.loadingProgressBar.background as AnimationDrawable
                animationDrawable.start()
                try {
                    val response =
                        BookApiServiceInstance.api.getChapter(
                            Helper.toIdChapter(idBook!!, chapterNum!!)
                        )
                    animationDrawable.stop()
                    binding.loadingProgressBar.visibility = View.GONE
                    if (response.isSuccessful) {
                        binding.scrollViewChapter.visibility = View.VISIBLE
                        binding.layoutError.visibility = View.GONE

                        val data = response.body()
                        binding.scrollViewChapter.fullScroll(ScrollView.FOCUS_UP)
                        binding.textContent.text = "${data!!.name}\n\n${data.content}"
                        binding.textContent.setOnClickListener {
                            callBack(-1)
                        }
                        val result = callBack(chapterNum!! - 1)
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
                    binding.loadingProgressBar.visibility = View.GONE
                    binding.layoutError.visibility = View.VISIBLE
                }
            }
        }
    }


}