package vulong.book_app.ui.main_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import vulong.book_app.R
import vulong.book_app.databinding.FragmentMainBinding
import vulong.book_app.util.Helper

class MainFragment : Fragment() {


    private var binding: FragmentMainBinding? = null
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
            Helper.systemBarInset(binding!!.root)

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