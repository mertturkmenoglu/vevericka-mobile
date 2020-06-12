package io.github.mertturkmenoglu.vevericka.ui.main.friendship

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import io.github.mertturkmenoglu.vevericka.R
import io.github.mertturkmenoglu.vevericka.data.model.User
import io.github.mertturkmenoglu.vevericka.interfaces.FriendshipRequestListener
import io.github.mertturkmenoglu.vevericka.ui.main.friendship.FriendshipRequestAdapter.FriendshipRequestViewHolder
import io.github.mertturkmenoglu.vevericka.util.StorageHelper
import io.github.mertturkmenoglu.vevericka.util.UserDiffCallback
import io.github.mertturkmenoglu.vevericka.util.loadCircleImage
import kotlinx.android.synthetic.main.item_friendship_request.view.*

class FriendshipRequestAdapter :
    ListAdapter<User, FriendshipRequestViewHolder>(UserDiffCallback.callback) {

    private lateinit var approveListener: FriendshipRequestListener.OnApproveListener
    private lateinit var dismissListener: FriendshipRequestListener.OnDismissListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendshipRequestViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_friendship_request, parent, false)
        return FriendshipRequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: FriendshipRequestViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }

    fun setOnApproveListener(action: (user: User) -> Unit) {
        this.approveListener = object : FriendshipRequestListener.OnApproveListener {
            override fun onApprove(user: User) {
                action(user)
            }
        }
    }

    fun setOnDismissListener(action: (user: User) -> Unit) {
        this.dismissListener = object : FriendshipRequestListener.OnDismissListener {
            override fun onDismiss(user: User) {
                action(user)
            }
        }
    }

    inner class FriendshipRequestViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val profilePicture: ImageView = view.friendshipRequestItemProfileImage
        private val fullName: TextView = view.friendshipRequestItemFullName
        private val approveBtn: MaterialButton = view.friendshipRequestItemApproveButton
        private val dismissBtn: MaterialButton = view.friendshipRequestItemDismissButton

        init {
            approveBtn.setOnClickListener {
                val isInitialized = this@FriendshipRequestAdapter::approveListener.isInitialized
                if (isInitialized && adapterPosition != RecyclerView.NO_POSITION) {
                    approveListener.onApprove(getItem(adapterPosition))
                }
            }

            dismissBtn.setOnClickListener {
                val isInitialized = this@FriendshipRequestAdapter::dismissListener.isInitialized
                if (isInitialized && adapterPosition != RecyclerView.NO_POSITION) {
                    dismissListener.onDismiss(getItem(adapterPosition))
                }
            }
        }

        fun bind(user: User) {
            fullName.text = user.getFullName()

            StorageHelper.getPictureDownloadUrl(user.imageUrl)
                .addOnSuccessListener { profilePicture.loadCircleImage(it) }
        }
    }
}