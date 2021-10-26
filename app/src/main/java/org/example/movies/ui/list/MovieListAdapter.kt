package org.example.movies.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import org.example.movies.App
import org.example.movies.MainViewModel
import org.example.movies.R
import org.example.movies.data.db.Movie
import org.example.movies.databinding.MovieListItemBinding

class MovieListAdapter(
    private val viewModel: MainViewModel,
    private val listener: OnItemClickListener
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

    inner class MovieViewHolder(
        private val binding: MovieListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if(position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    item?.let {
                        listener.onItemClick(position, it)
                    }
                }

            }
        }

        fun bind(mainViewModel: MainViewModel, movie: Movie, position_: Int) {
            val url = "https://image.tmdb.org/t/p/original/${movie.posterPath}?api_key=${App.instance.api_key}"
            binding.apply {
                viewModel = mainViewModel
                item = movie
                position = position_
                Glide.with(itemView)
                    .load(url)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_baseline_error_24)
                    .into(imageView)


                executePendingBindings()
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, movie: Movie)
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