package io.github.mertturkmenoglu.vevericka.ui.main.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import io.github.mertturkmenoglu.vevericka.R
import io.github.mertturkmenoglu.vevericka.data.model.User
import io.github.mertturkmenoglu.vevericka.util.FirebaseAuthHelper
import io.github.mertturkmenoglu.vevericka.util.StorageHelper
import kotlinx.android.synthetic.main.fragment_profile.*
import org.jetbrains.anko.design.snackbar

class ProfileFragment : Fragment() {
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_profile, container, false)

        val uid = FirebaseAuthHelper.getCurrentUserId()
        profileViewModel.getUser(uid).observe(viewLifecycleOwner, Observer {
            initViews(uid, it)
        })

        return root
    }

    private fun initViews(uid: String, user: User) {
        loadProfileImage(uid)
        setFullNameTextView(user)
        setBio(user)
        setLocation(user)
        setWebsite(user)

        profilePostsButton.setOnClickListener {
            it.snackbar(getString(R.string.profile_posts))
        }

        profileFriendsButton.setOnClickListener {
            it.snackbar(getString(R.string.profile_friends))
        }
    }

    private fun loadProfileImage(uid: String) {
        StorageHelper.getProfilePictureUrl(uid).addOnSuccessListener {
            Glide.with(this)
                .load(it)
                .override(125, 125)
                .apply(RequestOptions().circleCrop())
                .into(profileProfileImage)
        }
    }

    private fun setFullNameTextView(user: User) {
        profileUserFullName.text = user.getFullName()
    }

    private fun setBio(user: User) {
        profileUserBio.text = user.bio
    }

    private fun setLocation(user: User) {
        val locationText = String.format(getString(R.string.profile_user_location), user.location)
        profileUserLocation.text = locationText
    }

    private fun setWebsite(user: User) {
        val websiteText = String.format(getString(R.string.profile_user_website), user.website)
        profileUserWebsite.text = websiteText
    }
}