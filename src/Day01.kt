import kotlin.math.absoluteValue

fun main() {
    fun part1(input: List<String>): Int {
        val directions = processInput(input)
        var result = 0
        var actualPosition = 50
        directions.forEach {
            actualPosition = (actualPosition + it).mod(100)
            if (actualPosition == 0) {
                result++
            }
        }
        return result
    }

    fun part2(input: List<String>): Int {
        val directions = processInput(input)
        var result = 0
        var actualPosition = 50
        var nextPosition: Int
        directions.forEach {steps ->
            val remainder = steps.rem(100)
            val dividedSteps = steps.div(100)
            result += dividedSteps.absoluteValue
            nextPosition = actualPosition + remainder

            if(nextPosition<= 0 && actualPosition != 0) result++
            if(nextPosition>=100) result++

            actualPosition = nextPosition.mod(100)
        }
        return result
    }

    val testInput = readInput("Day01_test")
    check(part1(testInput) == 3)
    check(part2(testInput) == 6)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}

private fun processInput(input: List<String>): List<Int> {
    return input.map {
        it.replace("R", "").replace("L", "-").toInt()
    }
}
