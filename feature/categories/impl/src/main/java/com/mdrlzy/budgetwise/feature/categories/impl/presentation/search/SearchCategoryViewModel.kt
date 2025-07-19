package com.mdrlzy.budgetwise.feature.categories.impl.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mdrlzy.budgetwise.feature.categories.api.CategoryRepo
import com.mdrlzy.budgetwise.feature.categories.impl.domain.usecase.FilterCategoryUseCase
import com.mdrlzy.budgetwise.feature.categories.impl.presentation.main.CategoriesScreenEffect
import com.mdrlzy.budgetwise.feature.categories.impl.presentation.main.CategoriesScreenState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

class SearchCategoryViewModel(
    private val isIncomeMode: Boolean,
    private val categoryRepo: CategoryRepo,
    private val filterCategoryUseCase: FilterCategoryUseCase,
) : ViewModel(), ContainerHost<CategoriesScreenState, CategoriesScreenEffect> {
    override val container: Container<CategoriesScreenState, CategoriesScreenEffect> =
        container(CategoriesScreenState.Loading)

    init {
        init()
    }

    fun onRetry() = init()

    private fun init() = intent {
        categoryRepo.getAll().fold(
            ifLeft = {
                reduce {
                    CategoriesScreenState.Error(it)
                }
            },
            ifRight = { categories ->
                val filteredByIncome = categories.filter { it.isIncome == isIncomeMode }
                reduce {
                    CategoriesScreenState.Success(
                        all = filteredByIncome,
                        filtered = filteredByIncome
                    )
                }
            },
        )
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

class SearchCategoryViewModelFactory @AssistedInject constructor(
    @Assisted private val isIncomeMode: Boolean,
    private val categoryRepo: CategoryRepo,
    private val filterCategoryUseCase: FilterCategoryUseCase,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchCategoryViewModel(isIncomeMode, categoryRepo, filterCategoryUseCase) as T
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted isIncomeMode: Boolean,
        ): SearchCategoryViewModelFactory
    }
}