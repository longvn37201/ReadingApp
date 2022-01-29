package vulong.book_app.ui.welcome_screen.screen

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import vulong.book_app.R
import vulong.book_app.databinding.FragmentWelcomeThirdBinding
import vulong.book_app.model.firebase.User
import vulong.book_app.util.Constant
import vulong.book_app.util.Constant.DB_URL
import vulong.book_app.util.Constant.SERVER_CLIENT_ID


class ThirdFragment : Fragment() {

    private lateinit var binding: FragmentWelcomeThirdBinding
    private lateinit var gso: GoogleSignInOptions
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

    private lateinit var databaseRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentWelcomeThirdBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        init google sigin, firebase auth, realtime db
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(SERVER_CLIENT_ID)
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        auth = Firebase.auth
        databaseRef = FirebaseDatabase.getInstance(DB_URL).reference

        //start home without login
        binding.buttonStartHome.setOnClickListener {
            navigateToHome()
        }

        //login
        binding.buttonLogin.setOnClickListener {
            val currentUser = auth.currentUser
            if (currentUser == null)
                signIn()
            else {
                //todo remove toast
                Toast.makeText(
                    context,
                    "Chào mừng: ${currentUser.displayName}",
                    Toast.LENGTH_SHORT
                ).show()
                navigateToHome()
            }
        }
    }

    private fun signIn() {
        val signInIntent: Intent = googleSignInClient.signInIntent
        resultLauncher.launch(signInIntent)
    }

    private val resultLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    Toast.makeText(
                        context,
                        "Đăng nhập thất bại, thử lại hoặc chọn cách đăng nhập khác.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            } else {
                Log.d("longvn", "That bai")
                Toast.makeText(
                    context,
                    "Đăng nhập thất bại, thử lại hoặc chọn cách đăng nhập khác.",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    if (user != null) {
                        (createUserIfNotAlreadyExist(user))
                        Toast.makeText(
                            context,
                            "Chào mừng: ${auth.currentUser?.displayName}",
                            Toast.LENGTH_SHORT
                        ).show()
                        navigateToHome()
                    } else Toast.makeText(
                        context,
                        "Đăng nhập thất bại, thử lại hoặc chọn cách đăng nhập khác.",
                        Toast.LENGTH_SHORT,
                    ).show()
                } else {
                    Toast.makeText(
                        context,
                        "Đăng nhập thất bại, thử lại hoặc chọn cách đăng nhập khác.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }

    private fun createUserIfNotAlreadyExist(user: FirebaseUser) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val data = dataSnapshot.value
                //chưa tồn tại người dùng thì add new
                if (data == null) {
                    databaseRef
                        .child("users")
                        .child(user.uid)
                        .setValue(
                            User(
                                uid = user.uid,
                                name = if (user.displayName != null) user.displayName!! else "Người dùng"
                            )
                        )
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        databaseRef.child("users").child(user.uid).addValueEventListener(postListener)
    }

    private fun navigateToHome() {
        val editor: SharedPreferences.Editor =
            requireContext()
                .getSharedPreferences(Constant.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
                .edit()
        editor.putBoolean(Constant.SHOW_WELCOME_SCREEN, false)
        editor.apply()
        findNavController().navigate(R.id.action_welcomeFragment_to_mainFragment)
    }

}

//todo signOut Firebase.auth.signOut() in another fragment
