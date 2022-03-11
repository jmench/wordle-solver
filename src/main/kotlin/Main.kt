import java.io.File
import java.util.ArrayList

const val filePath = "./src/main/resources/sorted-words.txt"
const val correctSpotChar = " 🟩"
const val incorrectSpotChar = " 🟨"
const val impossibleChar = " ⬛"
const val gameBoardWinner = "ggggg"

fun main() {
    // Set up the logic
    val sortedWords : ArrayList<String> = ArrayList()
    val possibleWords : ArrayList<String> = ArrayList()
    var gameBoard: String

    File(filePath).forEachLine {
        sortedWords.add(it)
        possibleWords.add(it)
    }

    val wordle = sortedWords.random()

    printInstructions()
    val gameChoice = readLine().toString()

    println("Please enter your first guess: ")
    for (i in 1..6) {
        println("Guess $i:")
        val guess = getGuess(sortedWords)
        if (gameChoice == "1") {
            gameBoard = updateGameBoard(guess, wordle)
            getPossibleWordsBasedOnGameBoard(guess, gameBoard, possibleWords)
            getAndPrintPrettyGameBoard(gameBoard)
        } else {
            gameBoard = getGuessStatus(guess)
            getPossibleWordsBasedOnGameBoard(guess, gameBoard, possibleWords)
        }
        if (gameBoard == gameBoardWinner) {
            println("you win")
            return
        }
        println("Number of possible words: " + possibleWords.size)
        println(possibleWords)
    }
    print("you lose -- word was $wordle")
}

fun getAndPrintPrettyGameBoard(gameBoard: String) {
    var prettyGameBoard = ""
    val gameBoardArr = gameBoard.toCharArray()
    for (letter in gameBoardArr) {
        prettyGameBoard += when (letter) {
            'g' -> { correctSpotChar }
            'y' -> { incorrectSpotChar }
            else -> { impossibleChar }
        }
    }
    println(prettyGameBoard)
}

fun updateGameBoard(guess: String, wordle: String): String {
    var gameBoard = ""
    val guessArr = (guess.toCharArray())
    val wordleArr = (wordle.toCharArray())
    for (i in guessArr.indices) {
        if (guessArr[i] == wordleArr[i]) {
            gameBoard += 'g'
            wordleArr[i] = '-' // need to keep the solved letter from being tracked
        } else if (wordle.contains(guessArr[i])) {
            gameBoard += 'y'
        } else {
            gameBoard += 'b'
        }
    }
    return handleRepeatedLetters(guess, gameBoard)
}

fun getGuessStatus(guess: String) : String {
    println("Please input the results of your word")
    println("\'g\' for green | \'y\' for yellow | \'b\' for black")
    val gameBoard = readLine().toString()
    return handleRepeatedLetters(guess, gameBoard)
}

fun getPossibleWordsBasedOnGameBoard(guess: String, gameBoard: String, possibleWords: ArrayList<String>) {
    val gameBoardArr = gameBoard.toCharArray()
    val guessArr = guess.toCharArray()
    for (i in guessArr.indices) {
        val letter = guessArr[i]
        if (gameBoardArr[i] == '-') {
            // repeated letter and we don't do anything
        }
        else if (gameBoardArr[i] == 'g') {
            letterInCorrectSpot(letter, i, possibleWords)
        }
        else if (gameBoardArr[i] == 'y') {
            letterInWrongSpot(letter, i, possibleWords)
        }
        else {
            letterNotInWord(letter, possibleWords)
        }
    }
}

fun letterInCorrectSpot(letter:Char, index:Int, possibleWords: ArrayList<String>) {
    val wordsToRemove : ArrayList<String> = ArrayList()
    for (word in possibleWords) {
        val charWord = word.toCharArray()
        if (charWord[index] != letter) {
            wordsToRemove.add(word)
        }
    }
    possibleWords.removeAll(wordsToRemove.toSet())
}

fun letterInWrongSpot(letter:Char, index:Int, possibleWords: ArrayList<String>) {
    val wordsToRemove : ArrayList<String> = ArrayList()
    for (word in possibleWords) {
        val charWord = word.toCharArray()
        if (charWord[index] == letter) {
            wordsToRemove.add(word)
        } else if (!word.contains(letter)) {
            wordsToRemove.add(word)
        }
    }
    possibleWords.removeAll(wordsToRemove.toSet())
}

fun letterNotInWord(letter:Char, possibleWords: ArrayList<String>) {
    val wordsToRemove : ArrayList<String> = ArrayList()
    for (word in possibleWords) {
        if (word.contains(letter)) {
            wordsToRemove.add(word)
        }
    }
    possibleWords.removeAll(wordsToRemove.toSet())
}

fun handleRepeatedLetters(guess: String, gameBoard: String) : String {
    val gameBoardArr = gameBoard.toCharArray()
    val guessArr = guess.toCharArray()
    for (i in guessArr.indices) {
        for (k in guessArr.indices) {
            if (guessArr[i] == guessArr[k] && i != k) {
                if (gameBoardArr[i] == 'g' && gameBoardArr[k] != 'g') {
                    gameBoardArr[k] = '-'
                } else if (gameBoardArr[k] == 'g') {
                    gameBoardArr[i] = '-'
                } else if (gameBoardArr[i] == 'y' && gameBoardArr[k] != 'y') {
                    gameBoardArr[k] = '-'
                }
            }
        }
    }
    var game = ""
    for(i in gameBoardArr.indices) {
        game += gameBoardArr[i]
    }
   return game
}

fun isValid(guess: String, sortedWords: ArrayList<String>) : Boolean {
    return if (sortedWords.contains(guess)) {
        true
    } else {
        println("invalid word, please enter a valid 5 letter word")
        false
    }
}

fun getGuess(sortedWords: ArrayList<String>): String {
    var guess : String
    do {
        guess = readLine().toString()
    } while(!isValid(guess, sortedWords))
    return guess
}

fun printInstructions() {
    println("Welcome to the Wordle Solver!")
    println("Here's how this works:")
    println("After you enter your guess, the solver will print a list of all possible words")
    println("Choose a word from the list as your next guess, and repeat the process until you've solved the wordle!")
    println("Enter 1 if you want to play a random wordle")
    println("Enter 2 if you want help solving your daily wordle")
}