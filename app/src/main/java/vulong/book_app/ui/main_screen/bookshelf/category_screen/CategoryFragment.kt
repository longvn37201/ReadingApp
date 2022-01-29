package vulong.book_app.ui.main_screen.bookshelf.category_screen

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import vulong.book_app.R
import vulong.book_app.databinding.FragmentMainBookshelfCategoryBinding
import vulong.book_app.util.Constant.CATEGORY_FILE_NAME
import vulong.book_app.util.Helper


class CategoryFragment : Fragment() {


    private lateinit var binding: FragmentMainBookshelfCategoryBinding
    private lateinit var listCategory: ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMainBookshelfCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addCategoryFromAssets()


    }

    private val onCategoryClick: (String) -> Unit = {
        Log.d("longvn", "$it is clicked")
    }

    private fun addCategoryFromAssets() {
        listCategory = Helper.readFile(CATEGORY_FILE_NAME, requireContext())
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
                binding.layoutCategory.addView(this)
                (layoutParams as GridLayout.LayoutParams).columnSpec =
                    GridLayout.spec(GridLayout.UNDEFINED, 1f)
                setOnClickListener {
                    onCategoryClick(category)
                }
            }
        }
    }

}