package vulong.book_app.ui.read_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import vulong.book_app.R
import vulong.book_app.databinding.FragmentReadBookBinding
import vulong.book_app.model.local_db.ReadBookProgress
import vulong.book_app.util.ZoomOutPageTransformer


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
        viewModel.getDownloadState(requireContext())
        setData()

    }

    private fun setData() {
        //adapter
        binding.pager.adapter = ChapterPagerAdapter(
            this,
            viewModel.currentBook!!.chapterNumber,
            viewModel.currentBook!!.id,
            viewModel.isDownload
        ) {
            if (it == -1) {
                if (viewModel.isShowSystemBar.value == true) {
                    hideSystemUI()
                    binding.toolbar.visibility = View.GONE
                } else {
                    showSystemUI()
                    binding.toolbar.visibility = View.VISIBLE
                }
                viewModel.isShowSystemBar.value = !viewModel.isShowSystemBar.value!!
                -1
            } else {
                if (viewModel.isFirstScrollToSaved) {
                    if (it == viewModel.currentBookProcess!!.page) {
                        viewModel.isFirstScrollToSaved = false
                        viewModel.currentBookProcess!!.scrollY
                    } else {
                        -1
                    }
                } else {
                    -1
                }
            }
        }

        //flip book animation
        binding.pager.setPageTransformer(ZoomOutPageTransformer())

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
                        "Chương ${position + 1}/${viewModel.currentBook!!.chapterNumber}"

                    super.onPageSelected(position)
                }
            }
        )

        binding.pager.setCurrentItem(
            viewModel.currentBookProcess!!.page,
            false
        )

        //button autoscroll
        binding.buttonAutoScroll.setOnClickListener {
            val layoutContainer =
                binding.pager.findViewWithTag<ConstraintLayout>(binding.pager.currentItem)
            if (viewModel.isAuToScroll) {
                binding.buttonAutoScroll.setImageResource(R.drawable.ic_play)
                job.cancel()
            } else {
                binding.buttonAutoScroll.setImageResource(R.drawable.ic_stop)
                job = CoroutineScope(Main).launch {
                    while (true) {
                        delay(50)
                        val nestScrollView = layoutContainer
                            .findViewById<NestedScrollView>(R.id.scrollViewChapter)
                        nestScrollView.smoothScrollTo(0, nestScrollView.scrollY + 3)
                    }
                }
            }
            viewModel.isAuToScroll = !viewModel.isAuToScroll
        }

        //toolbar
        //pading top
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.toolbar.setPadding(0, insets.top, 0, 0)
            WindowInsetsCompat.CONSUMED
        }

        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onStop() {
        val nestScrollView =
            binding.pager.findViewWithTag<ConstraintLayout>(binding.pager.currentItem)
                .findViewById<NestedScrollView>(R.id.scrollViewChapter)
        viewModel.saveReadBookProgress(
            requireContext(),
            ReadBookProgress(
                viewModel.currentBook!!.id,
                binding.pager.currentItem,
                nestScrollView.scrollY,
                System.currentTimeMillis()
            )
        )
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
