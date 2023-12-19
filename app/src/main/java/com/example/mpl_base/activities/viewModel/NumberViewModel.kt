package com.example.mpl_base.activities.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mpl_base.model.NumberData
import com.example.mpl_base.util.CalcUtil

/**
 * ViewModel for managing number-related data and operations.
 *
 * This ViewModel is responsible for handling and providing access to the current state
 * of the number data through LiveData. It also contains a method to update the state with
 * a random number.
 *
 * @property _numberState The private MutableLiveData to hold the current state of number data.
 * @property numberState The public LiveData property for observing changes in the number data state.
 */
class NumberViewModel : ViewModel() {

    private val _numberState = MutableLiveData<NumberData>(NumberData())

    val numberState: MutableLiveData<NumberData>
        get() = _numberState

    /**
     * Companion object to provide a singleton instance of NumberViewModel.
     */
    companion object {

        private var instance: NumberViewModel? = null

        /**
         * Get a singleton instance of NumberViewModel.
         *
         * @return The singleton instance of NumberViewModel.
         */
        fun getInstance(): NumberViewModel {
            if (instance == null) {
                instance = NumberViewModel()
            }
            return instance as NumberViewModel
        }
    }

    /**
     * Generates a random number and updates the number data state.
     */
    fun returnRandomNumber() {

        _numberState.value = NumberData(randomData = CalcUtil.rng()) // generate randomNum and update the view-model state value
    }

}
