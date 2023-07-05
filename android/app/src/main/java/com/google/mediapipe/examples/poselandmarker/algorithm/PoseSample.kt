package com.google.mediapipe.examples.poselandmarker.algorithm

import org.nd4j.linalg.api.ndarray.INDArray

class PoseSample(name: String, landmarks:List<DoubleArray>, class_name: String, embedding: INDArray) {
    private val name = name
    val landmarks = landmarks
    val class_name = class_name
    val embedding = embedding
}