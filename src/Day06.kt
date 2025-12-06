import Day06.part1
import Day06.part2
import kotlin.io.path.Path
import kotlin.io.path.readLines

fun main() {
    check(part1("Day06_test") == 4277556L)
    check(part2("Day06_test") == 3263827L)

    part1("Day06").println()
    part2("Day06").println()
}

object Day06 {
    fun part1(name:String): Long {
        val data = Path("src/resources/$name.txt").readLines().map { line ->
            line.replace("\\s{2,}".toRegex(), " ")
                .trim()
                .split(" ")
        }

        data[0].indices.map { i->
            data.dropLast(1).map {it[i].toLong() } to data.last()[i]
        }.let { return countResult(it) }
    }

    fun part2(name: String): Long {
        val input = Path("src/resources/$name.txt").readLines()
        return countResult(extractVerticalCalculations(input))
    }

    private fun countResult(data: List<Pair<List<Long>, String>>): Long {
        return data.sumOf { (numbers, operator) ->
            numbers.reduce { acc, i ->
                when (operator) {
                    "+" -> acc.plus(i)
                    "*" -> acc.times(i)
                    else -> acc
                }
            }
        }
    }

    private fun extractVerticalCalculations(input: List<String>): List<Pair<List<Long>, String>> {
        var index = input[0].lastIndex
        val lastRowIndex = input.lastIndex
        var currentProblem = mutableListOf<Long>()

        return buildList {
            while (index >= 0) {
                currentProblem.add(extractVerticalNumber(input, index, lastRowIndex))

                val operator = input[lastRowIndex][index]
                if (operator != ' ') {
                    add(currentProblem to operator.toString())
                    currentProblem = mutableListOf()
                    index -= 2
                } else {
                    index--
                }
            }
        }
    }

    private fun extractVerticalNumber(input: List<String>, index: Int, lastRowIndex: Int): Long {
        return input.take(lastRowIndex)
            .joinToString("") { it[index].toString() }
            .trim()
            .toLong()
    }
}