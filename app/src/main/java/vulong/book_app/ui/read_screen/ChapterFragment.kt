package vulong.book_app.ui.read_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import vulong.book_app.databinding.FragmentReadBookChapterBinding

class ChapterFragment : Fragment() {

    private lateinit var binding: FragmentReadBookChapterBinding
    private val viewModel: ReadBookViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentReadBookChapterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textContent.text =
            viewModel.chapters.value!!.arrayChapter[viewModel.currentRead.value!!.currentChapter]
    }

}