package vulong.book_app.ui.main_screen.setting

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import vulong.book_app.R
import vulong.book_app.databinding.FragmentMainSettingBinding
import vulong.book_app.ui.main_screen.MainFragmentDirections
import vulong.book_app.ui.main_screen.MainScreenViewModel
import vulong.book_app.util.Constant.POLICY_URL


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

        binding!!.apply {
            setUpSettingItem()
            setUpAccountInfo()
        }

        return binding!!.root
    }

    private fun setUpAccountInfo() {
        binding!!.apply {
            if (auth.currentUser != null) {
                // User is signed in
                Glide
                    .with(root.context)
                    .load(auth.currentUser!!.photoUrl)
                    .into(imageAvatar)
                textName.text = auth.currentUser!!.displayName
            } else {
                // No user is signed in
                textName.text = "Chưa đăng nhập"
                imageAvatar.setImageResource(R.drawable.ic_person)
            }
        }
    }


    private fun setUpSettingItem() {
        binding!!.apply {
            //policy
            policy.setOnClickListener {
                val builder = AlertDialog.Builder(requireContext())
                builder
                    .setMessage("Mở trong trình duyệt?")
                    .setCancelable(true)
                    .setPositiveButton("Xác nhận") { _, _ ->
                        //confirm
                        val webpage: Uri =
                            Uri.parse(POLICY_URL)
                        val intent = Intent(Intent.ACTION_VIEW, webpage)
                        startActivity(intent)
                    }
                    .setNegativeButton("Hủy") { dialog, _ ->
                        dialog.dismiss()
                    }
                val alert = builder.create()
                alert.show()
            }
            //login/logout
            if (auth.currentUser != null) {
                // User is signed in
                textLogin.text = "Đăng xuất"
                loginIcon.setImageResource(R.drawable.ic_logout)
            } else {
                // No user is signed in
                textLogin.text = "Đăng nhập"
                loginIcon.setImageResource(R.drawable.ic_login)
            }
            login.setOnClickListener {
                if (auth.currentUser != null) {
                    val builder = AlertDialog.Builder(requireContext())
                    builder
                        .setMessage("Xác nhận đăng xuất?")
                        .setCancelable(true)
                        .setPositiveButton("Xác nhận") { _, _ ->
                            //confirm
                            auth.signOut()
                            setUpAccountInfo()
                            setUpSettingItem()
                        }
                        .setNegativeButton("Hủy") { dialog, _ ->
                            dialog.dismiss()
                        }
                    val alert = builder.create()
                    alert.show()
                } else {
                    val action = MainFragmentDirections.actionMainFragmentToThirdFragment()
                    //1 for true
                    action.isComeFromSetting = "1"
                    findNavController().navigate(action)
                }
            }
            //app info
            appInfo.setOnClickListener {
                TransitionManager.beginDelayedTransition(binding!!.layoutItemSetting,
                    AutoTransition())
                if (binding!!.textAppVersion.visibility == View.GONE) {
                    binding!!.textAppVersion.visibility = View.VISIBLE
                    binding!!.appInfoExpandable.setImageResource(R.drawable.ic_expand_more)
                } else {
                    binding!!.textAppVersion.visibility = View.GONE
                    binding!!.appInfoExpandable.setImageResource(R.drawable.ic_arrow_right)
                }
            }
            //setting
            setting.setOnClickListener {
                TransitionManager.beginDelayedTransition(
                    binding!!.layoutItemSetting,
                    AutoTransition()
                )
                if (binding!!.textTheme.visibility == View.GONE) {
                    binding!!.textTheme.visibility = View.VISIBLE
                    binding!!.settingExpandableIcon.setImageResource(R.drawable.ic_expand_more)
                } else {
                    binding!!.textTheme.visibility = View.GONE
                    binding!!.settingExpandableIcon.setImageResource(R.drawable.ic_arrow_right)
                }
            }
        }
    }
}


