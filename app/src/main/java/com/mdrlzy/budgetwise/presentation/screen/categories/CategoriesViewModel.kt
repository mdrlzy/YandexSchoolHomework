package com.mdrlzy.budgetwise.presentation.screen.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mdrlzy.budgetwise.core.domain.model.Category
import com.mdrlzy.budgetwise.domain.repo.CategoryRepo
import kotlinx.coroutines.Job
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

sealed class CategoriesScreenState {

    data object Loading : CategoriesScreenState()

    data class Success(
        val categories: List<Category>,
        val searchQuery: String = "",
    ) : CategoriesScreenState()

    data class Error(val error: Throwable?) : CategoriesScreenState()
}

sealed class CategoriesScreenEffect

class CategoriesViewModel(
    private val categoryRepo: CategoryRepo
) : ViewModel(),
    ContainerHost<CategoriesScreenState, CategoriesScreenEffect> {
    override val container: Container<CategoriesScreenState, CategoriesScreenEffect> =
        container(CategoriesScreenState.Loading)

    private var initJob: Job? = null

    fun onActive() = init()

    fun onInactive() {
        initJob?.cancel()
    }

    fun onRetry() = init()

    private fun init() {
        initJob = intent {
            if (state is CategoriesScreenState.Success) return@intent

            categoryRepo.getAll().fold(
                ifLeft = {
                    reduce {
                        CategoriesScreenState.Error(it)
                    }
                },
                ifRight = {
                    reduce {
                        CategoriesScreenState.Success(it)
                    }
                }
            )
        }
    }

    fun onSearchQueryChange(query: String) = blockingIntent {
        if (state is CategoriesScreenState.Success) {
            val newState = (state as CategoriesScreenState.Success)
                .copy(searchQuery = query)
            reduce {
                newState
            }
        }
    }
}

class CategoriesViewModelFactory @Inject constructor(
    private val categoryRepo: CategoryRepo
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CategoriesViewModel(categoryRepo) as T
    }
}