package vulong.book_app.ui.main_screen.setting

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import vulong.book_app.R
import vulong.book_app.databinding.FragmentMainSettingBinding
import vulong.book_app.ui.main_screen.MainFragmentDirections
import vulong.book_app.ui.main_screen.MainScreenViewModel
import vulong.book_app.util.Constant.LIST_SETTING_ITEM
import vulong.book_app.util.Helper.dpToPx


class SettingFragment : Fragment() {


    private val viewModel by activityViewModels<MainScreenViewModel>()
    private var binding: FragmentMainSettingBinding? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        if (binding == null)
            binding = FragmentMainSettingBinding.inflate(inflater, container, false)

        auth = Firebase.auth

        setUpSettingItem()
        setUpInfoUser()

        return binding!!.root
    }

    private fun setUpInfoUser() {
        if (auth.currentUser != null) {
            // User is signed in
            Glide
                .with(binding!!.root.context)
                .load(auth.currentUser!!.photoUrl)
                .into(binding!!.imageAvatar)
            binding!!.textName.text = auth.currentUser!!.displayName
        } else {
            // No user is signed in
            binding!!.textName.text = "Chưa đăng nhập"
            binding!!.imageAvatar.setImageResource(R.drawable.ic_person)
        }
    }


    private fun setUpSettingItem() {
        binding!!.layoutItemSetting.removeAllViews()
        LIST_SETTING_ITEM.forEachIndexed { index, item ->
            val inflater = LayoutInflater.from(context)
            val constrainLayout =
                inflater.inflate(R.layout.item_setting, null, false) as ConstraintLayout
            val layoutParams =
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    requireContext().dpToPx(70))
            constrainLayout.layoutParams = layoutParams
            constrainLayout.setOnClickListener {
                when (index) {
                    0 -> {

                    }
                    1 -> {
                        val url = "https://facebook.com"
                        val uri = Uri.parse(url)
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        if (intent.resolveActivity(requireActivity().packageManager) != null) {
                            startActivity(intent)
                        }
                    }
                    2 -> {
                        findNavController().navigate(R.id.action_mainFragment_to_aboutFragment)
                    }
                    3 -> {
                        //login/logout
                        if (auth.currentUser != null) {
                            auth.signOut()
                            setUpInfoUser()
                            setUpSettingItem()
                        } else {
                            val action = MainFragmentDirections.actionMainFragmentToThirdFragment()
                            //1 for true
                            action.isComeFromSetting = "1"
                            findNavController().navigate(action)
                        }
                    }
                }
            }
            if (item.first == "Đăng Xuất" && auth.currentUser == null) {
                constrainLayout.findViewById<TextView>(R.id.textItemName).text = "Đăng Nhập"
                constrainLayout.findViewById<ImageView>(R.id.iconStart)
                    .setImageResource(R.drawable.ic_login)
            } else {
                constrainLayout.findViewById<TextView>(R.id.textItemName).text = item.first
                constrainLayout.findViewById<ImageView>(R.id.iconStart)
                    .setImageResource(item.second)
            }
            binding!!.layoutItemSetting.addView(constrainLayout)
        }
    }

}

