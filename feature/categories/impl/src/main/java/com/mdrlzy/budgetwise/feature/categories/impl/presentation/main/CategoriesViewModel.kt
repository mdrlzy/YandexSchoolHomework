package com.mdrlzy.budgetwise.feature.categories.impl.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mdrlzy.budgetwise.feature.categories.api.CategoryRepo
import com.mdrlzy.budgetwise.feature.categories.impl.domain.usecase.FilterCategoryUseCase
import kotlinx.coroutines.Job
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

class CategoriesViewModel(
    private val categoryRepo: CategoryRepo,
    private val filterCategoryUseCase: FilterCategoryUseCase,
) : ViewModel(), ContainerHost<CategoriesScreenState, CategoriesScreenEffect> {
    override val container: Container<CategoriesScreenState, CategoriesScreenEffect> =
        container(CategoriesScreenState.Loading)

    private var initJob: Job? = null

    fun onActive() = init()

    fun onInactive() {
        initJob?.cancel()
    }

    fun onRetry() = init()

    private fun init() {
        initJob =
            intent {
                if (state is CategoriesScreenState.Success) return@intent

                categoryRepo.getAll().fold(
                    ifLeft = {
                        reduce {
                            CategoriesScreenState.Error(it)
                        }
                    },
                    ifRight = { categories ->
                        reduce {
                            CategoriesScreenState.Success(
                                all = categories,
                                filtered = categories
                            )
                        }
                    },
                )
            }
    }

    fun onFilterChange(filter: String) =
        blockingIntent {
            if (state is CategoriesScreenState.Success) {
                val success = (state as CategoriesScreenState.Success)

                val newState = success.copy(
                    filter = filter,
                    filtered = filterCategoryUseCase(success.all, filter)
                )
                reduce {
                    newState
                }
            }
        }
}

class CategoriesViewModelFactory @Inject constructor(
    private val categoryRepo: CategoryRepo,
    private val filterCategoryUseCase: FilterCategoryUseCase,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CategoriesViewModel(categoryRepo, filterCategoryUseCase) as T
    }
}
