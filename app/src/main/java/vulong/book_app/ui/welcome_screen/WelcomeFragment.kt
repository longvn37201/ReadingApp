package vulong.book_app.ui.welcome_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import vulong.book_app.databinding.FragmentWelcomeBinding
import vulong.book_app.ui.welcome_screen.screen.FirstFragment
import vulong.book_app.ui.welcome_screen.screen.SecondFragment
import vulong.book_app.ui.welcome_screen.screen.ThirdFragment
import vulong.book_app.util.Helper.systemBarInset


class WelcomeFragment : Fragment() {

    private lateinit var binding: FragmentWelcomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        systemBarInset(binding.root)

        binding.viewPager.adapter = WelcomeAdapter(
            arrayListOf(
                FirstFragment(),
                SecondFragment(),
                ThirdFragment()
            ),
            childFragmentManager,
            lifecycle
        )
        binding.viewpagerIndicator.setViewPager2(binding.viewPager)

    }


}