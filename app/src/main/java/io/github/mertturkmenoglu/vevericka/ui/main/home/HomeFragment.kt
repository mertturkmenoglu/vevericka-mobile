package io.github.mertturkmenoglu.vevericka.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.mertturkmenoglu.vevericka.R
import io.github.mertturkmenoglu.vevericka.ui.main.home.posts.NewPostActivity
import io.github.mertturkmenoglu.vevericka.ui.main.home.posts.PostAdapter
import io.github.mertturkmenoglu.vevericka.util.FirebaseAuthHelper
import kotlinx.android.synthetic.main.fragment_home.view.*
import org.jetbrains.anko.support.v4.startActivity

class HomeFragment : Fragment() {
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var root: View
    private lateinit var mAdapter: PostAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        root = inflater.inflate(R.layout.fragment_home, container, false)
        root.homeNewPostFab.visibility = View.INVISIBLE

        root.homePostsRecyclerView.layoutManager = LinearLayoutManager(activity?.applicationContext)
        mAdapter = PostAdapter(context ?: throw IllegalStateException())
        root.homePostsRecyclerView.adapter = mAdapter

        getPosts()

        root.homeSwipeRefreshLayout.setOnRefreshListener(::getPosts)

        root.homeNewPostFab.setOnClickListener {
            startActivity<NewPostActivity>()
        }

        return root
    }

    private fun getPosts() {
        root.homeSwipeRefreshLayout.isRefreshing = true
        val uid = FirebaseAuthHelper.getCurrentUserId()
        homeViewModel.getPosts(uid).observe(viewLifecycleOwner, Observer {
            mAdapter.submitList(it)
            root.homeSwipeRefreshLayout.isRefreshing = false
            root.homeNewPostFab.visibility = View.VISIBLE
        })
    }
}
