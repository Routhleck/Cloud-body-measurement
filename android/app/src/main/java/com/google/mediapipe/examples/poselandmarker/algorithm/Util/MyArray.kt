package com.google.mediapipe.examples.poselandmarker.algorithm.Util

import kotlin.math.pow
import kotlin.math.sqrt

class MyArray(var array: DoubleArray) {

    fun add(other: MyArray): DoubleArray {
        return MyArray(array.zip(other.array).map { it.first + it.second }.toDoubleArray()).array
    }

    fun div(divider: Double): DoubleArray {
        return MyArray(array.map { it / divider }.toDoubleArray()).array
    }

    fun toDoubleVector(): DoubleArray {
        return array
    }

    fun sub(other: MyArray): DoubleArray {
        return MyArray(array.zip(other.array).map { it.first - it.second }.toDoubleArray()).array
    }

    fun getDistance(other: MyArray): Double {
        return sqrt(array.zip(other.array) { a, b -> (a - b).pow(2) }.sum())
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