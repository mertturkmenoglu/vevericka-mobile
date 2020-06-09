package io.github.mertturkmenoglu.vevericka.ui.main.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.mertturkmenoglu.vevericka.R
import io.github.mertturkmenoglu.vevericka.ui.main.friends.PersonAdapter
import kotlinx.android.synthetic.main.fragment_search.view.*

class SearchFragment : Fragment() {
    private lateinit var searchViewModel: SearchViewModel
    private lateinit var mRoot: View
    private lateinit var mAdapter: PersonAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        searchViewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        mRoot = inflater.inflate(R.layout.fragment_search, container, false)

        initRecyclerView()

        mRoot.searchTextInput.setEndIconOnClickListener {
            search()
        }

        return mRoot
    }

    private fun initRecyclerView() {
        val ctx = activity?.applicationContext ?: throw IllegalStateException()
        mRoot.searchRecyclerView.visibility = View.INVISIBLE
        mRoot.searchRecyclerView.layoutManager = LinearLayoutManager(ctx)
        mAdapter = PersonAdapter(ctx)
        mRoot.searchRecyclerView.adapter = mAdapter
        mAdapter.setPersonClickListener {
            Toast.makeText(ctx, "Clicked", Toast.LENGTH_SHORT).show()
        }
    }

    private fun search() {
        val query = mRoot.searchTextInput.editText?.text?.toString()?.trim() ?: return

        if (query.isBlank()) {
            return
        }

        getSearchResults(query)
    }

    private fun getSearchResults(query: String) {
        searchViewModel.getSearchResults(query).observe(viewLifecycleOwner, Observer {
            mAdapter.submitList(it)
            mRoot.searchRecyclerView.visibility = View.VISIBLE
        })
    }
}