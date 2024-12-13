package com.dicoding.sahabatgula.ui.navigation_ui.article
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.dicoding.sahabatgula.R
import com.dicoding.sahabatgula.data.remote.response.Article
import com.dicoding.sahabatgula.databinding.ActivityDetailArticleBinding

class DetailArticleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailArticleBinding
    private val detailArticleViewModel by viewModels<DetailArticleViewModel>()
    private var articleId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setActionBar()

        articleId = intent.getStringExtra("id")
        val articleLink = intent.getStringExtra("link")

        if (!articleId.isNullOrEmpty()) {
            obeserveViewModel()
            detailArticleViewModel.setArticleData(articleId)
        }

        binding.btnSee.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(articleLink))
            startActivity(intent)
        }
    }

    private fun obeserveViewModel() {
        detailArticleViewModel.article.observe(this) { article ->
            if (article != null) {
                setArticleData(article)
            } else {
                Toast.makeText(this, "Article not found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setArticleData(article: Article) {
        binding.apply {
            titleArticle.text = article.title
            tinjauArticle.text = article.subtitle
            dataArticle.text = HtmlCompat.fromHtml(article.content ?: "", HtmlCompat.FROM_HTML_MODE_LEGACY)

            Glide.with(this@DetailArticleActivity)
                .load(article.imageUrl)
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.broken_image)
                .into(imgItemPhoto)
        }
    }

    private fun setActionBar() {
        supportActionBar?.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.white_bg_action_bar))
        supportActionBar?.elevation = 0f
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.apply {
            val textView = TextView(this@DetailArticleActivity).apply {
                text = "Kembali"
                textSize = 16f
                setTypeface(typeface, Typeface.NORMAL)
                setTextColor(ContextCompat.getColor(context, R.color.black))
                setBackgroundColor(ContextCompat.getColor(context, R.color.white))
                gravity = Gravity.START
            }
            setCustomView(
                textView,
                ActionBar.LayoutParams(
                    ActionBar.LayoutParams.MATCH_PARENT,
                    ActionBar.LayoutParams.WRAP_CONTENT,
                    Gravity.START
                )
            )
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
