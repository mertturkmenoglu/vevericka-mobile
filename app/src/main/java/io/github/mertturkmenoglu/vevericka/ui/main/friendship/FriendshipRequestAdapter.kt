package io.github.mertturkmenoglu.vevericka.ui.main.friendship

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.button.MaterialButton
import io.github.mertturkmenoglu.vevericka.R
import io.github.mertturkmenoglu.vevericka.data.model.User
import io.github.mertturkmenoglu.vevericka.interfaces.FriendshipRequestListener
import io.github.mertturkmenoglu.vevericka.util.StorageHelper
import io.github.mertturkmenoglu.vevericka.util.UserDiffCallback
import kotlinx.android.synthetic.main.item_friendship_request.view.*

class FriendshipRequestAdapter(private val context: Context) :
    ListAdapter<User, FriendshipRequestAdapter.FriendshipRequestViewHolder>(UserDiffCallback.callback) {

    private lateinit var reqListener: FriendshipRequestListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendshipRequestViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_friendship_request, parent, false)
        return FriendshipRequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: FriendshipRequestViewHolder, position: Int) {
        val user = getItem(position)

        with(holder) {
            fullName.text = user.getFullName()

            StorageHelper.getPictureDownloadUrl(user.imageUrl).addOnSuccessListener { uri ->
                Glide.with(context)
                    .load(uri)
                    .apply(RequestOptions().circleCrop())
                    .into(profilePicture)
            }
        }
    }

    fun setRequestListener(listener: FriendshipRequestListener) {
        this.reqListener = listener
    }

    inner class FriendshipRequestViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val profilePicture: ImageView = view.friendshipRequestItemProfileImage
        val fullName: TextView = view.friendshipRequestItemFullName
        val approveBtn: MaterialButton = view.friendshipRequestItemApproveButton
        val dismissBtn: MaterialButton = view.friendshipRequestItemDismissButton

        init {
            approveBtn.setOnClickListener {
                val isInitialized = this@FriendshipRequestAdapter::reqListener.isInitialized
                if (isInitialized && adapterPosition != RecyclerView.NO_POSITION) {
                    reqListener.onApprove(getItem(adapterPosition))
                }
            }

            dismissBtn.setOnClickListener {
                val isInitialized = this@FriendshipRequestAdapter::reqListener.isInitialized
                if (isInitialized && adapterPosition != RecyclerView.NO_POSITION) {
                    reqListener.onDismiss(getItem(adapterPosition))
                }
            }
        }
    }
}