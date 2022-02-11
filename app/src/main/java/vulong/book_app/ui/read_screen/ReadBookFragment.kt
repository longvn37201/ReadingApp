package vulong.book_app.ui.read_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.wajahatkarim3.easyflipviewpager.BookFlipPageTransformer2
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import vulong.book_app.R
import vulong.book_app.databinding.FragmentReadBookBinding
import vulong.book_app.util.model.State


class ReadBookFragment : Fragment() {


    private val args: ReadBookFragmentArgs by navArgs()
    private val viewModel: ReadBookViewModel by viewModels()
    private lateinit var binding: FragmentReadBookBinding

    private lateinit var job: Job

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
        viewModel.getAllChapter()
        setData()

    }

    private fun setData() {
        viewModel.chaptersOfBookLoadingState.observe(viewLifecycleOwner) {
            binding.apply {
                when (it) {
                    is State.Loading -> {
                        pager.visibility = View.GONE
                        layoutError.visibility = View.GONE
                        loadingProgressBar.visibility = View.VISIBLE
                    }
                    is State.Error -> {
                        pager.visibility = View.GONE
                        layoutError.visibility = View.VISIBLE
                        loadingProgressBar.visibility = View.GONE
                    }
                    is State.Success -> {
                        pager.visibility = View.VISIBLE
                        layoutError.visibility = View.GONE
                        loadingProgressBar.visibility = View.GONE
                    }
                    else -> {}
                }
            }
        }
        viewModel.chaptersOfBook.observe(viewLifecycleOwner) { chapters ->
            if (chapters != null) {
                //adapter
                binding.pager.adapter = ReadBookAdapter(
                    chapters.listChapter,
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
                        //fix auto scroll crash
//                        if (viewModel.isShowSystemBar.value == true) {
//                            hideSystemUI()
//                            binding.toolbar.visibility = View.GONE
//                            viewModel.isShowSystemBar.value = !viewModel.isShowSystemBar.value!!
//                        }
                    },
                    viewModel.currentBookProcess!!.page,
                    viewModel.currentBookProcess!!.scrollY,
                )
                //set recent page read
                binding.pager.setCurrentItem(
                    viewModel.currentBookProcess!!.page,
                    false
                )
            }
        }

        //flip book animation
        val bookFlipPageTransformer = BookFlipPageTransformer2()
        bookFlipPageTransformer.isEnableScale = true
        bookFlipPageTransformer.scaleAmountPercent = 10f
        binding.pager.setPageTransformer(bookFlipPageTransformer)

        //page change -> change title toolbar
        binding.pager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    //fix click rootview of current page callback this page change
                    if (viewModel.isAuToScroll && position != viewModel.currentBookProcess!!.page) {
                        viewModel.isAuToScroll = false
                        binding.buttonAutoScroll.setImageResource(R.drawable.ic_play)
                        job.cancel()
                    }
                    viewModel.currentBookProcess!!.page = position
                    binding.textCurrentChapter.text =
                        "Chương ${viewModel.currentBookProcess!!.page + 1}/${viewModel.currentBook!!.chapterNumber}"
                    super.onPageSelected(position)
                }
            }
        )

        //button autoscroll
        binding.buttonAutoScroll.setOnClickListener {
            val currentItemOfPager =
                binding.pager.findViewWithTag<NestedScrollView>(binding.pager.currentItem)
            if (viewModel.isAuToScroll) {
                binding.buttonAutoScroll.setImageResource(R.drawable.ic_play)
                job.cancel()
            } else {
                binding.buttonAutoScroll.setImageResource(R.drawable.ic_stop)
                job = CoroutineScope(Main).launch {
                    while (true) {
                        delay(50)
                        currentItemOfPager.smoothScrollTo(0, currentItemOfPager.scrollY + 2)
                    }
                }
            }
            viewModel.isAuToScroll = !viewModel.isAuToScroll
        }


        //button reload
        binding.buttonReload.setOnClickListener {
            viewModel.getAllChapter()
        }

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
        viewModel.currentBookProcess!!.time = System.currentTimeMillis()
        viewModel.saveReadBookProgress(requireContext())
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
