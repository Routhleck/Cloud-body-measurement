package com.cloudsports.actiondetect.algorithm

class PoseSample(name: String, val landmarks: Array<DoubleArray>, val className: String,
                 val embedding: Array<DoubleArray>
) {
    private val _name = name
}