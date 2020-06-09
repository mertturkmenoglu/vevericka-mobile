package io.github.mertturkmenoglu.vevericka.ui.main.friends

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
import io.github.mertturkmenoglu.vevericka.util.FirebaseAuthHelper
import kotlinx.android.synthetic.main.fragment_friends.view.*

class FriendsFragment : Fragment() {
    private lateinit var friendsViewModel: FriendsViewModel
    private lateinit var mRoot: View
    private lateinit var mAdapter: PersonAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        friendsViewModel = ViewModelProvider(this).get(FriendsViewModel::class.java)

        mRoot = inflater.inflate(R.layout.fragment_friends, container, false)
        mRoot.friendsAddFriendFab.visibility = View.INVISIBLE

        initRecyclerView()
        getFriends()

        mRoot.friendsSwipeRefreshLayout.setOnRefreshListener(::getFriends)

        mRoot.friendsAddFriendFab.setOnClickListener {
            addFriend()
        }

        return mRoot
    }

    private fun initRecyclerView() {
        val ctx = activity?.applicationContext ?: throw IllegalStateException()
        mRoot.friendsRecyclerView.layoutManager = LinearLayoutManager(ctx)
        mAdapter = PersonAdapter(ctx)
        mRoot.friendsRecyclerView.adapter = mAdapter
        mAdapter.setPersonClickListener {
            Toast.makeText(ctx, "Clicked", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getFriends() {
        mRoot.friendsSwipeRefreshLayout.isRefreshing = true
        val uid = FirebaseAuthHelper.getCurrentUserId()
        friendsViewModel.getFriends(uid).observe(viewLifecycleOwner, Observer {
            mAdapter.submitList(it)
            mRoot.friendsSwipeRefreshLayout.isRefreshing = false
            mRoot.friendsAddFriendFab.visibility = View.VISIBLE
        })
    }

    private fun addFriend() {
        Toast.makeText(context, "Add friend clicked", Toast.LENGTH_SHORT).show()
    }
}