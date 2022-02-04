package vulong.book_app.ui.read_screen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
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

        setPaddingForToolbar()

        viewModel.currentRead.value = args.currentRead
        viewModel.getAllChapters()
        viewModel.chapters.observe(viewLifecycleOwner) { chapters ->
            if (chapters != null) {
                binding.pager.adapter = ReadBookAdapter(
                    chapters,
                    {
                        if (viewModel.isShowSystemBar.value == true) {
                            hideSystemUI()
                            binding.toolbar.visibility = View.GONE
                        } else {
                            showSystemUI()
                            binding.toolbar.visibility = View.VISIBLE
                        }
                        viewModel.isShowSystemBar.value = !viewModel.isShowSystemBar.value!!
                    },
                    {
                        if (viewModel.isShowSystemBar.value == true) {
                            hideSystemUI()
                            binding.toolbar.visibility = View.GONE
                            viewModel.isShowSystemBar.value = !viewModel.isShowSystemBar.value!!
                        }
                    }
                )
                binding.pager.setCurrentItem(
                    viewModel.currentRead.value!!.currentChapter - 1,
                    false
                )

                val bookFlipPageTransformer = BookFlipPageTransformer2()
                bookFlipPageTransformer.isEnableScale = true
                bookFlipPageTransformer.scaleAmountPercent = 10f
                binding.pager.setPageTransformer(bookFlipPageTransformer)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("longvn", "onDestroy: ")
        if(viewModel.isShowSystemBar.value == false){
            showSystemUI()
        }
    }

    private fun setPaddingForToolbar() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
//            binding.toolbar.setPaddingTop(insets.top)
            binding.toolbar.setPadding(0, insets.top, 0, 0)
            WindowInsetsCompat.CONSUMED
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
