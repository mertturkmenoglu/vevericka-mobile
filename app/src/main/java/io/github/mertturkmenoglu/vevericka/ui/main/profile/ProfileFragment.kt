package io.github.mertturkmenoglu.vevericka.ui.main.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import io.github.mertturkmenoglu.vevericka.R
import io.github.mertturkmenoglu.vevericka.data.model.User
import io.github.mertturkmenoglu.vevericka.ui.main.home.posts.PostAdapter
import io.github.mertturkmenoglu.vevericka.util.FirebaseAuthHelper
import io.github.mertturkmenoglu.vevericka.util.StorageHelper
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
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

        if (uid != FirebaseAuthHelper.getCurrentUserId()) {
            profileEditProfileButton.isClickable = false
            profileEditProfileButton.visibility = GONE
        }

        profileEditProfileButton.setOnClickListener {
            editProfile(uid)
        }
    }

    private fun initRecyclerView() {
        val ctx = activity?.applicationContext ?: throw IllegalStateException()
        mRoot.profileRecyclerView.layoutManager = LinearLayoutManager(ctx)
        mAdapter = PostAdapter(ctx)
        mRoot.profileRecyclerView.adapter = mAdapter
        mAdapter.setPostClickListener {
            Toast.makeText(ctx, "Clicked", Toast.LENGTH_SHORT).show()
        }
    }

    private fun editProfile(uid: String) {

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