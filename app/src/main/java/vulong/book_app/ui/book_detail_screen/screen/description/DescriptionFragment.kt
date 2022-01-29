package vulong.book_app.ui.book_detail_screen.screen.description

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import vulong.book_app.databinding.FragmentBookDetailDescriptionBinding
import vulong.book_app.ui.book_detail_screen.BookDetailViewModel

class DescriptionFragment : Fragment() {

    private lateinit var binding: FragmentBookDetailDescriptionBinding
    private val viewModel: BookDetailViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentBookDetailDescriptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getTextDescription()

        viewModel.textDescription.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.textdescription.text = it
            }
        }
    }
}