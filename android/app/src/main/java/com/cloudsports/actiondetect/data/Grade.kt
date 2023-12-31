package com.cloudsports.actiondetect.data


class Grade(

    val year: String,
    val items: List<GradeItem>

) {
    data class updateRequest(val id: Int?,val item : String,val number: String,val train_time : String)
    data class recordRequest(val UserId : String)

    var level: String? = null
    var score: Double? = null

    init {
        //通过items的平均分计算score
        var sum = 0
        for (item in items) {
            sum += item.testScore ?: 0
        }
        score = sum / items.size.toDouble()

        when (score!!) {
            in Double.NEGATIVE_INFINITY..60.0 -> {
                level = "flunk"
            }
            in 60.0..70.0 -> {
                level = "pass"
            }
            in 70.0..80.0 -> {
                level = "pass"
            }
            in 80.0..90.0 -> {
                level = "good"
            }
            in 90.0..100.0 -> {
                level = "excellent"
            }
        }

    }
}
