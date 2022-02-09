package vulong.book_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.fragment.NavHostFragment
import vulong.book_app.databinding.ActivityMainBinding
import vulong.book_app.util.Constant.SHOW_WELCOME_SCREEN
import vulong.book_app.util.SharedPrefUtils


class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        setupNavigationComponent()

    }


    private fun setupNavigationComponent() {
        val showWelcomeScreen = SharedPrefUtils.getBooleanData(this, SHOW_WELCOME_SCREEN, true)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)

        navGraph.startDestination = if (showWelcomeScreen) R.id.welcomeFragment
        else R.id.mainFragment

        navController.graph = navGraph
    }


}