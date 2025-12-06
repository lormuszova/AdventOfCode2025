import Day06.part1
import Day06.part2
import kotlin.io.path.Path
import kotlin.io.path.readLines

fun main() {
    check(part1(readInput("Day06_test")) == 4277556L)
    check(part2("Day06_test") == 3263827L)

    part1(readInput("Day06")).println()
    part2("Day06").println()
}

object Day06 {
    fun part1(input: List<String>): Long {
        val data = input.map { line ->
            line.replace("\\s{2,}".toRegex(), " ")
                .trim()
                .split(" ")
        }

        data[0].indices.map { i->
            data.dropLast(1).map {it[i].toLong() } to data.last()[i]
        }.let { return countResult(it) }
    }

    fun countResult(data: List<Pair<List<Long>, String>>): Long {
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

    fun part2(name: String): Long {
        val input = Path("src/resources/$name.txt").readLines()
        val data = mutableListOf<Pair<List<Long>, String>>()
        var i = input[0].length - 1
        var oneMathProblem = mutableListOf<Long>()
        while (i >= 0) {
            var numbers = ""
            for (j in 0 until input.size - 1) {
                numbers += input[j][i]
            }
            oneMathProblem.add(numbers.trim().toLong())
            if (input[input.size - 1][i] != ' ') {
                data.add(oneMathProblem to input[input.size - 1][i].toString())
                oneMathProblem = mutableListOf()
                i -= 2
            } else {
                i -= 1
            }
        }
        return countResult(data)
    }
}