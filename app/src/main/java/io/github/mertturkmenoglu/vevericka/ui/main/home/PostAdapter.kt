package io.github.mertturkmenoglu.vevericka.ui.main.home

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
import io.github.mertturkmenoglu.vevericka.data.model.Post
import io.github.mertturkmenoglu.vevericka.data.model.User
import io.github.mertturkmenoglu.vevericka.util.FirestoreHelper
import io.github.mertturkmenoglu.vevericka.util.StorageHelper
import kotlinx.android.synthetic.main.item_home_post.view.*

class PostAdapter(private val context: Context) :
    ListAdapter<Post, PostAdapter.PostViewHolder>(DIFF_CALLBACK) {
    private companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Post>() {
            override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem.imageUrl == newItem.imageUrl
            }

            override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        val favCount = String.format(context.getString(R.string.post_item_likes), post.likeCount)
        val comment =
            String.format(context.getString(R.string.post_item_comments), post.comments.size)
        holder.content.text = post.content
        holder.favorites.text = favCount
        holder.comments.text = comment

        FirestoreHelper.getUser(post.uid).addOnSuccessListener {
            val user = it.toObject<User>()
            holder.fullName.text = user?.getFullName()

            Glide.with(context)
                .load(post.imageUrl)
                .into(holder.image)

            StorageHelper.getPictureDownloadUrl(post.uid).addOnSuccessListener { uri ->
                Glide.with(context)
                    .load(uri)
                    .apply(RequestOptions().circleCrop())
                    .into(holder.profilePicture)
            }
        }
    }

    inner class PostViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val profilePicture: ImageView = view.postItemProfilePicture
        val content: TextView = view.postItemContent
        val fullName: TextView = view.postItemUserFullName
        val image: ImageView = view.postItemImage
        val favorites: TextView = view.postItemFavCount
        val comments: TextView = view.postItemCommentsCount
    }
}