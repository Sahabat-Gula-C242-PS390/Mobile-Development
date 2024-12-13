package com.dicoding.sahabatgula.ui.navigation_ui.article

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.sahabatgula.R
import com.dicoding.sahabatgula.data.remote.response.DataItem
import com.dicoding.sahabatgula.databinding.ItemArticleBinding

class ArticleAdapter: ListAdapter<DataItem, ArticleAdapter.MyViewHolder>(DIFF_CALLBACK) {

    class MyViewHolder(private val binding: ItemArticleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(viewCard: DataItem) {
            binding.titleArticle.text = viewCard.title
            binding.tinjauArticle.text = viewCard.subtitle
            Glide.with(binding.root.context)
                .load(viewCard.imageUrl)
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.broken_image)
                .into(binding.imgItemPhoto)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val article = getItem(position)
        holder.bind(article)

        holder.itemView.setOnClickListener{
            val context = holder.itemView.context
            val intent = Intent(context, DetailArticleActivity::class.java).apply {
                putExtra("id", article.articleId)
                putExtra("link", article.originalLink)
            }
            context.startActivity(intent)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItem>() {
            override fun areItemsTheSame(
                oldItem: DataItem,
                newItem: DataItem
            ): Boolean {
                return oldItem.articleId == newItem.articleId
            }

            override fun areContentsTheSame(
                oldItem: DataItem,
                newItem: DataItem
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}
