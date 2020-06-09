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
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import io.github.mertturkmenoglu.vevericka.R
import io.github.mertturkmenoglu.vevericka.data.model.Post
import io.github.mertturkmenoglu.vevericka.data.model.User
import io.github.mertturkmenoglu.vevericka.interfaces.PostClickListener
import io.github.mertturkmenoglu.vevericka.ui.main.home.HomeFragment
import io.github.mertturkmenoglu.vevericka.ui.main.home.posts.PostAdapter
import io.github.mertturkmenoglu.vevericka.util.FirebaseAuthHelper
import io.github.mertturkmenoglu.vevericka.util.StorageHelper
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.anko.design.snackbar

class ProfileFragment : Fragment() {
    companion object {
        const val KEY_PROFILE_UID = "uid"
    }

    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var mRoot: View
    private lateinit var mAdapter: PostAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        mRoot = inflater.inflate(R.layout.fragment_profile, container, false)
        initRecyclerView()

        val uid = arguments?.getString(KEY_PROFILE_UID) ?: FirebaseAuthHelper.getCurrentUserId()
        profileViewModel.getUser(uid).observe(viewLifecycleOwner, Observer {
            initViews(uid, it)
        })

        getPosts(uid)

        return mRoot
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

        val currentUid = FirebaseAuthHelper.getCurrentUserId()
        if (uid != currentUid) {
            if (user.isFriendWith(currentUid)) {
                setActionFabToGone()
            } else {
                setActionFabToFriend(currentUid, uid)
            }
        } else {
            setActionFabToCurrent(uid)
        }
    }

    private fun setActionFabToGone() {
        profileActionFab.isClickable = false
        profileActionFab.visibility = GONE
    }

    private fun setActionFabToFriend(currentUid: String, otherUid: String) {
        profileActionFab.isClickable = true
        profileActionFab.visibility = VISIBLE
        profileActionFab.text = getString(R.string.friends_add_friend)
        profileActionFab.setIconResource(R.drawable.ic_round_add_white_24dp)
        profileActionFab.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                sendFriendshipRequest(currentUid, otherUid)
            }
        }
    }

    private fun setActionFabToCurrent(uid: String) {
        profileActionFab.isClickable = true
        profileActionFab.visibility = VISIBLE
        profileActionFab.text = getString(R.string.edit_profile)
        profileActionFab.setIconResource(R.drawable.ic_round_edit_white_24p)
        profileActionFab.setOnClickListener {
            editProfile(uid)
        }
    }

    private fun initRecyclerView() {
        val ctx = activity?.applicationContext ?: throw IllegalStateException()
        mRoot.profileRecyclerView.layoutManager = LinearLayoutManager(ctx)
        mAdapter = PostAdapter(ctx)
        mRoot.profileRecyclerView.adapter = mAdapter
        mAdapter.setPostClickListener(object : PostClickListener {
            override fun onCommentClick(post: Post) {
                val args = bundleOf(HomeFragment.KEY_POST to Gson().toJson(post))
                val action = R.id.action_navigation_profile_to_navigation_post_detail
                findNavController().navigate(action, args)
            }

            override fun onFavClick(post: Post) {
                Toast.makeText(ctx, post.content, Toast.LENGTH_SHORT).show()
            }

            override fun onPersonClick(post: Post) {
                val args = bundleOf(KEY_PROFILE_UID to post.uid)
                val action = R.id.action_navigation_profile_self
                findNavController().navigate(action, args)
            }
        })
    }

    private fun editProfile(uid: String) {

    }

    private suspend fun sendFriendshipRequest(from: String, to: String) {
        profileViewModel.sendFriendshipRequest(from, to)
    }

    private fun getPosts(uid: String) {
        profileViewModel.getPosts(uid).observe(viewLifecycleOwner, Observer {
            mAdapter.submitList(it)
        })
    }

    private fun loadProfileImage(uid: String) {
        StorageHelper.getPictureDownloadUrl(uid).addOnSuccessListener {
            val imageWidth = resources.getDimension(R.dimen.profile_profile_image_width).toInt()
            val imageHeight = resources.getDimension(R.dimen.profile_profile_image_height).toInt()

            Glide.with(this)
                .load(it)
                .override(imageWidth, imageHeight)
                .apply(RequestOptions().circleCrop())
                .into(profileProfileImage)
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