package org.example.movies.ui.list

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import androidx.paging.LoadState
import kotlinx.android.synthetic.main.fragment_movie_list.*
import org.example.movies.*
import org.example.movies.App.Companion.TAG
import org.example.movies.data.db.Movie
import org.example.movies.databinding.FragmentMovieListBinding
import org.example.movies.ui.detail.MovieFragment
import org.example.movies.ui.detail.MovieFragmentArgs

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MovieListFragment : Fragment(), MovieListAdapter.OnItemClickListener {

    val viewModel: MainViewModel by activityViewModels {
        val context = requireContext().applicationContext as App
        MainViewModelFactory(context.repository, this, null)
    }

    private var _binding: FragmentMovieListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var adapter: MovieListAdapter
    private var favorites = arrayListOf<Long>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMovieListBinding.inflate(inflater, container, false)

        adapter = MovieListAdapter(viewModel, this)

        binding.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.itemAnimator = null
            recyclerView.adapter = adapter
        }

        adapter.addLoadStateListener { loadState ->
            binding.apply {
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                recyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
                buttonRetry.isVisible = loadState.source.refresh is LoadState.Error
                textViewError.isVisible = loadState.source.refresh is LoadState.Error

                if(loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    adapter.itemCount < 1) {
                    recyclerView.isVisible = false
                    textViewEmpty.isVisible = true
                } else {
                    textViewEmpty.isVisible = false
                }

            }
            if(loadState.refresh is LoadState.NotLoading) {
                updateSnapshot()
            }

        }

        viewModel.moviesLiveData.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        viewModel.dbListLiveData.observe(viewLifecycleOwner) { list_ ->
            list_?.let { list ->
                favorites.clear()
                list.forEach { favorites.add(it.id) }
                updateSnapshot()
            }
        }

        return binding.root

    }

    fun updateSnapshot() {
        val snapshot = adapter.snapshot()
        snapshot.forEach { it ->
            it?.let { item ->
                favorites.forEach { id->
                    if(item.id == id) {
                        item.favorite = true
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.changeListEvent.observe(viewLifecycleOwner, EventObserver {
            Log.d(TAG, "Observed event $it")
            binding.recyclerView.scrollToPosition(0)
        })

        viewModel.listPositionEvent.observe(this.viewLifecycleOwner, EventObserver {
            Log.i(TAG, "Observed Event $it")

            adapter.notifyItemChanged(it)
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(position: Int, movie: Movie) {
        val action = MovieListFragmentDirections.actionMovieListFragmentToMovieFragment(position, movie)
        findNavController().navigate(action)
    }
}