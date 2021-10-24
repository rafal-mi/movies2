package org.example.movies

import android.app.SearchManager
import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cursoradapter.widget.CursorAdapter
import org.example.movies.R
import org.example.movies.data.db.Movie

class MyCursorAdapter(
    private val context: Context,
    private val cursor_: Cursor,
    private val autoRequery: Boolean
): CursorAdapter(context, cursor_, autoRequery) {
    override fun newView(context: Context?, cursor: Cursor?, parent: ViewGroup?): View {
        return LayoutInflater.from(context).inflate(R.layout.autocomplete_row, parent, false)
    }

    override fun bindView(view: View?, context: Context?, cursor: Cursor?) {
        if(view == null) return
        if(cursor == null) return
        val textView = view.findViewById<TextView>(R.id.suggestion_text_view)
        val suggestion = cursor.getString(cursor.getColumnIndexOrThrow(SearchManager.SUGGEST_COLUMN_TEXT_1))
        textView.text = suggestion
    }

    override fun getCount(): Int {
        val count = super.getCount()
        return if(count < 4) count else 4
    }
}