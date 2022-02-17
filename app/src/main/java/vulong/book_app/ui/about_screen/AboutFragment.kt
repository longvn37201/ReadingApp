package vulong.book_app.ui.about_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import vulong.book_app.databinding.FragmentAboutBinding
import vulong.book_app.util.Helper.dpToPx
import vulong.book_app.util.Helper.setMarginTop

class AboutFragment : Fragment() {
    private lateinit var binding: FragmentAboutBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            val px = requireContext().dpToPx(20)
            binding.buttonBack.setMarginTop(insets.top + px)
            WindowInsetsCompat.CONSUMED
        }

        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}