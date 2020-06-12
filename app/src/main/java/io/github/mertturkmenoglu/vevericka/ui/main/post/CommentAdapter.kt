package io.github.mertturkmenoglu.vevericka.ui.main.post

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.toObject
import io.github.mertturkmenoglu.vevericka.R
import io.github.mertturkmenoglu.vevericka.data.model.Comment
import io.github.mertturkmenoglu.vevericka.data.model.User
import io.github.mertturkmenoglu.vevericka.interfaces.CommentClickListener
import io.github.mertturkmenoglu.vevericka.util.CommentDiffCallback
import io.github.mertturkmenoglu.vevericka.util.FirestoreHelper
import io.github.mertturkmenoglu.vevericka.util.StorageHelper
import io.github.mertturkmenoglu.vevericka.util.loadCircleImage
import kotlinx.android.synthetic.main.item_comment.view.*

class CommentAdapter :
    ListAdapter<Comment, CommentAdapter.CommentViewHolder>(CommentDiffCallback.callback) {

    private lateinit var listener: CommentClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_comment, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = getItem(position)
        holder.bind(comment)
    }

    fun setCommentClickListener(action: (uid: String) -> Unit) {
        this.listener.onClick(action)
    }

    inner class CommentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val profilePicture: ImageView = view.commentProfilePicture
        private val content: TextView = view.commentContent
        private val fullName: TextView = view.commentUserFullName

        init {
            view.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener.onClick(getItem(adapterPosition).uid)
                }
            }
        }

        fun bind(comment: Comment) {
            content.text = comment.content

            FirestoreHelper.getUserAsTask(comment.uid).addOnSuccessListener {
                val user = it.toObject<User>()
                fullName.text = user?.getFullName()

                StorageHelper.getPictureDownloadUrl(user?.imageUrl ?: "")
                    .addOnSuccessListener { uri -> profilePicture.loadCircleImage(uri) }
            }
        }
    }
}