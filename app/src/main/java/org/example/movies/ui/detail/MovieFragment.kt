package org.example.movies.ui.detail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import org.example.movies.App
import org.example.movies.App.Companion.TAG
import org.example.movies.R
import org.example.movies.databinding.FragmentMovieBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class MovieFragment : Fragment() {
    private val args by navArgs<MovieFragmentArgs>()

    private var _binding: FragmentMovieBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMovieBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val movie = args.movie
        val url = "https://image.tmdb.org/t/p/original/${movie.posterPath}?api_key=${App.instance.api_key}"

        Log.d(TAG, "MovieFragment argument is $movie")

        binding.apply {
            Glide.with(this@MovieFragment)
                .load(url)
                .error(R.drawable.ic_baseline_error_24)
                .into(imageView)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}