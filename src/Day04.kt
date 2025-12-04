import Day04.processInput

fun main() {
    fun part1(input: List<String>): Int {
        return Day04.part1(processInput(input))
    }

    fun part2(input: List<String>): Int {
        return Day04.part2(processInput(input))
    }

    // Or read a large test input from the `src/Day04_test.txt` file:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 43)

    // Read the input from the `src/Day04.txt` file.
    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}

object Day04 {
    fun part1(input: List<CharArray>): Int {
        var result = 0
        input.forEachIndexed { line, s ->
            s.forEachIndexed { column, char ->
                if(char == '@') {
                    if(adjacentToiletPapersLessThen4(input, line, column)) result++
                }
            }
        }
        return result
    }

    fun part2(input: List<CharArray>): Int {
        var result = 0
        var resultOneRun = part2OneRun(input)
        while(resultOneRun.first != 0) {
            result += resultOneRun.first
            resultOneRun = part2OneRun(resultOneRun.second)
        }
        return result
    }

    private fun adjacentToiletPapersLessThen4(input: List<CharArray>, line: Int, column: Int): Boolean {
        var adjacentRolls = 0
        val toiletPaper = '@'

            if (line > 0) {
                if (column > 0) {
                    if (input[line - 1][column - 1] == toiletPaper) adjacentRolls++
                }
                if (input[line - 1][column] == toiletPaper) adjacentRolls++
                if (column < input[line].size - 1) {
                    if (input[line - 1][column + 1] == toiletPaper) adjacentRolls++
                }
            }
            if (line < input.size - 1) {
                if (column > 0) {
                    if (input[line + 1][column - 1] == toiletPaper) adjacentRolls++
                }
                if (input[line + 1][column] == toiletPaper) adjacentRolls++
                if (column < input[line].size - 1) {
                    if (input[line + 1][column + 1] == toiletPaper) adjacentRolls++
                }
            }
            if (column > 0) {
                if (input[line][column - 1] == toiletPaper) adjacentRolls++
            }
            if (column < input[line].size - 1) {
                if (input[line][column + 1] == toiletPaper) adjacentRolls++
            }
            return adjacentRolls < 4
    }

    fun processInput(input: List<String>): List<CharArray> = input.map { it.toCharArray() }



    private fun part2OneRun(input: List<CharArray>): Pair<Int, List<CharArray>> {
        var result = 0
        val nextInput = input
        input.forEachIndexed { line, s ->
            s.forEachIndexed { column, char ->
                val toiletPaper = '@'
                if(char == toiletPaper) {
                    if(adjacentToiletPapersLessThen4(input, line, column)) {
                        result++
                        nextInput[line][column] = '.'
                    }
                }
            }
        }
        return Pair(result, nextInput)
    }
}