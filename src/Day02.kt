fun main() {
    fun part1(input: List<String>): Long {
        return Day02.findInvalidIdsFirstPart(Day02.processInput(input))
    }

    fun part2(input: List<String>): Long {
        return Day02.findInvalidIdsSecondPart(Day02.processInput(input))
    }

    // Or read a large test input from the `src/Day02_test.txt` file:
    val testInput = readInput("Day02_test")
    check(part2(testInput) == 4174379265L)

    // Read the input from the `src/Day02.txt` file.
    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}

object Day02 {
    fun processInput(input: List<String>): List<LongRange> {
        val idsRange = mutableListOf<LongRange>()
        input[0].split(",").forEach { it ->
            val ids = it.split("-")
            idsRange.add(ids[0].toLong()..ids[1].toLong())
        }
        return idsRange
    }

    fun findInvalidIdsFirstPart(idsRange: List<LongRange>): Long {
        var sum = 0L
         idsRange.forEach { range ->
            range.forEach { id ->
                val id = id.toString()
                val length = id.length
                if (length.mod(2) == 1) return@forEach
                val half = length / 2
                val firstHalf = id.substring(0, half)
                val secondHalf = id.substring(half)
                if (firstHalf == secondHalf) sum += id.toLong()
            }
        }
        return sum
    }

    fun findInvalidIdsSecondPart(idsRange: List<LongRange>): Long {
        var sum = 0L
        idsRange.forEach {range->
            range.forEach {id ->
                val idString = id.toString()
                val length = idString.length
                for (i in (length) downTo (2)) {
                    if (length.mod(i) != 0) continue
                    val newLength = length.div(i)
                    val arrayOfValues = arrayOfNulls<Long>(i)
                    for (j in 0..i - 1) {
                        arrayOfValues[j] = idString.substring(j * newLength, j * newLength + newLength).toLong()
                    }
                    var repetitionFound = true
                    for (k in 1..arrayOfValues.size - 1) {
                        if (arrayOfValues[0] != arrayOfValues[k]) {
                            repetitionFound = false
                            continue
                        }
                    }
                    if (repetitionFound) {
                        sum += id
                        return@forEach
                    }
                }
            }
        }
        return sum
    }
}


