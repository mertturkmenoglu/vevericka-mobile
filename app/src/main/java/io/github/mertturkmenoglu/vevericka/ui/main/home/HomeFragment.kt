package io.github.mertturkmenoglu.vevericka.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import io.github.mertturkmenoglu.vevericka.R
import io.github.mertturkmenoglu.vevericka.ui.main.home.posts.NewPostActivity
import io.github.mertturkmenoglu.vevericka.ui.main.home.posts.PostAdapter
import io.github.mertturkmenoglu.vevericka.ui.main.profile.ProfileFragment
import io.github.mertturkmenoglu.vevericka.util.FirebaseAuthHelper
import io.github.mertturkmenoglu.vevericka.util.makeInvisible
import io.github.mertturkmenoglu.vevericka.util.makeVisible
import kotlinx.android.synthetic.main.fragment_home.view.*
import org.jetbrains.anko.support.v4.startActivity

class HomeFragment : Fragment() {
    companion object {
        const val KEY_POST = "post"
    }

    private lateinit var viewModel: HomeViewModel
    private lateinit var root: View
    private lateinit var mAdapter: PostAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        root = inflater.inflate(R.layout.fragment_home, container, false)
        root.homeNewPostFab.makeInvisible()

        initRecyclerView()
        getPosts()

        root.homeSwipeRefreshLayout.setOnRefreshListener(::getPosts)

        root.homeNewPostFab.setOnClickListener {
            startActivity<NewPostActivity>()
        }

        return root
    }

    private fun initRecyclerView() {
        root.homePostsRecyclerView.layoutManager = LinearLayoutManager(activity?.applicationContext)
        mAdapter = PostAdapter(context ?: throw IllegalStateException())
        root.homePostsRecyclerView.adapter = mAdapter

        setAdapterClickListeners()
    }

    private fun setAdapterClickListeners() {
        mAdapter.setOnCommentClickListener { post ->
            val args = bundleOf(KEY_POST to Gson().toJson(post))
            val action = R.id.action_navigation_home_to_navigation_post_detail
            findNavController().navigate(action, args)
        }

        mAdapter.setOnFavClickListener { post ->
            Toast.makeText(this@HomeFragment.context, post.content, Toast.LENGTH_SHORT).show()
        }

        mAdapter.setOnPersonClickListener { post ->
            val args = bundleOf(ProfileFragment.KEY_PROFILE_UID to post.uid)
            val action = R.id.action_navigation_home_to_navigation_profile
            findNavController().navigate(action, args)
        }
    }

    private fun getPosts() {
        root.homeSwipeRefreshLayout.isRefreshing = true
        val uid = FirebaseAuthHelper.getCurrentUserId()
        viewModel.getPosts(uid).observe(viewLifecycleOwner, Observer {
            mAdapter.submitList(it)
            root.homeSwipeRefreshLayout.isRefreshing = false
            root.homeNewPostFab.makeVisible()
        })
    }
}
