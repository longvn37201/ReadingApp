package vulong.book_app.ui.main_screen.category

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import vulong.book_app.R
import vulong.book_app.databinding.FragmentMainCategoryBinding
import vulong.book_app.ui.main_screen.MainFragmentDirections
import vulong.book_app.ui.main_screen.MainScreenViewModel
import vulong.book_app.util.Constant
import vulong.book_app.util.Helper
import vulong.book_app.util.Helper.setMarginTop

class CategoryFragment : Fragment() {

    private var binding: FragmentMainCategoryBinding? = null
    val viewModel by activityViewModels<MainScreenViewModel>()

    private lateinit var listCategory: ArrayList<String>
    private var isSetupView = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMainCategoryBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addCategoryFromAssets()
        binding!!.layoutSearch.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToSearchBookFragment("")
            findNavController().navigate(action)
        }
        //margin for top inset
        binding!!.insetTop.setMarginTop(viewModel.insetTop)
    }

    private val onCategoryClick: (String) -> Unit = { category ->
        val action = MainFragmentDirections.actionMainFragmentToSearchBookFragment(category.trim())
        findNavController().navigate(action)
    }

    private fun addCategoryFromAssets() {
        listCategory = Helper.readAssets(Constant.CATEGORY_FILE_NAME, requireContext())
        listCategory.forEach { category ->
            TextView(context).apply {
                text = category.trim()
                isClickable = true
                gravity = Gravity.CENTER
                background =
                    ResourcesCompat.getDrawable(resources,
                        R.drawable.category_item_background,
                        null)
                val params: LinearLayout.LayoutParams =
                    LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                    )
                val scale = requireContext().resources.displayMetrics.density
                val marginInPixels = ((5 * scale + 0.5f).toInt())
                val heightInPixels = ((50 * scale + 0.5f).toInt())
                params.setMargins(marginInPixels, marginInPixels, marginInPixels, marginInPixels)
                params.height = heightInPixels
                layoutParams = params
                binding!!.layoutCategory.addView(this)
                (layoutParams as GridLayout.LayoutParams).columnSpec =
                    GridLayout.spec(GridLayout.UNDEFINED, 1f)
                setOnClickListener {
                    onCategoryClick(category)
                }
            }
        }
    }

}