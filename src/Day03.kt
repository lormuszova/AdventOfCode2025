import kotlin.collections.toIntArray

fun main() {
    fun part1(input: List<String>): Int {
        return Day03.partOne(Day03.processInput(input))
    }

    fun part2(input: List<String>): Long {
        return Day03.partTwo(Day03.processInput(input))
    }

    // Or read a large test input from the `src/Day03_test.txt` file:
    val testInput = readInput("Day03_test")
    check(part2(testInput) == 3121910778619)

    // Read the input from the `src/Day03.txt` file.
    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}

object Day03 {
    fun processInput(input: List<String>): List<IntArray> {
        return input.map {
            it.map { char->
                char.digitToInt()
            }.toIntArray()
        }
    }

    fun partOne(input: List<IntArray>): Int {
        var result = 0
        input.forEach { battery ->
            var max = 0
            for(i in 0..battery.size - 2) {
                for (j in i + 1..battery.size - 1) {
                    if(max < battery[i]*10+battery[j]) {
                        max = battery[i]*10+battery[j]
                    }
                }
            }
            result += max
        }
        return result
    }

    fun partTwo(input: List<IntArray>): Long {
        var result = 0L
        input.forEach { battery->
            var max = ""
            var minIndex = 0
            for(i in 12 downTo 1) {
                val (number, index) = findBiggestNumberIndexed(battery, minIndex, battery.size - i)
                max += number.toString()
                minIndex = index + 1
            }
            result += max.toLong()
        }
        return result
    }

    private fun findBiggestNumberIndexed(battery: IntArray, minIndex: Int, maxIndex: Int): Pair<Int, Int> {
        var max = Pair(0, 0)
        for(i in minIndex..maxIndex) {
            if(max.first < battery[i]) {
                max = Pair(battery[i], i)
            }
        }
        return max
    }
}