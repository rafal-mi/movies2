package org.example.movies.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import org.example.movies.App
import org.example.movies.App.Companion.TAG
import org.example.movies.MainViewModel
import org.example.movies.MainViewModelFactory
import org.example.movies.R
import org.example.movies.data.db.Movie
import org.example.movies.databinding.FragmentMovieBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class MovieFragment : Fragment() {
    val viewModel: MainViewModel by activityViewModels {
        val context = requireContext().applicationContext as App
        MainViewModelFactory(context.repository, this, null)
    }

    private val args by navArgs<MovieFragmentArgs>()

    private var _binding: FragmentMovieBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var position: Int = 0
    private lateinit var movie: Movie

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        position = args.position
        movie = args.movie
        val url = "https://image.tmdb.org/t/p/original/${movie.posterPath}?api_key=${App.instance.api_key}"

        Log.d(TAG, "MovieFragment argument is $movie")

        binding.apply {
            Glide.with(this@MovieFragment)
                .load(url)
                .error(R.drawable.ic_baseline_error_24)
                .into(imageView)

            titleTextView.text = movie.originalTitle
            releaseDateTextView.text = getString(R.string.release_date, movie.releaseDate)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.action_search).isVisible = false
        val item = menu.findItem(R.id.action_favorite)
        item.isVisible = true

        context?.let {
            val colorId = if(movie.favorite) R.color.red else R.color.white
            item.icon.setTint(ContextCompat.getColor(it, colorId))

        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.action_favorite) {
            context?.let {
//                val color = ContextCompat.getColor(it, R.color.teal_700)
//                item.icon.setTint(color)

                viewModel.mark(position, movie)
                val colorId = if(movie.favorite) R.color.red else R.color.white
                item.icon.setTint(ContextCompat.getColor(it, colorId))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}