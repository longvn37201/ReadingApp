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
import vulong.book_app.util.SharedPrefUtils
import vulong.book_app.util.model.State


class ReadBookFragment : Fragment() {


    private val args: ReadBookFragmentArgs by navArgs()
    private val viewModel: ReadBookViewModel by activityViewModels()
    private lateinit var binding: FragmentReadBookBinding
    private var scrollY: Int = 0
    private var currentPage: Int = 0

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

        viewModel.savedStatus.value = args.savedStatus
        currentPage = viewModel.savedStatus.value!!.page

        viewModel.getAllChapters()

        //display state
        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is State.Success -> {
                    binding.pager.visibility = View.VISIBLE
                    binding.loadingProgressBar.visibility = View.GONE
                    binding.layoutError.visibility = View.GONE
                }
                is State.Error -> {
                    binding.pager.visibility = View.GONE
                    binding.loadingProgressBar.visibility = View.GONE
                    binding.layoutError.visibility = View.VISIBLE
                }
                is State.Loading -> {
                    binding.pager.visibility = View.GONE
                    binding.loadingProgressBar.visibility = View.VISIBLE
                    binding.layoutError.visibility = View.GONE
                }
            }
        }

        //set data when chapters load success
        viewModel.chapters.observe(viewLifecycleOwner) { chapters ->
            if (chapters != null) {
                //adapter
                binding.pager.adapter = ReadBookAdapter(
                    chapters,
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
                        scrollY = y
                        if (viewModel.isShowSystemBar.value == true) {
                            hideSystemUI()
                            binding.toolbar.visibility = View.GONE
                            viewModel.isShowSystemBar.value = !viewModel.isShowSystemBar.value!!
                        }
                    },
                    viewModel.savedStatus.value!!.page,
                    viewModel.savedStatus.value!!.scrollY,
                )
                //recent page read
                if (viewModel.savedStatus.value!!.page != 0) {
                    binding.pager.setCurrentItem(
                        viewModel.savedStatus.value!!.page,
                        false
                    )
                }
                //flip book animation
                val bookFlipPageTransformer = BookFlipPageTransformer2()
                bookFlipPageTransformer.isEnableScale = true
                bookFlipPageTransformer.scaleAmountPercent = 10f
                binding.pager.setPageTransformer(bookFlipPageTransformer)
            }
        }

        //page change -> change title toolbar
        binding.pager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    currentPage = position
                    binding.textCurrentChapter.text =
                        "Chương ${currentPage + 1}/${viewModel.savedStatus.value!!.book.chapterNumber}"
                    super.onPageSelected(position)
                }
            }
        )

        binding.buttonReload.setOnClickListener {
            viewModel.getAllChapters()
        }

        setUpToolbar()
    }

    override fun onStop() {
        SharedPrefUtils.saveString(
            requireContext(),
            "recent_book_source",
            viewModel.savedStatus.value!!.book.publicSource
        )
        SharedPrefUtils.saveInt(
            requireContext(),
            "recent_book_y",
            scrollY
        )
        SharedPrefUtils.saveInt(
            requireContext(),
            "recent_book_page",
            currentPage
        )
        super.onStop()
    }

    override fun onDestroy() {
        if (viewModel.isShowSystemBar.value == false) {
            showSystemUI()
        }
        super.onDestroy()
    }

    private fun setUpToolbar() {
        //pading top
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.toolbar.setPadding(0, insets.top, 0, 0)
            WindowInsetsCompat.CONSUMED
        }

        //data
        binding.textCurrentChapter.text =
            "Chương ${currentPage + 1}/${viewModel.savedStatus.value!!.book.chapterNumber}"

        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }
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
