package com.example.mpl_base.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mpl_base.R
import com.example.mpl_base.model.NumberData
import com.example.mpl_base.util.CalcUtil

class NumberViewModel : ViewModel() {

    private val _numberState = MutableLiveData<NumberData>(NumberData())

    val numberState: MutableLiveData<NumberData>
        get() = _numberState
    companion object {
        private var instance: NumberViewModel? = null

        fun getInstance(): NumberViewModel {
            if (instance == null) {
                instance = NumberViewModel()
            }
            return instance as NumberViewModel
        }
    }

    fun returnRandomNumber() {
        _numberState.value = NumberData(randomData = CalcUtil.rng())
    }

    fun checkTrueButton(number:Int):Boolean{
        val isPrime = CalcUtil.checkIfPrime(number)

        return false
    }
    fun checkFalseButton():Boolean{

        return false
    }
}
