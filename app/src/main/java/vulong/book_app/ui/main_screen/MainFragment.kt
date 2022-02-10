package vulong.book_app.ui.main_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import vulong.book_app.R
import vulong.book_app.databinding.FragmentMainBinding

class MainFragment : Fragment() {


    private var binding: FragmentMainBinding? = null
    val viewModel by activityViewModels<MainScreenViewModel>()
    private var isSetupView = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        if (binding == null) {
            binding = FragmentMainBinding.inflate(inflater, container, false)
        }
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!isSetupView) {
            isSetupView = true
            viewModel.checkContainRecentBook(requireContext())
//            Helper.systemBarInset(binding!!.root)
            ViewCompat.setOnApplyWindowInsetsListener(binding!!.root) { v, windowInsets ->
                val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
                val mlp = v.layoutParams as ViewGroup.MarginLayoutParams
                viewModel.insetTop = insets.top
                viewModel.insetBottom = insets.bottom
                mlp.bottomMargin = insets.bottom
                v.layoutParams = mlp
                WindowInsetsCompat.CONSUMED
            }
            binding!!.apply {
                viewPager.adapter = MainScreenAdapter(this@MainFragment)
                viewPager.isUserInputEnabled = false
                viewPager.isSaveEnabled = false
                bottomNavigationView.setItemSelected(R.id.bookshelf)
                bottomNavigationView.setOnItemSelectedListener {
                    when (it) {
                        R.id.bookshelf -> {
                            viewPager.setCurrentItem(0, false)
                        }
                        R.id.search -> {
                            viewPager.setCurrentItem(1, false)
                        }
                        R.id.setting -> {
                            viewPager.setCurrentItem(2, false)
                        }
                    }
                }
            }
        }

    }


}