package org.example.movies

import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import org.example.movies.data.db.Movie
import org.example.movies.ui.list.MovieListAdapter

@BindingAdapter("app:tint")
fun ImageView.setImageTint(@ColorInt color: Int) {
    setColorFilter(color)
}
