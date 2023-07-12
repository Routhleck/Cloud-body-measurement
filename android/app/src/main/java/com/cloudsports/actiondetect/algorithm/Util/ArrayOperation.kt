package com.cloudsports.actiondetect.algorithm.Util

import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

object ArrayOperation {

    fun averageDoubleArray(array1: DoubleArray, array2: DoubleArray): DoubleArray {
        return DoubleArray(array1.size) { i -> (array1[i] + array2[i]) / 2 }
    }

    fun getLandmarksByDimensionRange(landmarks: Array<DoubleArray>, indices: IntRange): Array<DoubleArray> {
        return landmarks.map { it.sliceArray(indices) }.toTypedArray()
    }


    fun getNorm(array: DoubleArray): Double {
        return sqrt(array.sumOf { it.pow(2) })
    }

    fun getMaxOfArrayDoubleArray(array: Array<DoubleArray>): Double {
        return array.maxOf { it.maxOrNull()!! }
    }

    fun getMeanOfArrayDoubleArray(array: Array<DoubleArray>): Double {
        return array.sumOf { it.sum() } / array.size
    }

    fun absArrayDoubleArray(array: Array<DoubleArray>): Array<DoubleArray> {
        return array.map { it -> it.map { abs(it) }.toDoubleArray() }.toTypedArray()
    }

    fun subArrayDoubleArrayByDoubleArray(array: Array<DoubleArray>, arrayI: DoubleArray): Array<DoubleArray> {
        return array.map { MyDoubleArray(MyDoubleArray(it).sub(MyDoubleArray(arrayI))).array }.toTypedArray()
    }

    fun subArrayDoubleArrayByArrayDoubleArray(array: Array<DoubleArray>, arrayI: Array<DoubleArray>): Array<DoubleArray> {
        return array.mapIndexed { index, doubles -> MyDoubleArray(
            MyDoubleArray(doubles).sub(
                MyDoubleArray(arrayI[index])
            )).array }.toTypedArray()
    }


    fun divArrayDoubleArrayByDouble(array: Array<DoubleArray>, divider: Double): Array<DoubleArray> {
        return array.map { MyDoubleArray(it).div(divider) }.toTypedArray()
    }

    fun multiplyArrayDoubleArrayByDouble(array: Array<DoubleArray>, multiplier: Double): Array<DoubleArray> {
        return array.map { MyDoubleArray(it).multiply(multiplier) }.toTypedArray()
    }

    fun multiplyArrayDoubleArrayByDimensionWeight(array: Array<DoubleArray>, dimensionWeight: DoubleArray): Array<DoubleArray> {
        val result = Array(array.size) { DoubleArray(array[0].size) }

        for (i in array.indices) {
            for (j in array[i].indices) {
                result[i][j] = array[i][j] * dimensionWeight[j]
            }
        }

        return result
    }


    fun vstack(arrays: MutableList<DoubleArray>) : Array<DoubleArray> {
        return arrays.toTypedArray()
    }

}
