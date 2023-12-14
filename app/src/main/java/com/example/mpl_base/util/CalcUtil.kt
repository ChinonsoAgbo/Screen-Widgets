package com.example.mpl_base.util

class CalcUtil
{
    companion object{
//        var randomNumber: Int = rng()

        fun rng(): Int
        {
            return (1..100).random()
        }

        fun checkIfPrime(number: Int): Boolean
        {
            if (number <= 1)
            {
                return false
            }

            val range = 2 until (number / 2 + 1)
            for (i in range) {
                if (number % i == 0) {
                    return false
                }
            }

            return true
        }
    }
}