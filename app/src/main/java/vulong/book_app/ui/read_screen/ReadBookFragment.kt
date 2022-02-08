package vulong.book_app.ui.read_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.wajahatkarim3.easyflipviewpager.BookFlipPageTransformer2
import vulong.book_app.databinding.FragmentReadBookBinding


class ReadBookFragment : Fragment() {


    private val args: ReadBookFragmentArgs by navArgs()
    private val viewModel: ReadBookViewModel by activityViewModels()
    private lateinit var binding: FragmentReadBookBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentReadBookBinding.inflate(inflater, container, false)
        hideSystemUI()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.currentBook = args.currentBook
        viewModel.currentBookProcess = args.currentBookProgress

        setData()

    }

    private fun setData() {
        //adapter
        binding.pager.adapter = ReadBookAdapter(
            viewModel.currentBook!!.listChapter,
            {//on click
                if (viewModel.isShowSystemBar.value == true) {
                    hideSystemUI()
                    binding.toolbar.visibility = View.GONE
                } else {
                    showSystemUI()
                    binding.toolbar.visibility = View.VISIBLE
                }
                viewModel.isShowSystemBar.value = !viewModel.isShowSystemBar.value!!
            },
            { y -> //on scroll
                viewModel.currentBookProcess!!.scrollY = y
                if (viewModel.isShowSystemBar.value == true) {
                    hideSystemUI()
                    binding.toolbar.visibility = View.GONE
                    viewModel.isShowSystemBar.value = !viewModel.isShowSystemBar.value!!
                }
            },
            viewModel.currentBookProcess!!.page,
            viewModel.currentBookProcess!!.scrollY,
        )
        //set recent page read
        binding.pager.setCurrentItem(
            viewModel.currentBookProcess!!.page,
            false
        )
        //flip book animation
        val bookFlipPageTransformer = BookFlipPageTransformer2()
        bookFlipPageTransformer.isEnableScale = true
        bookFlipPageTransformer.scaleAmountPercent = 10f
        binding.pager.setPageTransformer(bookFlipPageTransformer)

        //page change -> change title toolbar
        binding.pager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    viewModel.currentBookProcess!!.page = position
                    binding.textCurrentChapter.text =
                        "Chương ${viewModel.currentBookProcess!!.page + 1}/${viewModel.currentBook!!.chapterNumber}"
                    super.onPageSelected(position)
                }
            }
        )

        //toolbar
        //pading top
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.toolbar.setPadding(0, insets.top, 0, 0)
            WindowInsetsCompat.CONSUMED
        }

        //toolbar title
        binding.textCurrentChapter.text =
            "Chương ${viewModel.currentBookProcess!!.page + 1}/${viewModel.currentBook!!.chapterNumber}"

        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onStop() {
        if (!(viewModel.currentBookProcess!!.page == 0 && viewModel.currentBookProcess!!.scrollY == 0)) {
            viewModel.currentBookProcess!!.time = System.currentTimeMillis()
            viewModel.saveReadBookProgress(requireContext())
        }
        super.onStop()
    }

    override fun onDestroy() {
        if (viewModel.isShowSystemBar.value == false) {
            showSystemUI()
        }
        super.onDestroy()
    }

    private fun hideSystemUI() {
        WindowInsetsControllerCompat(requireActivity().window, binding.root).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    private fun showSystemUI() {
        WindowInsetsControllerCompat(
            requireActivity().window,
            binding.root
        ).show(WindowInsetsCompat.Type.systemBars())
    }

}
