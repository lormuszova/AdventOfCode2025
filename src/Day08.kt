import utils.Coordinate3D

fun main() {
    fun part1(input: List<String>, connections: Int): Int {
        return Day08.part1(input, connections)
    }

    fun part2(input: List<String>): Long {
        return Day08.part2(input)
    }

    val testInput = readInput("Day08_test")
    check(part1(testInput, 10) == 40)
    check(part2(testInput) == 25272L)

    // Read the input from the `src/Day08.txt` file.
    val input = readInput("Day08")
    part1(input, 1000).println()
    part2(input).println()
}

object Day08 {
    fun part1(input: List<String>, connections: Int): Int {
        val coordinates = input.map { line ->
            val (x, y, z) = line.split(",").map { it.toInt() }
            Coordinate3D(x, y, z)
        }.toSet()
        val distances = coordinates.flatMapIndexed { index, c1 ->
            coordinates.drop(index + 1).map { c2 ->
                Triple(c1, c2, c1.getDistanceSquare(c2))
            }
        }.sortedBy { it.third }
        val circuits = coordinates.map { mutableSetOf(it) }.toMutableList()
        var connectionsMade = 0

        for ((c1, c2, _) in distances) {

            val s1 = circuits.find { it.contains(c1) }
            val s2 = circuits.find { it.contains(c2) }

            if (s1 !== s2) {
                s1!!.addAll(s2!!)
                circuits.remove(s2)
            }

            connectionsMade++
            if (connectionsMade == connections) break
        }
        val largestCircuits = circuits.map { it.size }.sortedDescending().take(3)
        return largestCircuits.reduce { acc, i -> acc * i }
    }

    fun part2(input: List<String>): Long {
        val coordinates = input.map { line ->
            val (x, y, z) = line.split(",").map { it.toInt() }
            Coordinate3D(x, y, z)
        }.toSet()

        val circuits = coordinates.map { mutableSetOf(it) }.toMutableList()
        val distances = coordinates.flatMapIndexed { index, c1 ->
            coordinates.drop(index + 1).map { c2 ->
                Triple(c1, c2, c1.getDistanceSquare(c2))
            }
        }.sortedBy { it.third }

        for ((c1, c2, _) in distances) {

            val s1 = circuits.find { it.contains(c1) }
            val s2 = circuits.find { it.contains(c2) }

            if (s1 !== s2) {
                s1!!.addAll(s2!!)
                circuits.remove(s2)
            }

            if (circuits.size == 1) {
                return c1.x.toLong() * c2.x.toLong()
            }
        }
        return 0L
    }
}