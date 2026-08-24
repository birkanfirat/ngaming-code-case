package com.ngaming.ngamingcase.posts.ui.detail

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.ngaming.ngamingcase.core.common.AppError
import com.ngaming.ngamingcase.core.ui.base.BaseFragment
import com.ngaming.ngamingcase.core.ui.ext.applySystemBarInsets
import com.ngaming.ngamingcase.core.ui.ext.collectWhileStarted
import com.ngaming.ngamingcase.core.ui.ext.loadThumbnail
import com.ngaming.ngamingcase.core.ui.ext.toMessage
import com.ngaming.ngamingcase.posts.ui.R
import com.ngaming.ngamingcase.posts.ui.databinding.FragmentPostDetailBinding
import com.ngaming.ngamingcase.posts.ui.model.postImageUrl
import dagger.hilt.android.AndroidEntryPoint

/** Gönderi detay ekranı. Kalem ikonuna basınca alanlar düzenlenebilir oluyor. */
@AndroidEntryPoint
class PostDetailFragment : BaseFragment<FragmentPostDetailBinding>(
    R.layout.fragment_post_detail,
    FragmentPostDetailBinding::bind,
) {

    private val viewModel: PostDetailViewModel by viewModels()
    private var editorFilled = false
    private var postShown = false
    private var editActionVisible: Boolean? = null

    private val backCallback = object : OnBackPressedCallback(enabled = false) {
        override fun handleOnBackPressed() = viewModel.onCancelClick()
    }

    private val menuProvider = object : MenuProvider {
        override fun onCreateMenu(menu: Menu, inflater: MenuInflater) =
            inflater.inflate(R.menu.post_detail, menu)

        override fun onPrepareMenu(menu: Menu) {
            val state = viewModel.uiState.value
            menu.findItem(R.id.action_edit).isVisible = state.post != null && !state.isEditing
        }

        override fun onMenuItemSelected(item: MenuItem): Boolean {
            if (item.itemId != R.id.action_edit) return false
            viewModel.onEditClick()
            return true
        }
    }

    override fun onViewReady(savedInstanceState: Bundle?) {
        editorFilled = savedInstanceState?.getBoolean(STATE_EDITOR_FILLED) == true

        binding.content.applySystemBarInsets(bottom = true)
        binding.thumbnail.loadThumbnail(postImageUrl(requireArguments().getInt(ARG_POSITION)))
        requireActivity().addMenuProvider(menuProvider, viewLifecycleOwner, Lifecycle.State.RESUMED)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, backCallback)

        binding.titleInput.doAfterTextChanged { binding.titleLayout.error = null }
        binding.bodyInput.doAfterTextChanged { binding.bodyLayout.error = null }
        binding.cancelButton.setOnClickListener { viewModel.onCancelClick() }
        binding.saveButton.setOnClickListener {
            viewModel.onSaveClick(
                title = binding.titleInput.text?.toString().orEmpty(),
                body = binding.bodyInput.text?.toString().orEmpty(),
            )
        }

        collectWhileStarted(viewModel.uiState, ::render)
        collectWhileStarted(viewModel.events, ::handleEvent)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(STATE_EDITOR_FILLED, editorFilled)
    }

    private fun render(state: PostDetailUiState) {
        val post = state.post
        if (post == null) {
            if (postShown) findNavController().navigateUp()
            return
        }
        postShown = true

        binding.titleText.text = post.title
        binding.bodyText.text = post.body

        if (state.isEditing) {
            if (!editorFilled) {
                editorFilled = true
                binding.titleInput.setText(post.title)
                binding.bodyInput.setText(post.body)
            }
        } else {
            editorFilled = false
        }

        binding.readGroup.isVisible = !state.isEditing
        binding.editGroup.isVisible = state.isEditing
        binding.savingProgress.isVisible = state.isSaving
        binding.saveButton.isEnabled = !state.isSaving
        binding.cancelButton.isEnabled = !state.isSaving
        backCallback.isEnabled = state.isEditing

        if (editActionVisible != !state.isEditing) {
            editActionVisible = !state.isEditing
            requireActivity().invalidateMenu()
        }
    }

    private fun handleEvent(event: PostDetailEvent) = when (event) {
        PostDetailEvent.Saved -> showMessage(getString(R.string.post_updated))
        PostDetailEvent.Unchanged -> showMessage(getString(R.string.post_unchanged))
        is PostDetailEvent.Failed -> showError(event.error)
    }

    private fun showError(error: AppError) {
        val message = error.toMessage(requireContext())
        when {
            error is AppError.Validation && error.field == AppError.Validation.Field.TITLE ->
                binding.titleLayout.error = message

            error is AppError.Validation ->
                binding.bodyLayout.error = message

            else -> showMessage(message)
        }
    }

    private fun showMessage(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    companion object {
        const val ARG_POST_ID = "postId"
        const val ARG_POSITION = "position"

        private const val STATE_EDITOR_FILLED = "editorFilled"

        fun argsOf(postId: Int, position: Int) = Bundle().apply {
            putInt(ARG_POST_ID, postId)
            putInt(ARG_POSITION, position)
        }
    }
}
