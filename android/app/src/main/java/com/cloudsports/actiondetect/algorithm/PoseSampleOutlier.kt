package com.cloudsports.actiondetect.algorithm

import com.cloudsports.actiondetect.algorithm.PoseSample

class PoseSampleOutlier(sample: PoseSample, detected_class: Map<String, Int>, all_classes: Map<String, Int>) {
    private val _sample = sample
    private val _detectedClass = detected_class
    private val _allClasses = all_classes
}