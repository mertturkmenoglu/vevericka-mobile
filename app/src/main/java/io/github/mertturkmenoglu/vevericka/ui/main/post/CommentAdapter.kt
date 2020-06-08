package io.github.mertturkmenoglu.vevericka.ui.main.post

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.firestore.ktx.toObject
import io.github.mertturkmenoglu.vevericka.R
import io.github.mertturkmenoglu.vevericka.data.model.Comment
import io.github.mertturkmenoglu.vevericka.data.model.User
import io.github.mertturkmenoglu.vevericka.interfaces.CommentClickListener
import io.github.mertturkmenoglu.vevericka.util.FirestoreHelper
import io.github.mertturkmenoglu.vevericka.util.StorageHelper
import kotlinx.android.synthetic.main.item_comment.view.*

class CommentAdapter(private val context: Context) :
    ListAdapter<Comment, CommentAdapter.CommentViewHolder>(
        DIFF_CALLBACK
    ) {
    private companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Comment>() {
            override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
                return oldItem.uid == newItem.uid && oldItem.timestamp == newItem.timestamp
            }

            override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
                return oldItem == newItem
            }
        }
    }

    private lateinit var listener: CommentClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_comment, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = getItem(position)
        holder.content.text = comment.content

        FirestoreHelper.getUser(comment.uid).addOnSuccessListener {
            val user = it.toObject<User>()
            holder.fullName.text = user?.getFullName()

            val uriTask = StorageHelper.getPictureDownloadUrl(user?.imageUrl ?: "")
            uriTask.addOnSuccessListener { uri ->
                Glide.with(context)
                    .load(uri)
                    .apply(RequestOptions().circleCrop())
                    .into(holder.profilePicture)
            }
        }
    }

    fun setPostClickListener(action: (uid: String) -> Unit) {
        this.listener = object : CommentClickListener {
            override fun onClick(uid: String) {
                action(uid)
            }
        }
    }

    inner class CommentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val profilePicture: ImageView = view.commentProfilePicture
        val content: TextView = view.commentContent
        val fullName: TextView = view.commentUserFullName

        init {
            view.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener.onClick(getItem(adapterPosition).uid)
                }
            }
        }
    }
}