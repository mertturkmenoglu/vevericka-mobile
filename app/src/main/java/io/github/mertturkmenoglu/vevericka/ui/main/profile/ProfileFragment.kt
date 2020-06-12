package io.github.mertturkmenoglu.vevericka.ui.main.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import io.github.mertturkmenoglu.vevericka.R
import io.github.mertturkmenoglu.vevericka.data.model.User
import io.github.mertturkmenoglu.vevericka.ui.main.home.HomeFragment
import io.github.mertturkmenoglu.vevericka.ui.main.home.posts.PostAdapter
import io.github.mertturkmenoglu.vevericka.util.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.jetbrains.anko.design.snackbar

class ProfileFragment : Fragment() {
    companion object {
        const val KEY_PROFILE_UID = "uid"
    }

    private lateinit var viewModel: ProfileViewModel
    private lateinit var mRoot: View
    private lateinit var mAdapter: PostAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        mRoot = inflater.inflate(R.layout.fragment_profile, container, false)
        initRecyclerView()

        val uid = getUidFromArguments(arguments)
        viewModel.getUser(uid).observe(viewLifecycleOwner, Observer {
            initViews(uid, it)
        })

        getPosts(uid)

        return mRoot
    }

    private fun getUidFromArguments(arguments: Bundle?): String {
        return arguments?.getString(KEY_PROFILE_UID) ?: FirebaseAuthHelper.getCurrentUserId()
    }

    private fun initViews(uid: String, user: User) {
        loadProfileImage(uid)
        setFullNameTextView(user)
        setBio(user)
        setLocation(user)
        setWebsite(user)

        profileFriendsButton.setOnClickListener {
            it.snackbar(getString(R.string.profile_friends))
        }

        setActionFab(uid, user)
    }

    private fun setActionFab(uid: String, user: User) {
        val currentUid = FirebaseAuthHelper.getCurrentUserId()
        if (uid == currentUid) {
            setActionFabToCurrent(uid)
            return
        }

        if (user.isFriendWith(currentUid)) {
            setActionFabToGone()
        } else {
            setActionFabToFriend(currentUid, uid)
        }
    }

    private fun setActionFabToGone() {
        profileActionFab.makeNonClickable()
        profileActionFab.makeGone()
    }

    private fun setActionFabToFriend(currentUid: String, otherUid: String) {
        with(profileActionFab) {
            makeClickable()
            makeVisible()

            text = getString(R.string.friends_add_friend)
            setIconResource(R.drawable.ic_round_add_white_24dp)

            setOnClickListener {
                lifecycleScope.launch(Dispatchers.IO) {
                    sendFriendshipRequest(currentUid, otherUid)
                }
            }
        }
    }

    private fun setActionFabToCurrent(uid: String) = with(profileActionFab) {
        makeClickable()
        makeVisible()
        text = getString(R.string.edit_profile)
        setIconResource(R.drawable.ic_round_edit_white_24p)
        setOnClickListener { editProfile(uid) }
    }

    private fun initRecyclerView() {
        val ctx = activity?.applicationContext ?: throw IllegalStateException()
        mRoot.profileRecyclerView.layoutManager = LinearLayoutManager(ctx)
        mAdapter = PostAdapter(ctx)
        mRoot.profileRecyclerView.adapter = mAdapter

        mAdapter.setOnCommentClickListener { post ->
            val args = bundleOf(HomeFragment.KEY_POST to Gson().toJson(post))
            val action = R.id.action_navigation_profile_to_navigation_post_detail
            findNavController().navigate(action, args)
        }

        mAdapter.setOnFavClickListener { post ->
            Toast.makeText(ctx, post.content, Toast.LENGTH_SHORT).show()
        }

        mAdapter.setOnPersonClickListener { post ->
            val args = bundleOf(KEY_PROFILE_UID to post.uid)
            val action = R.id.action_navigation_profile_self
            findNavController().navigate(action, args)
        }
    }

    private fun editProfile(uid: String) {

    }

    private suspend fun sendFriendshipRequest(from: String, to: String) {
        viewModel.sendFriendshipRequest(from, to)
    }

    private fun getPosts(uid: String) {
        viewModel.getPosts(uid).observe(viewLifecycleOwner, Observer {
            mAdapter.submitList(it)
        })
    }

    private fun loadProfileImage(uid: String) {
        val imageWidth = resources.getDimension(R.dimen.profile_profile_image_width).toInt()
        val imageHeight = resources.getDimension(R.dimen.profile_profile_image_height).toInt()

        lifecycleScope.launch(Dispatchers.IO) {
            val uri = StorageHelper.getPictureDownloadUrl(uid).await()
            profileProfileImage.loadCircleImage(uri, imageWidth, imageHeight)
        }
    }

    private fun setFullNameTextView(user: User) {
        profileUserFullName.text = user.getFullName()
    }

    private fun setBio(user: User) {
        profileUserBio.text = user.bio
        profileUserBio.visibility = if (user.bio.isNotBlank()) VISIBLE else GONE
    }

    private fun setLocation(user: User) {
        val locationText = String.format(getString(R.string.profile_user_location), user.location)
        profileUserLocation.text = locationText
        profileUserLocation.visibility = if (user.location.isNotBlank()) VISIBLE else GONE
    }

    private fun setWebsite(user: User) {
        val websiteText = String.format(getString(R.string.profile_user_website), user.website)
        profileUserWebsite.text = websiteText
        profileUserWebsite.visibility = if (user.website.isNotBlank()) VISIBLE else GONE
    }
}