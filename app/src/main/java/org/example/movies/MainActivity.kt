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
import androidx.core.view.MenuItemCompat
import androidx.core.view.MenuItemCompat.collapseActionView
import androidx.lifecycle.asLiveData
import androidx.paging.ExperimentalPagingApi
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import org.example.movies.App.Companion.TAG
import org.example.movies.databinding.ActivityMainBinding
import org.example.movies.kotlin.getQueryTextChangeStateFlow

@ExperimentalPagingApi
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    @ExperimentalPagingApi
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

        viewModel.autocompleteLiveData.observe(this, { list ->
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

        menu.findItem(R.id.action_favorite).isVisible = false

        val menuItem: MenuItem = menu.findItem(R.id.action_search)
        searchView = menuItem.actionView as SearchView
        searchView?.imeOptions = EditorInfo.IME_ACTION_DONE

        val cursor = MatrixCursor(autocompleteColNames)
        searchView?.suggestionsAdapter = MyCursorAdapter(this, cursor, false, object: MyCursorAdapter.OnItemClickListener {
            override fun onItemClick(string: String) {
                Log.d(TAG, "Suggestion clicked has string $string")
                searchView!!.setQuery(string, true)
                searchView!!.clearFocus()

            }

        })

//        searchView?.setOnSuggestionListener(object: SearchView.OnSuggestionListener {
//            override fun onSuggestionSelect(position: Int): Boolean {
//                val item = searchView!!.suggestionsAdapter.getItem(position)
//                Log.d(TAG, "Suggestion selected at position $position the item $item")
//                return false
//            }
//
//            override fun onSuggestionClick(position: Int): Boolean {
//                val item = searchView!!.suggestionsAdapter.getItem(position)
//                Log.d(TAG, "Suggestion clicked at position $position the item $item")
//                return false
//            }
//
//        })

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d(TAG, "Query text submitted: $query")

                query?.let {

                    viewModel.setQuery(query)
                    (searchView as SearchView).clearFocus()

                }
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                Log.d(TAG, "Query text changed: $query")

                query?.let {
                    viewModel.queryTextFlow.value = it
                }
                return true
            }
        })

        menuItem.setOnActionExpandListener(object: MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                Log.d(TAG, "menu item collapsing")
                viewModel.setQuery(null)
                return true
            }

        })

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