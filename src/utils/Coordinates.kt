package utils

data class Coordinate3D(val x: Int, val y: Int, val z: Int) {
    fun getDistanceSquare(other: Coordinate3D): Long {
        val dx = (other.x - this.x).toLong()
        val dy = (other.y - this.y).toLong()
        val dz = (other.z - this.z).toLong()
        return (dx * dx) + (dy * dy) + (dz * dz)
    }
}