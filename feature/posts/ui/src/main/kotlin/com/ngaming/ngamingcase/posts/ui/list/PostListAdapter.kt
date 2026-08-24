package com.ngaming.ngamingcase.posts.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ngaming.ngamingcase.core.ui.ext.clearThumbnail
import com.ngaming.ngamingcase.core.ui.ext.loadThumbnail
import com.ngaming.ngamingcase.posts.ui.databinding.ItemPostBinding
import com.ngaming.ngamingcase.posts.ui.model.PostUiModel
import com.ngaming.ngamingcase.posts.ui.model.postImageUrl

/** Liste satırlarını çiziyor. DiffUtil sayesinde sadece değişen satır güncelleniyor. */
class PostListAdapter(
    private val onPostClick: (post: PostUiModel, position: Int) -> Unit,
) : ListAdapter<PostUiModel, PostListAdapter.PostViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding) { position -> onPostClick(getItem(position), position) }
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) =
        holder.bind(getItem(position), position)

    override fun onViewRecycled(holder: PostViewHolder) = holder.recycle()

    class PostViewHolder(
        private val binding: ItemPostBinding,
        onClick: (position: Int) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                bindingAdapterPosition.takeIf { it != RecyclerView.NO_POSITION }?.let(onClick)
            }
        }

        fun bind(post: PostUiModel, position: Int) = with(binding) {
            title.text = post.title
            body.text = post.body
            thumbnail.loadThumbnail(postImageUrl(position))
        }

        fun recycle() = binding.thumbnail.clearThumbnail()
    }

    private object DiffCallback : DiffUtil.ItemCallback<PostUiModel>() {
        override fun areItemsTheSame(oldItem: PostUiModel, newItem: PostUiModel) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: PostUiModel, newItem: PostUiModel) =
            oldItem == newItem
    }
}
