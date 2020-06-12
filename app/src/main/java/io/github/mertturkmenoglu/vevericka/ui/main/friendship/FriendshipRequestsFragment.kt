package io.github.mertturkmenoglu.vevericka.ui.main.friendship

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.mertturkmenoglu.vevericka.R
import io.github.mertturkmenoglu.vevericka.util.FirebaseAuthHelper
import kotlinx.android.synthetic.main.fragment_friendship_requests.view.*
import org.jetbrains.anko.design.snackbar

class FriendshipRequestsFragment : Fragment() {
    private lateinit var viewModel: FriendshipRequestsViewModel
    private lateinit var mRoot: View
    private lateinit var mAdapter: FriendshipRequestAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(FriendshipRequestsViewModel::class.java)
        mRoot = inflater.inflate(R.layout.fragment_friendship_requests, container, false)

        initRecyclerView()
        getFriendshipRequests()

        mRoot.friendshipRequestsSwipeRefreshLayout.setOnRefreshListener(::getFriendshipRequests)

        return mRoot
    }

    private fun initRecyclerView() {
        val ctx = activity?.applicationContext ?: throw IllegalStateException()
        mRoot.friendshipRequestsRecyclerView.layoutManager = LinearLayoutManager(ctx)
        mAdapter = FriendshipRequestAdapter()
        mRoot.friendshipRequestsRecyclerView.adapter = mAdapter

        mAdapter.setOnApproveListener {
            viewModel.approveFriendshipRequest(FirebaseAuthHelper.getCurrentUserId(), it.imageUrl)
            mRoot.snackbar("Friendship request approved")
        }

        mAdapter.setOnDismissListener {
            viewModel.dismissFriendshipRequest(FirebaseAuthHelper.getCurrentUserId(), it.imageUrl)
            mRoot.snackbar("Friendship request dismissed")
        }
    }

    private fun getFriendshipRequests() {
        mRoot.friendshipRequestsSwipeRefreshLayout.isRefreshing = true
        val uid = FirebaseAuthHelper.getCurrentUserId()

        viewModel.getFriendshipRequests(uid).observe(viewLifecycleOwner, Observer {
            mAdapter.submitList(it)
            mRoot.friendshipRequestsSwipeRefreshLayout.isRefreshing = false
        })
    }
}