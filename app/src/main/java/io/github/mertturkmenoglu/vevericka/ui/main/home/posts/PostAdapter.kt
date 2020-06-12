package io.github.mertturkmenoglu.vevericka.ui.main.home.posts

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.toObject
import io.github.mertturkmenoglu.vevericka.R
import io.github.mertturkmenoglu.vevericka.data.model.Post
import io.github.mertturkmenoglu.vevericka.data.model.User
import io.github.mertturkmenoglu.vevericka.interfaces.PostClickListener
import io.github.mertturkmenoglu.vevericka.util.*
import kotlinx.android.synthetic.main.item_home_post.view.*

class PostAdapter(private val context: Context) :
    ListAdapter<Post, PostAdapter.PostViewHolder>(PostDiffCallback.callback) {

    private lateinit var commentClickListener: PostClickListener.OnCommentClickListener
    private lateinit var favClickListener: PostClickListener.OnFavClickListener
    private lateinit var personClickListener: PostClickListener.OnPersonClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setOnCommentClickListener(action: (post: Post) -> Unit) {
        this.commentClickListener = object : PostClickListener.OnCommentClickListener {
            override fun onCommentClick(post: Post) {
                action(post)
            }
        }
    }

    fun setOnFavClickListener(action: (post: Post) -> Unit) {
        this.favClickListener = object : PostClickListener.OnFavClickListener {
            override fun onFavClick(post: Post) {
                action(post)
            }
        }
    }

    fun setOnPersonClickListener(action: (post: Post) -> Unit) {
        this.personClickListener = object : PostClickListener.OnPersonClickListener {
            override fun onPersonClick(post: Post) {
                action(post)
            }
        }
    }

    inner class PostViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val profilePicture: ImageView = view.postItemProfilePicture
        private val content: TextView = view.postItemContent
        private val fullName: TextView = view.postItemUserFullName
        private val image: ImageView = view.postItemImage
        private val favorites: TextView = view.postItemFavCount
        private val comments: TextView = view.postItemCommentsCount
        private val commentsIcon: ImageButton = view.postItemCommentsButton
        private val favIcon: ImageButton = view.postItemFavButton

        init {
            setClickListeners()
        }

        fun bind(post: Post) {
            val favText = context.getString(R.string.post_item_likes)
            val commentText = context.getString(R.string.post_item_comments)

            content.text = post.content
            favorites.text = String.format(favText, post.likeCount)
            comments.text = String.format(commentText, post.comments.size)

            FirestoreHelper.getUserAsTask(post.uid).addOnSuccessListener {
                val user = it.toObject<User>()
                fullName.text = user?.getFullName()
                image.loadImage(post.imageUrl)

                StorageHelper.getPictureDownloadUrl(post.uid)
                    .addOnSuccessListener { uri -> profilePicture.loadCircleImage(uri) }
            }
        }

        private fun setClickListeners() {
            setOnCommentClickListeners()
            setOnPersonClickListeners()
            setOnFavClickListeners()
        }

        private fun setOnCommentClickListeners() {
            comments.setOnClickListener {
                val isInitialized = this@PostAdapter::commentClickListener.isInitialized
                if (isInitialized && adapterPosition != RecyclerView.NO_POSITION) {
                    commentClickListener.onCommentClick(getItem(adapterPosition))
                }
            }

            commentsIcon.setOnClickListener {
                val isInitialized = this@PostAdapter::commentClickListener.isInitialized
                if (isInitialized && adapterPosition != RecyclerView.NO_POSITION) {
                    commentClickListener.onCommentClick(getItem(adapterPosition))
                }
            }
        }

        private fun setOnPersonClickListeners() {
            profilePicture.setOnClickListener {
                val isInitialized = this@PostAdapter::personClickListener.isInitialized
                if (isInitialized && adapterPosition != RecyclerView.NO_POSITION) {
                    personClickListener.onPersonClick(getItem(adapterPosition))
                }
            }

            fullName.setOnClickListener {
                val isInitialized = this@PostAdapter::personClickListener.isInitialized
                if (isInitialized && adapterPosition != RecyclerView.NO_POSITION) {
                    personClickListener.onPersonClick(getItem(adapterPosition))
                }
            }
        }

        private fun setOnFavClickListeners() {
            favIcon.setOnClickListener {
                val isInitialized = this@PostAdapter::favClickListener.isInitialized
                if (isInitialized && adapterPosition != RecyclerView.NO_POSITION) {
                    favClickListener.onFavClick(getItem(adapterPosition))
                }
            }
        }
    }
}