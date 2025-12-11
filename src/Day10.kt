import kotlin.math.abs
import kotlin.math.roundToInt

fun main() {
    fun part1(input: List<String>): Int {
        val (diagrams, buttonsWiring, _) = processInput(input)

        return diagrams.indices.sumOf { i ->
            smallestCombinationsForOnOff(diagrams[i], buttonsWiring[i])
        }
    }

    fun part2(input: List<String>): Long {
        val (_, buttonsWiring, joltages) = processInput(input)
        return joltages.indices.sumOf { i ->
            smallestCombinationForJoltageILP(joltages[i], buttonsWiring[i])
        }
    }

    // Or read a large test input from the `src/Day10_test.txt` file:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 7)
    check(part2(testInput) == 33L)

    // Read the input from the `src/Day10.txt` file.
    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}

private fun processInput(input: List<String>): Triple<List<BooleanArray>, List<List<IntArray>>, List<IntArray>> {
    val diagram = mutableListOf<BooleanArray>()
    val buttonWiring = mutableListOf<List<IntArray>>()
    val joltage = mutableListOf<IntArray>()
    input.forEach { line ->
        val oneLineButtonWiring = mutableListOf<IntArray>()
        line.split(" ").forEach { data ->
            if (data[0] == '[') {
                diagram.add(
                    data.substring(1, data.length - 1).map { char ->
                        if (char == '#') true else false
                    }.toBooleanArray()
                )
            }
            if (data[0] == '(') {
                data.substring(1, data.length - 1).split(",").map { it.toInt() }
                    .let { oneLineButtonWiring.add(it.toIntArray()) }
            }
            if (data[0] == '{') joltage.add(
                data.substring(1, data.length - 1).split(",").map { it.toInt() }.toIntArray()
            )
        }
        buttonWiring.add(oneLineButtonWiring)
    }
    return Triple(diagram, buttonWiring, joltage)
}

private fun smallestCombinationsForOnOff(diagram: BooleanArray, buttonWiring: List<IntArray>): Int {
    val n = buttonWiring.size
    for (k in 1..n) {
        val found = combinations(n, k).any { indices ->
            val state = BooleanArray(diagram.size)
            indices.forEach { index ->
                oneOnOffSwitchPushed(state, buttonWiring[index])
            }
            state.contentEquals(diagram)
        }
        if (found) return k
    }
    return 0
}

private fun combinations(n: Int, k: Int): Sequence<List<Int>> = sequence {
    val indices = IntArray(k) { it }
    while (true) {
        yield(indices.toList())
        var i = k - 1
        while (i >= 0 && indices[i] == n - k + i) i--
        if (i < 0) break
        indices[i]++
        for (j in i + 1 until k) indices[j] = indices[j - 1] + 1
    }
}

private fun oneOnOffSwitchPushed(
    startState: BooleanArray,
    oneButtonWiring: IntArray
): BooleanArray {
    oneButtonWiring.forEach { button ->
        startState[button] = !startState[button]
    }
    return startState
}

private fun smallestCombinationForJoltageILP(target: IntArray, buttonWiring: List<IntArray>): Long {
    val rows = target.size
    val columns = buttonWiring.size

    val matrix = Array(rows) { DoubleArray(columns + 1) }

    for (column in 0 until columns) {
        for (row in buttonWiring[column]) {
            matrix[row][column] = 1.0
        }
    }
    for (row in 0 until rows) {
        matrix[row][columns] = target[row].toDouble()
    }

    // Gaussian Elimination to RREF
    var pivotRow = 0
    val pivotCols = IntArray(rows) { -1 }
    val isFreeVar = BooleanArray(columns) { true }

    for (col in 0 until columns) {
        if (pivotRow >= rows) break
        var maxRow = pivotRow
        for (i in pivotRow + 1 until rows) {
            if (abs(matrix[i][col]) > abs(matrix[maxRow][col])) {
                maxRow = i
            }
        }

        if (abs(matrix[maxRow][col]) < 1e-9) continue

        val temp = matrix[pivotRow]
        matrix[pivotRow] = matrix[maxRow]
        matrix[maxRow] = temp

        val div = matrix[pivotRow][col]
        for (j in col until columns + 1) {
            matrix[pivotRow][j] /= div
        }

        for (i in 0 until rows) {
            if (i != pivotRow) {
                val factor = matrix[i][col]
                for (j in col until columns + 1) {
                    matrix[i][j] -= factor * matrix[pivotRow][j]
                }
            }
        }

        pivotCols[pivotRow] = col
        isFreeVar[col] = false
        pivotRow++
    }

    val freeVariables = (0 until columns).filter { isFreeVar[it] }

    var minTotal = Long.MAX_VALUE

    fun searchOverFreeVariables(freeIdx: Int, currentFreeValues: IntArray) {
        if (freeIdx == freeVariables.size) {
            var currentSum = 0L
            val currentSolution = IntArray(columns)

            for (i in freeVariables.indices) {
                currentSolution[freeVariables[i]] = currentFreeValues[i]
                currentSum += currentFreeValues[i]
            }

            var possible = true
            for (row in 0 until rows) {
                val pCol = pivotCols[row]
                if (pCol == -1) {
                    // 0 = Constant? Check for consistency
                    if (abs(matrix[row][columns]) > 1e-9) {
                        possible = false // 0 = 5 implies impossible
                    }
                    continue
                }

                var pivotVal = matrix[row][columns]
                for (free in freeVariables) {
                    pivotVal -= matrix[row][free] * currentSolution[free]
                }

                val rounded = pivotVal.roundToInt()
                if (abs(pivotVal - rounded) > 1e-9 && possible) possible = false
                if (rounded < 0) possible = false

                if (possible) {
                    currentSolution[pCol] = rounded
                    currentSum += rounded
                } else {
                    break
                }
            }

            if (possible) {
                if (currentSum < minTotal) minTotal = currentSum
            }
            return
        }


        for (v in 0..250) {
            currentFreeValues[freeIdx] = v
            searchOverFreeVariables(freeIdx + 1, currentFreeValues)
        }
    }

    searchOverFreeVariables(0, IntArray(freeVariables.size))

    return if (minTotal == Long.MAX_VALUE) 0L else minTotal
}