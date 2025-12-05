import kotlin.io.path.Path
import kotlin.io.path.readText

fun main() {
    // Or read a large test input from the `src/Day05_test.txt` file:
    check(Day05.part1("Day05_test") == 3)
    check(Day05.part2("Day05_test") == 14L)

    // Read the input from the `src/Day05.txt` file.
    Day05.part1("Day05").println()
    Day05.part2("Day05").println()
}

object Day05 {
    fun processData(name: String): Pair<List<LongRange>, List<Long>> {
        val data = Path("src/resources/$name.txt").readText().split("\n\n")

        val ranges = mutableListOf<LongRange>()
        data[0].lines().forEach { line ->
            val (start, end) = line.split("-").map { it.toLong() }
            val newRange = start..end

            val overlappingRanges = ranges.filter { it.overlaps(newRange) }

            if (overlappingRanges.isEmpty()) {
                ranges.add(newRange)
            } else {
                val mergedRange = overlappingRanges.fold(newRange) { acc, range ->
                    acc.merge(range)
                }
                ranges.removeAll(overlappingRanges)
                ranges.add(mergedRange)
            }
        }

        val ingredient = data[1].lines().map { it.toLong() }
        return Pair(ranges, ingredient)
    }

    fun part1(name: String): Int {
        var result = 0
        val (ranges, ingredients) = processData(name)
        ingredients.forEach { ingredient ->
            if (ranges.any { range -> ingredient in range }) {
                result++
            }
        }
        return result
    }

    fun part2(name: String): Long {
        var result = 0L
        val (ranges, _) = processData(name)
        ranges.forEach { range ->
            result += range.last - range.first + 1L
        }
        return result
    }
}