package com.dicoding.sahabatgula.ui.navigation_ui.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.sahabatgula.data.remote.response.DataItem
import com.dicoding.sahabatgula.databinding.FragmentArticleBinding

class ArticleFragment : Fragment() {

    private lateinit var binding: FragmentArticleBinding
    private lateinit var adapter: ArticleAdapter
    private val articleViewModel by viewModels<ArticleViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArticleBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ArticleAdapter()

        if(articleViewModel.listArticle.value.isNullOrEmpty()){
            articleViewModel.setListArticle()
        }

        articleViewModel.listArticle.observe(viewLifecycleOwner){ listArticle ->
            setArticleData(listArticle)
        }

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvArticle.layoutManager = layoutManager

        binding.rvArticle.adapter = adapter
    }

    private fun setArticleData(article: List<DataItem>?) {
        adapter.submitList(article)
    }


}
