package com.cloudsports.actiondetect.data


class GradeItem(
    private val testItem: String,
    val testName: String,
    val testResult: Double)
{
    var testScore: Int? = null
    lateinit var scoreLevel: String

    init {
        when (testItem) {
            "height" -> {
                when (testResult) {
                    in Double.NEGATIVE_INFINITY..100.0 -> {
                        testScore = 0
                        scoreLevel = "flunk"
                    }
                    in 100.0..120.0 -> {
                        testScore = 60
                        scoreLevel = "pass"
                    }
                    in 120.0..140.0 -> {
                        testScore = 70
                        scoreLevel = "pass"
                    }
                    in 140.0..160.0 -> {
                        testScore = 80
                        scoreLevel = "good"
                    }
                    in 160.0..180.0 -> {
                        testScore = 90
                        scoreLevel = "excellent"
                    }
                    else -> {
                        testScore = 100
                        scoreLevel = "excellent"
                    }
                }
            }

            "weight" -> {
                when (testResult) {
                    in Double.NEGATIVE_INFINITY..40.0 -> {
                        testScore = 0
                        scoreLevel = "flunk"
                    }
                    in 40.0..45.0 -> {
                        testScore = 60
                        scoreLevel = "pass"
                    }
                    in 45.0..50.0 -> {
                        testScore = 70
                        scoreLevel = "pass"
                    }
                    in 50.0..55.0 -> {
                        testScore = 80
                        scoreLevel = "good"
                    }
                    in 55.0..60.0 -> {
                        testScore = 90
                        scoreLevel = "excellent"
                    }
                    else -> {
                        testScore = 100
                        scoreLevel = "excellent"
                    }
                }
            }

            "vital_capacity" -> {
                when (testResult) {
                    in 1000.0..1500.0 -> {
                        testScore = 60
                        scoreLevel = "pass"
                    }
                    in 1500.0..2000.0 -> {
                        testScore = 70
                        scoreLevel = "pass"
                    }
                    in 2000.0..2500.0 -> {
                        testScore = 80
                        scoreLevel = "good"
                    }
                    in 2500.0..3000.0 -> {
                        testScore = 90
                        scoreLevel = "excellent"
                    }
                    in 3000.0..Double.POSITIVE_INFINITY -> {
                        testScore = 100
                        scoreLevel = "excellent"
                    }
                    else -> {
                        testScore = 0
                        scoreLevel = "flunk"
                    }
                }
            }

            "standing_long_jump" -> {
                when (testResult) {
                    in Double.NEGATIVE_INFINITY..100.0 -> {
                        testScore = 0
                        scoreLevel = "flunk"
                    }
                    in 100.0..150.0 -> {
                        testScore = 60
                        scoreLevel = "pass"
                    }
                    in 150.0..200.0 -> {
                        testScore = 70
                        scoreLevel = "pass"
                    }
                    in 200.0..220.0 -> {
                        testScore = 80
                        scoreLevel = "good"
                    }
                    in 220.0..240.0 -> {
                        testScore = 90
                        scoreLevel = "excellent"
                    }
                    else -> {
                        testScore = 100
                        scoreLevel = "excellent"
                    }
                }
            }

            "sit_and_reach" -> {
                when (testResult) {
                    in Double.NEGATIVE_INFINITY..0.0 -> {
                        testScore = 0
                        scoreLevel = "flunk"
                    }
                    in 0.0..10.0 -> {
                        testScore = 60
                        scoreLevel = "pass"
                    }
                    in 10.0..20.0 -> {
                        testScore = 70
                        scoreLevel = "pass"
                    }
                    in 20.0..30.0 -> {
                        testScore = 80
                        scoreLevel = "good"
                    }
                    in 30.0..40.0 -> {
                        testScore = 90
                        scoreLevel = "excellent"
                    }
                    else -> {
                        testScore = 100
                        scoreLevel = "excellent"
                    }
                }
            }

            "pull_or_sit_up" -> {
                when (testResult) {
                    in Double.NEGATIVE_INFINITY..10.0 -> {
                        testScore = 0
                        scoreLevel = "flunk"
                    }
                    in 10.0..15.0 -> {
                        testScore = 60
                        scoreLevel = "pass"
                    }
                    in 15.0..20.0 -> {
                        testScore = 70
                        scoreLevel = "pass"
                    }
                    in 20.0..25.0 -> {
                        testScore = 80
                        scoreLevel = "good"
                    }
                    in 25.0..30.0 -> {
                        testScore = 90
                        scoreLevel = "excellent"
                    }
                    else -> {
                        testScore = 100
                        scoreLevel = "excellent"
                    }
                }
            }

            "sprint_50m" -> {
                when (testResult) {
                    in 0.0..6.5 -> {
                        testScore = 100
                        scoreLevel = "excellent"
                    }
                    in 6.5..7.0 -> {
                        testScore = 90
                        scoreLevel = "excellent"
                    }
                    in 7.0..7.5 -> {
                        testScore = 80
                        scoreLevel = "good"
                    }
                    in 7.5..8.0 -> {
                        testScore = 70
                        scoreLevel = "pass"
                    }
                    in 8.0..8.5 -> {
                        testScore = 60
                        scoreLevel = "pass"
                    }
                    else -> {
                        testScore = 0
                        scoreLevel = "flunk"
                    }
                }
            }

            "long_distance_run" -> {
                when (testResult) {
                    in Double.NEGATIVE_INFINITY..180.0 -> {
                        testScore = 100
                        scoreLevel = "excellent"
                    }
                    in 180.0..210.0 -> {
                        testScore = 90
                        scoreLevel = "excellent"
                    }
                    in 210.0..240.0 -> {
                        testScore = 80
                        scoreLevel = "good"
                    }
                    in 240.0..270.0 -> {
                        testScore = 70
                        scoreLevel = "pass"
                    }
                    in 270.0..300.0 -> {
                        testScore = 60
                        scoreLevel = "pass"
                    }
                    else -> {
                        testScore = 0
                        scoreLevel = "flunk"
                    }
                }
            }
        }
    }

}

