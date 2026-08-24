package com.ngaming.ngamingcase.posts.ui.list

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.google.android.material.snackbar.Snackbar
import com.ngaming.ngamingcase.core.ui.base.BaseFragment
import com.ngaming.ngamingcase.core.ui.ext.applySystemBarInsets
import com.ngaming.ngamingcase.core.ui.ext.collectWhileStarted
import com.ngaming.ngamingcase.core.ui.ext.toMessage
import com.ngaming.ngamingcase.posts.ui.R
import com.ngaming.ngamingcase.posts.ui.databinding.FragmentPostListBinding
import com.ngaming.ngamingcase.posts.ui.detail.PostDetailFragment
import com.ngaming.ngamingcase.posts.ui.model.PostUiModel
import dagger.hilt.android.AndroidEntryPoint
import com.ngaming.ngamingcase.core.ui.R as CoreUiR

/** Gönderi listesi ekranı. */
@AndroidEntryPoint
class PostListFragment : BaseFragment<FragmentPostListBinding>(
    R.layout.fragment_post_list,
    FragmentPostListBinding::bind,
) {

    private val viewModel: PostListViewModel by viewModels()

    override fun onViewReady(savedInstanceState: Bundle?) {
        val adapter = PostListAdapter(onPostClick = ::openDetail)
        setUpList(adapter)

        binding.swipeRefresh.setOnRefreshListener { viewModel.onRefresh() }
        binding.retryButton.setOnClickListener { viewModel.onRefresh() }

        collectWhileStarted(viewModel.uiState) { render(it, adapter) }
        collectWhileStarted(viewModel.events, ::handleEvent)
    }

    override fun onDestroyView() {
        binding.postList.adapter = null
        super.onDestroyView()
    }

    private fun setUpList(adapter: PostListAdapter) = with(binding.postList) {
        setAdapter(adapter)
        setHasFixedSize(true)
        applySystemBarInsets(bottom = true)

        addItemDecoration(
            MaterialDividerItemDecoration(context, MaterialDividerItemDecoration.VERTICAL).apply {
                isLastItemDecorated = false
                dividerInsetStart = resources.getDimensionPixelSize(CoreUiR.dimen.spacing_medium)
                dividerInsetEnd = resources.getDimensionPixelSize(CoreUiR.dimen.spacing_medium)
            },
        )

        val swipeCallback = SwipeToDeleteCallback(this) { position ->
            adapter.currentList.getOrNull(position)?.let { viewModel.onPostSwiped(it.id) }
        }
        ItemTouchHelper(swipeCallback).attachToRecyclerView(this)
    }

    private fun render(state: PostListUiState, adapter: PostListAdapter) = with(binding) {
        progress.isVisible = state.isLoading
        swipeRefresh.isRefreshing = state.isRefreshing
        emptyState.isVisible = state.showEmptyState
        adapter.submitList(state.posts)
    }

    private fun handleEvent(event: PostListEvent) = when (event) {
        is PostListEvent.PostDeleted -> Snackbar
            .make(binding.root, R.string.post_deleted, Snackbar.LENGTH_LONG)
            .setAction(CoreUiR.string.action_undo) {
                viewModel.onUndoDelete(event.post, event.position)
            }
            .show()

        is PostListEvent.Failed -> Snackbar
            .make(binding.root, event.error.toMessage(requireContext()), Snackbar.LENGTH_LONG)
            .show()
    }

    private fun openDetail(post: PostUiModel, position: Int) {
        val navController = findNavController()
        if (navController.currentDestination?.id != R.id.postListFragment) return
        navController.navigate(R.id.action_list_to_detail, PostDetailFragment.argsOf(post.id, position))
    }
}
