package com.google.mediapipe.examples.poselandmarker.algorithm.Util

import java.lang.Double.max
import java.lang.Double.min
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

    fun subArrayDoubleArrayByDoubleArray(array: Array<DoubleArray>, arrayI: DoubleArray): Array<DoubleArray> {
        return array.map { MyArray(MyArray(it).sub(MyArray(arrayI))).array }.toTypedArray()
    }

    fun divArrayDoubleArrayByDouble(array: Array<DoubleArray>, divider: Double): Array<DoubleArray> {
        return array.map { MyArray(it).div(divider) }.toTypedArray()
    }

    fun multiplyArrayDoubleArrayByDouble(array: Array<DoubleArray>, multiplier: Double): Array<DoubleArray> {
        return array.map { MyArray(it).multiply(multiplier) }.toTypedArray()
    }


    fun vstack(arrays: MutableList<Double>) : DoubleArray {
        return arrays.toTypedArray().toDoubleArray()
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

    fun topNSamples(samples: List<DoubleArray>, pose_embedding: DoubleArray, flipped_pose_embedding: DoubleArray, axes_weights_array: DoubleArray, topN: Int): List<Int> {
        return samples.mapIndexed { index, sample ->
            max(
                absSubMulMax(sample, pose_embedding, axes_weights_array),
                absSubMulMax(sample, flipped_pose_embedding, axes_weights_array)
            ) to index
        }.sortedBy { it.first }.take(topN).map { it.second }
    }

    fun topNSamplesMean(samples: List<DoubleArray>, pose_embedding: DoubleArray, flipped_pose_embedding: DoubleArray, axes_weights_array: DoubleArray, topN: Int): List<Int> {
        return samples.mapIndexed { index, sample ->
            min(
                absSubMulMean(sample, pose_embedding, axes_weights_array),
                absSubMulMean(sample, flipped_pose_embedding, axes_weights_array)
            ) to index
        }.sortedBy { it.first }.take(topN).map { it.second }
    }

    fun getClassesCount(samples: List<String>, indices: List<Int>): Map<String, Int> {
        return indices.map { samples[it] }.groupingBy { it }.eachCount()
    }
}
