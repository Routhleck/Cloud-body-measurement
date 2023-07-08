package com.google.mediapipe.examples.poselandmarker.algorithm

class EMADictSmoothing(private val windowSize: Int = 10, private val alpha: Double = 0.2) {
    private val dataInWindow: MutableList<Map<String, Double>> = mutableListOf()

    operator fun invoke(data: Map<String, Int>): Map<String, Double> {
        // 平滑给定的姿势分类。
        // 平滑是通过计算在给定时间窗口中观察到的每个姿势类别的指数移动平均值来完成的。错过的姿势类将替换为 0。
        dataInWindow.add(0, data.mapValues { it.value.toDouble() })
        if (dataInWindow.size > windowSize) {
            dataInWindow.subList(windowSize, dataInWindow.size).clear()
        }


        val keys = dataInWindow.flatMap { it.keys }.toSet()

        val smoothedData = mutableMapOf<String, Double>()
        for (key in keys) {
            var factor = 1.0
            var topSum = 0.0
            var bottomSum = 0.0
            for (dataEntry in dataInWindow) {
                val value = dataEntry[key] ?: 0.0
                topSum += factor * value
                bottomSum += factor
                factor *= (1.0 - alpha)
            }
            smoothedData[key] = topSum / bottomSum
        }

        return smoothedData
    }
}

