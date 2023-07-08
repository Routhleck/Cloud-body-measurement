package com.google.mediapipe.examples.poselandmarker.algorithm.Util

class MyDoubleArray(var array: DoubleArray) {

    fun add(other: MyDoubleArray): DoubleArray {
        return MyDoubleArray(array.zip(other.array).map { it.first + it.second }.toDoubleArray()).array
    }

    fun div(divider: Double): DoubleArray {
        return MyDoubleArray(array.map { it / divider }.toDoubleArray()).array
    }

    fun toDoubleVector(): DoubleArray {
        return array
    }

    fun sub(other: MyDoubleArray): DoubleArray {
        return MyDoubleArray(array.zip(other.array).map { it.first - it.second }.toDoubleArray()).array
    }

    fun getDistance(other: MyDoubleArray): DoubleArray {
        return MyDoubleArray(array.zip(other.array).map { it.first - it.second }.toDoubleArray()).array
    }

    fun toDoubleArray(): DoubleArray {
        return array
    }

    fun multiply(multiplier: Double): DoubleArray {
        for (i in array.indices) {
            array[i] *= multiplier
        }
        return array
    }
}