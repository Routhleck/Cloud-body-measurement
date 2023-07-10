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


    fun getItemByIndex(array2D: Array<DoubleArray>, index: Int): DoubleArray {
        return array2D[index]
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
        return array.map { it.map { abs(it) }.toDoubleArray() }.toTypedArray()
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

    fun absSubMulMax(sample: DoubleArray, pose_embedding: DoubleArray, axes_weights_array: DoubleArray): Double {
        val absSubMul = sample.zip(pose_embedding) { a, b -> abs(a - b) }.toDoubleArray()
        return absSubMul.zip(axes_weights_array) { a, b -> a * b }.maxOrNull()!!
    }

    fun absSubMulMean(sample: DoubleArray, pose_embedding: DoubleArray, axes_weights_array: DoubleArray): Double {
        val absSubMul = sample.zip(pose_embedding) { a, b -> abs(a - b) }.toDoubleArray()
        return absSubMul.zip(axes_weights_array) { a, b -> a * b }.average()
    }

    fun Double.sqrt() = kotlin.math.sqrt(this)

//    fun topNSamples(samples: Array<DoubleArray>, pose_embedding: DoubleArray, flipped_pose_embedding: DoubleArray, axes_weights_array: DoubleArray, topN: Int): List<Int> {
//        return samples.mapIndexed { index, sample ->
//            max(
//                absSubMulMax(sample, pose_embedding, axes_weights_array),
//                absSubMulMax(sample, flipped_pose_embedding, axes_weights_array)
//            ) to index
//        }.sortedBy { it.first }.take(topN).map { it.second }
//    }
//
//    fun topNSamplesMean(samples: List<DoubleArray>, pose_embedding: DoubleArray, flipped_pose_embedding: DoubleArray, axes_weights_array: DoubleArray, topN: Int): List<Int> {
//        return samples.mapIndexed { index, sample ->
//            min(
//                absSubMulMean(sample, pose_embedding, axes_weights_array),
//                absSubMulMean(sample, flipped_pose_embedding, axes_weights_array)
//            ) to index
//        }.sortedBy { it.first }.take(topN).map { it.second }
//    }

    fun getClassesCount(samples: List<String>, indices: List<Int>): Map<String, Int> {
        return indices.map { samples[it] }.groupingBy { it }.eachCount()
    }
}
