package utils

import kotlin.math.abs

data class Coordinate2D(val x: Int, val y: Int) {
    fun getLargestRectangle(other: Coordinate2D): Long {
        val dx = abs((other.x - this.x).toLong()) + 1L
        val dy = abs((other.y - this.y).toLong()) + 1L
        return dx * dy
    }
}

data class Coordinate3D(val x: Int, val y: Int, val z: Int) {
    fun getDistanceSquare(other: Coordinate3D): Long {
        val dx = (other.x - this.x).toLong()
        val dy = (other.y - this.y).toLong()
        val dz = (other.z - this.z).toLong()
        return (dx * dx) + (dy * dy) + (dz * dz)
    }
}