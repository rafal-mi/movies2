package org.example.movies.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.example.movies.MainViewModel
import org.example.movies.data.db.Movie
import org.example.movies.databinding.MovieListItemBinding

class MovieListAdapter(
    private val viewModel: MainViewModel
): PagingDataAdapter<Movie, MovieListAdapter.MovieViewHolder>(MovieDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            MovieListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.bind(viewModel, item, position)
        }

    }

    class MovieViewHolder(
        private val binding: MovieListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(mainViewModel: MainViewModel, movie: Movie, position_: Int) {
            binding.apply {
                viewModel = mainViewModel
                item = movie

                position = position_
                executePendingBindings()
            }
        }
    }
}

private class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }


}