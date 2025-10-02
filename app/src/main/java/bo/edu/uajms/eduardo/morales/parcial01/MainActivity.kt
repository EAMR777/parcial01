package bo.edu.uajms.eduardo.morales.parcial01
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {

//Eduardo Morales Rojas


    private lateinit var boardButtons: Array<Array<Button>>
    private val size = 4
    private var board = Array(size) { Array(size) { 0 } }
    private val goal = (1..15).toList() + listOf(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        boardButtons = Array(size) { row ->
            Array(size) { col ->
                val id = resources.getIdentifier("BTN$row$col", "id", packageName)
                findViewById<Button>(id)
            }
        }

        findViewById<Button>(R.id.BTNShuffle).setOnClickListener {
            shuffleBoard()
        }

        findViewById<Button>(R.id.BTNReset).setOnClickListener {
            resetBoard()
        }

        setupListeners()
        resetBoard()
    }

    private fun setupListeners() {
        for (i in 0 until size) {
            for (j in 0 until size) {
                boardButtons[i][j].setOnClickListener {
                    moveTile(i, j)
                }
            }
        }
    }

    private fun resetBoard() {
        val numbers = (1..15).toList() + listOf(0)
        for (i in 0 until size) {
            for (j in 0 until size) {
                val value = numbers[i * size + j]
                board[i][j] = value
                boardButtons[i][j].text = if (value == 0) "" else value.toString()
                boardButtons[i][j].isEnabled = true
            }
        }
    }

    private fun shuffleBoard() {
        val numbers = (1..15).toMutableList()
        numbers.add(0)
        numbers.shuffle()

        while (!isSolvable(numbers)) {
            numbers.shuffle()
        }

        for (i in 0 until size) {
            for (j in 0 until size) {
                val value = numbers[i * size + j]
                board[i][j] = value
                boardButtons[i][j].text = if (value == 0) "" else value.toString()
                boardButtons[i][j].isEnabled = true
            }
        }
    }

    private fun isSolvable(list: List<Int>): Boolean {
        var inversions = 0
        val noZero = list.filter { it != 0 }
        for (i in noZero.indices) {
            for (j in i + 1 until noZero.size) {
                if (noZero[i] > noZero[j]) inversions++
            }
        }

        val blankRow = list.indexOf(0) / size
        return (inversions + blankRow) % 2 == 0
    }

    private fun moveTile(row: Int, col: Int) {
        val directions = listOf(
            Pair(-1, 0), Pair(1, 0),
            Pair(0, -1), Pair(0, 1)
        )

        for ((dx, dy) in directions) {
            val newRow = row + dx
            val newCol = col + dy
            if (newRow in 0 until size && newCol in 0 until size && board[newRow][newCol] == 0) {

                board[newRow][newCol] = board[row][col]
                board[row][col] = 0

                boardButtons[newRow][newCol].text = board[newRow][newCol].toString()
                boardButtons[row][col].text = ""

                checkWin()
                break
            }
        }
    }

    private fun checkWin() {
        val current = board.flatten()
        if (current == goal) {
            Toast.makeText(this, "¡Puzzle resuelto!", Toast.LENGTH_LONG).show()
            disableBoard()
        }
    }

    private fun disableBoard() {
        for (i in 0 until size) {
            for (j in 0 until size) {
                boardButtons[i][j].isEnabled = false
            }
        }
    }
}