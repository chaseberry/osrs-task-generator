package com.chase.points

data class AssignedPoints(
    val itemSourceId: Int,
    val points: List<Point>,
    val killsForOnePoint: Int?,
) {

    data class Point(
        val itemId: Int,
        val points: Int,
    )

}