package vulong.book_app.ui.main_screen.search_book

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import vulong.book_app.databinding.FragmentMainSearchBookBinding

class SearchFragment : Fragment() {

    private var binding: FragmentMainSearchBookBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMainSearchBookBinding.inflate(inflater, container, false)
        return binding!!.root
    }


}