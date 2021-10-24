package org.example.movies

import android.app.SearchManager
import android.database.MatrixCursor
import android.os.Bundle
import android.provider.BaseColumns
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import org.example.movies.databinding.ActivityMainBinding
import org.example.movies.kotlin.getQueryTextChangeStateFlow

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<MainViewModel> {
        val context = this.applicationContext as App
        MainViewModelFactory(context.repository, this, null)
    }

    private var searchView: SearchView? = null
    private var query: String = ""

    private val autocompleteColNames = arrayOf(
        BaseColumns._ID,  // necessary for adapter
        SearchManager.SUGGEST_COLUMN_TEXT_1 // the full search term
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        viewModel.searchLiveData.observe(this, { list ->
            val cursor = MatrixCursor(autocompleteColNames)
            list.forEachIndexed { index, movie ->
                val row = arrayOf(index, movie.originalTitle)
                cursor.addRow(row)
            }

            searchView?.apply {
                suggestionsAdapter.changeCursor(cursor)
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)

        val menuItem: MenuItem = menu.findItem(R.id.action_search)
        searchView = menuItem.actionView as SearchView
        searchView?.imeOptions = EditorInfo.IME_ACTION_DONE

        val cursor = MatrixCursor(autocompleteColNames)
        searchView?.suggestionsAdapter = MyCursorAdapter(this, cursor, false)

        searchView?.let { it ->
            viewModel.queryTextChangeStateFlow = it.getQueryTextChangeStateFlow()
            viewModel.isQueryTypedNowFlow = viewModel.queryTextChangeStateFlow
                .debounce(1000)
                .map {
                    query = it
                    it.isNotEmpty()
                }
            viewModel.isQueryTypedNowLiveData = viewModel.isQueryTypedNowFlow.asLiveData()
            viewModel.isQueryTypedNowLiveData.observe(this, {
                Log.d(App.TAG, "Observed query $query")
                viewModel.setQuery(query)
            })
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}