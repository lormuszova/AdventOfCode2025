fun main() {
    fun part1(input: List<String>): Int {
        val data = processInput(input)
        return findAllPaths(data)
    }

    fun part2(input: List<String>): Long {
        val data = processInput(input)
        return allPathsThroughtExpectedStops(data)
    }

    // Or read a large test input from the `src/Day11_test2.txt` file:
    val testInput1 = readInput("Day11_test1")
    val testInput2 = readInput("Day11_test2")

    check(part1(testInput1) == 5)
    check(part2(testInput2) == 2L)

    // Read the input from the `src/Day11.txt` file.
    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}

private fun processInput(input: List<String>): Map<String, List<String>> {
    val data = mutableMapOf<String, List<String>>()
    input.map { line ->
        val (input, output) = line.split(": ")
        data[input] = output.split(" ")
    }
    return data
}

private fun findAllPaths(input: Map<String, List<String>>): Int {
    return findNextStepIfNotFinal("you", input)
}

private fun findNextStepIfNotFinal(input: String, data: Map<String, List<String>>): Int {
    if (input == "out") return 1
    var result = 0

    data[input]!!.forEach {
        result += findNextStepIfNotFinal(it, data)
    }
    return result
}

private fun countPaths(graph: Map<String, List<String>>, start: String, end: String): Long {
    val memo = mutableMapOf<String, Long>()
    return searchPaths(graph, end, start, memo)
}

private fun searchPaths(
    graph: Map<String, List<String>>,
    end: String,
    current: String,
    memo: MutableMap<String, Long>
): Long {
    if (current == end) return 1L

    val cached = memo[current]
    if (cached != null) return cached

    var total = 0L
    val nextNodes = graph[current].orEmpty()
    for (next in nextNodes) {
        total += searchPaths(graph, end, next, memo)
    }

    memo[current] = total
    return total
}

private fun allPathsThroughtExpectedStops(input: Map<String, List<String>>): Long {
    val path1 =
        countPaths(input, "svr", "dac") *
                countPaths(input, "dac", "fft") *
                countPaths(input, "fft", "out")

    val path2 =
        countPaths(input, "svr", "fft") *
                countPaths(input, "fft", "dac") *
                countPaths(input, "dac", "out")

    return path1 + path2
}