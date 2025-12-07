fun main() {
    fun part1(input: List<String>): Int {
        return Day07.part1(input)
    }

    fun part2(input: List<String>): Long {
        return Day07.part2(input)
    }

    val testInput = readInput("Day07_test")
    check(part1(testInput) == 21)
    check(part2(testInput) == 40L)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}

object Day07 {
    fun part1(input: List<String>): Int {
        var data = input.map {
            it.toCharArray()
        }

        var result = 0
        for(i in 0 until data.lastIndex) {
            val (splits, newData) = createNextBeams(i, data)
            data = newData
            result += splits
        }
        return result
    }

    fun part2(input: List<String>): Long {
        var currentBeams = mutableMapOf<Int, Long>()

        input[0].forEachIndexed { index, char ->
            if (char == 'S') {
                currentBeams[index] = 1L
            }
        }

        for (rowIndex in 0 until input.lastIndex) {
            val nextRow = input[rowIndex + 1]
            val nextBeams = mutableMapOf<Int, Long>()

            for ((colIndex, count) in currentBeams) {
                when (nextRow[colIndex]) {
                    '.', 'S' -> {
                        nextBeams[colIndex] = nextBeams.getOrDefault(colIndex, 0L) + count
                    }
                    '^' -> {
                        nextBeams[colIndex - 1] = nextBeams.getOrDefault(colIndex - 1, 0L) + count
                        nextBeams[colIndex + 1] = nextBeams.getOrDefault(colIndex + 1, 0L) + count
                    }
                }
            }
            currentBeams = nextBeams
        }

        return currentBeams.values.sum()
    }

    private fun createNextBeams(rowIndex: Int, input: List<CharArray>): Pair<Int, List<CharArray>>{
        var splits = 0
        input[rowIndex].forEachIndexed {index, char->
            if(char == 'S') {
                when(input[rowIndex+1][index]) {
                    '.' -> input[rowIndex+1][index] = 'S'
                    '^' -> {
                        input[rowIndex+1][index-1] = 'S'
                        input[rowIndex+1][index+1] = 'S'
                        splits++
                    }
                }
            }
        }
        return Pair(splits, input)
    }
}