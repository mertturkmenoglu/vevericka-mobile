package io.github.mertturkmenoglu.vevericka.ui.main.post

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import io.github.mertturkmenoglu.vevericka.R
import io.github.mertturkmenoglu.vevericka.data.model.Comment
import io.github.mertturkmenoglu.vevericka.data.model.Post
import io.github.mertturkmenoglu.vevericka.ui.main.home.HomeFragment
import io.github.mertturkmenoglu.vevericka.ui.main.profile.ProfileFragment
import io.github.mertturkmenoglu.vevericka.util.FirebaseAuthHelper
import kotlinx.android.synthetic.main.fragment_post_detail.view.*
import org.jetbrains.anko.customView
import org.jetbrains.anko.editText
import org.jetbrains.anko.singleLine
import org.jetbrains.anko.support.v4.alert

class PostDetailFragment : Fragment() {
    private lateinit var postDetailViewModel: PostDetailViewModel
    private lateinit var root: View
    private lateinit var mAdapter: CommentAdapter
    private lateinit var mPost: Post

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        bundle: Bundle?
    ): View? {
        postDetailViewModel = ViewModelProvider(this).get(PostDetailViewModel::class.java)
        root = inflater.inflate(R.layout.fragment_post_detail, container, false)
        root.postDetailNewCommentFab.visibility = View.INVISIBLE

        val postJson = arguments?.getString(HomeFragment.KEY_POST)
        mPost = Gson().fromJson(postJson, Post::class.java)

        initRecyclerView()
        getComments()

        root.postDetailSwipeRefreshLayout.setOnRefreshListener(::getComments)

        root.postDetailNewCommentFab.setOnClickListener {
            newComment(it)
        }

        return root
    }

    private fun initRecyclerView() {
        val ctx = activity?.applicationContext ?: throw IllegalStateException()
        root.postDetailCommentsRecyclerView.layoutManager = LinearLayoutManager(ctx)
        mAdapter = CommentAdapter(ctx)

        root.postDetailCommentsRecyclerView.adapter = mAdapter
        mAdapter.setPostClickListener {
            val args = bundleOf(ProfileFragment.KEY_PROFILE_UID to it)
            findNavController().navigate(
                R.id.action_navigation_post_detail_to_navigation_profile,
                args
            )
        }
    }

    private fun getComments() {
        root.postDetailSwipeRefreshLayout.isRefreshing = true

        postDetailViewModel.getComments(mPost).observe(viewLifecycleOwner, Observer {
            mAdapter.submitList(it)
            root.postDetailSwipeRefreshLayout.isRefreshing = false
            root.postDetailNewCommentFab.visibility = View.VISIBLE
        })
    }

    private fun newComment(view: View) {
        alert {
            title = getString(R.string.new_comment)

            customView {
                val input = editText {
                    hint = context.getString(R.string.comment)
                    singleLine = false
                    inputType = InputType.TYPE_CLASS_TEXT
                }

                positiveButton(getString(R.string.comment_dialog_new_comment)) {
                    val uid = FirebaseAuthHelper.getCurrentUserId()
                    val content = input.text?.toString()?.trim() ?: ""
                    val comment = Comment(uid, content)
                    postDetailViewModel.addNewComment(mPost, comment)
                }
            }
        }.show()
    }
}