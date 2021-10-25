package org.example.movies.ui.list

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import kotlinx.android.synthetic.main.fragment_movie_list.*
import org.example.movies.*
import org.example.movies.App.Companion.TAG
import org.example.movies.databinding.FragmentMovieListBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MovieListFragment : Fragment() {

//    val viewModel: MainViewModel by navGraphViewModels(R.id.nav_graph) {
//        MainViewModelFactory((requireContext().applicationContext as App).repository, this, null)
//    }

    val viewModel: MainViewModel by activityViewModels {
        val context = requireContext().applicationContext as App
        MainViewModelFactory(context.repository, this, null)
    }

    private var _binding: FragmentMovieListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var adapter: MovieListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMovieListBinding.inflate(inflater, container, false)

        adapter = MovieListAdapter(viewModel)

        binding.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.itemAnimator = null
            recyclerView.adapter = adapter
        }

        viewModel.moviesLiveData.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }


        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.changeListEvent.observe(viewLifecycleOwner, EventObserver {
            Log.d(TAG, "Observed event $it")
            binding.recyclerView.scrollToPosition(0)
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}