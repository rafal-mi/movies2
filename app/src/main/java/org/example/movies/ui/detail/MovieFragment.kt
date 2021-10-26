package org.example.movies.ui.detail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
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

        Log.d(TAG, "MovieFragment argument is $movie")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}