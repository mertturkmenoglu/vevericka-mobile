package io.github.mertturkmenoglu.vevericka.ui.main.friends

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
import io.github.mertturkmenoglu.vevericka.R
import io.github.mertturkmenoglu.vevericka.data.model.User
import io.github.mertturkmenoglu.vevericka.interfaces.PersonClickListener
import io.github.mertturkmenoglu.vevericka.util.StorageHelper
import kotlinx.android.synthetic.main.item_person.view.*

class PersonAdapter(private val context: Context) :
    ListAdapter<User, PersonAdapter.PersonViewHolder>(DIFF_CALLBACK) {

    private companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.imageUrl == newItem.imageUrl
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem == newItem
            }
        }
    }

    private lateinit var listener: PersonClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_person, parent, false)
        return PersonViewHolder(view)
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        val user = getItem(position)
        holder.fullName.text = user.getFullName()

        StorageHelper.getPictureDownloadUrl(user.imageUrl).addOnSuccessListener { uri ->
            Glide.with(context)
                .load(uri)
                .apply(RequestOptions().circleCrop())
                .into(holder.profilePicture)
        }
    }


    fun setPersonClickListener(action: (user: User) -> Unit) {
        this.listener = object : PersonClickListener {
            override fun onClick(user: User) {
                action(user)
            }
        }
    }

    inner class PersonViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val profilePicture: ImageView = view.personItemProfileImage
        val fullName: TextView = view.personItemFullName

        init {
            view.setOnClickListener {
                val isInitialized = this@PersonAdapter::listener.isInitialized
                if (isInitialized && adapterPosition != RecyclerView.NO_POSITION) {
                    listener.onClick(getItem(adapterPosition))
                }
            }
        }
    }

}