import utils.Coordinate2D
import kotlin.collections.mutableListOf
import kotlin.math.max
import kotlin.math.min

fun main() {
    fun part1(input: List<String>): Long {
        val coordinatesOrdered = coordinatesSortedBySquareArea(saveCoordinates(input))
        return coordinatesOrdered.first().third
    }

    fun part2(input: List<String>): Long {
        val coordinates = saveCoordinates(input)
        val xRanges = createAllXRanges(coordinates) // Calculate ONCE

        val coordinatesOrdered = coordinatesSortedBySquareArea(saveCoordinates(input))
        coordinatesOrdered.forEach {
            if (isSquareInsideTheLoop(it.first, it.second, xRanges)) return it.third
        }
        return 0L
    }

    val testInput = readInput("Day09_test")
    check(part1(testInput) == 50L)
    check(part2(testInput) == 24L)

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}

private fun saveCoordinates(input: List<String>): List<Coordinate2D> {
    return input.map { line ->
        val (x, y) = line.split(",").map { it.toInt() }
        Coordinate2D(x, y)
    }
}

private fun coordinatesSortedBySquareArea(coordinates: List<Coordinate2D>): List<Triple<Coordinate2D, Coordinate2D, Long>> {
    return coordinates.flatMapIndexed { index, c1 ->
        coordinates.drop(index + 1).map { c2 ->
            Triple(c1, c2, c1.getLargestRectangle(c2))
        }
    }.sortedByDescending { it.third }
}

private fun createAllXRanges(coordinates: List<Coordinate2D>): Map<Int, IntRange> {
    val allTiles = mutableMapOf<Int, MutableList<Int>>()

    for (redTile in coordinates) {
        val y = redTile.y
        val x = redTile.x

        allTiles.getOrPut(y) { mutableListOf() }.add(x)
    }

    val numberOfRedTiles = coordinates.size
    for (i in 0 until numberOfRedTiles) {
        val currentTile = coordinates[i]
        val nextTile = coordinates[(i + 1) % numberOfRedTiles]

        if (currentTile.x == nextTile.x) {
            val x = currentTile.x

            val startRow = min(currentTile.y, nextTile.y)
            val endRow = max(currentTile.y, nextTile.y)

            for (row in (startRow + 1) until endRow) {
                allTiles.getOrPut(row) { mutableListOf() }.add(x)
            }
        }
    }

    val rangesPerRow = mutableMapOf<Int, IntRange>()

    for ((row, xValues) in allTiles) {
        rangesPerRow[row] = xValues.min()..xValues.max()
    }

    return rangesPerRow
}

private fun isSquareInsideTheLoop(
    c1: Coordinate2D,
    c2: Coordinate2D,
    xRanges: Map<Int, IntRange>
): Boolean {
    val xMin = min(c1.x, c2.x)
    val xMax = max(c1.x, c2.x)
    val yMin = min(c1.y, c2.y)
    val yMax = max(c1.y, c2.y)

    for (y in yMin..yMax) {
        val range = xRanges[y] ?: return false

        if (xMin < range.first || xMax > range.last) {
            return false
        }
    }
    return true
}


