package vulong.book_app.ui.book_detail_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayoutMediator
import jp.wasabeef.glide.transformations.BlurTransformation
import vulong.book_app.R
import vulong.book_app.databinding.FragmentBookDetailBinding
import vulong.book_app.model.local_db.ReadBookProgress
import vulong.book_app.util.Helper
import vulong.book_app.util.Helper.setMarginBottom
import vulong.book_app.util.Helper.setMarginTop
import vulong.book_app.util.model.State


class BookDetailFragment() : Fragment() {

    private val args: BookDetailFragmentArgs by navArgs()
    private val viewModel: BookDetailViewModel by activityViewModels()
    private var binding: FragmentBookDetailBinding? = null
    private var isSetupView = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        if (binding == null) {
            binding = FragmentBookDetailBinding.inflate(inflater, container, false)
        }
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.currentBook = args.currentBook
        //get current book progress
        viewModel.getCurrentBookProgress(requireContext())
        setUpData()
    }

    private fun setUpData() {
        binding!!.apply {
            if (!isSetupView) {
                isSetupView = true
                //book info
                Glide
                    .with(root.context)
                    .load(viewModel.currentBook!!.imageUrl)
                    .apply(RequestOptions.bitmapTransform(BlurTransformation(25, 3)))
                    .error(R.drawable.image_error)
                    .into(imageBackground)
                Glide
                    .with(root.context)
                    .load(viewModel.currentBook!!.imageUrl)
                    .placeholder(R.drawable.image_placeholder)
                    .error(R.drawable.image_error)
                    .into(imageCurrentBook)
                textBookName.text = viewModel.currentBook!!.name
                textAuthor.text = "Tác giả: ${viewModel.currentBook!!.author}"
                textCategory.text =
                    "Thể loại: ${Helper.handleCategoryTextInDetailScreen(viewModel.currentBook!!.category)}"
                textChapterNumber.text = "Số chương: ${viewModel.currentBook!!.chapterNumber}"

                //viewpager with tab layout
                pager.adapter = BookDetailAdapter(this@BookDetailFragment)
                pager.isSaveEnabled = false

                TabLayoutMediator(layoutTab, pager, true) { tab, position ->
                    if (position == 0) {
                        tab.text = "Giới Thiệu"
                    } else {
                        tab.text = "Các Chương"
                    }
                }.attach()

                //button read
                binding!!.buttonRead.setOnClickListener {
                    val action =
                        BookDetailFragmentDirections
                            .actionBookDetailFragmentToReadBookFragment(
                                viewModel.currentBook!!,
                                viewModel.readBookProcess ?: ReadBookProgress(
                                    idBook = viewModel.currentBook!!.id,
                                    page = 0,
                                    scrollY = 0
                                )
                            )
                    findNavController().navigate(action)
                }

                viewModel.readBookProcessState.observe(viewLifecycleOwner) {
                    if (it is State.Loading) {
                        binding!!.buttonRead.visibility = View.INVISIBLE
                        binding!!.loadingProgressBar.visibility = View.VISIBLE
                    }
                    if (it is State.Success) {
                        binding!!.buttonRead.visibility = View.VISIBLE
                        binding!!.loadingProgressBar.visibility = View.GONE
                        try {
                            binding!!.buttonRead.text =
                                "Đọc tiếp chương ${viewModel.readBookProcess!!.page + 1}"
                        } catch (e: Exception) {
                            binding!!.buttonRead.text =
                                "Bắt đầu đọc"
                        }
                    }
                }

                //toolbar back icon
                toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
                toolbar.setNavigationOnClickListener {
                    findNavController().popBackStack()
                }

                //insets toolbar
                ViewCompat.setOnApplyWindowInsetsListener(root) { _, windowInsets ->
                    val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
                    toolbar.setMarginTop(insets.top)
                    root.setMarginBottom(insets.bottom)
                    WindowInsetsCompat.CONSUMED
                }

                //show title when collapsing toolbar
                var isShow = true
                var scrollRange = -1
                appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { barLayout, verticalOffset ->
                    if (scrollRange == -1) {
                        scrollRange = barLayout?.totalScrollRange!!
                    }
                    if (scrollRange + verticalOffset == 0) {
                        collapsingToolbarLayout.title = viewModel.currentBook!!.name
                        imageCurrentBook.visibility = View.INVISIBLE
                        textAuthor.visibility = View.INVISIBLE
                        textBookName.visibility = View.INVISIBLE
                        textChapterNumber.visibility = View.INVISIBLE
                        textCategory.visibility = View.INVISIBLE
                        isShow = true
                    } else if (isShow) {
                        //careful there should a space between double quote otherwise it wont work
                        collapsingToolbarLayout.title = " "
                        imageCurrentBook.visibility = View.VISIBLE
                        textAuthor.visibility = View.VISIBLE
                        textBookName.visibility = View.VISIBLE
                        textChapterNumber.visibility = View.VISIBLE
                        textCategory.visibility = View.VISIBLE
                        isShow = false
                    }
                })
            } else {
                viewModel.readBookProcessState.observe(viewLifecycleOwner) {
                    if (it is State.Loading) {
                        binding!!.buttonRead.visibility = View.INVISIBLE
                        binding!!.loadingProgressBar.visibility = View.VISIBLE
                    }
                    if (it is State.Success) {
                        binding!!.buttonRead.visibility = View.VISIBLE
                        binding!!.loadingProgressBar.visibility = View.GONE
                        try {
                            binding!!.buttonRead.text =
                                "Đọc tiếp chương ${viewModel.readBookProcess!!.page + 1}"
                        } catch (e: Exception) {
                            binding!!.buttonRead.text =
                                "Bắt đầu đọc"
                        }
                    }
                }
            }
        }
    }
}