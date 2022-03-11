import java.io.File
import java.util.ArrayList

var wordle : String = ""
var sortedWords : ArrayList<String> = ArrayList()
var possibleWords : ArrayList<String> = ArrayList()
val filePath = "/Users/jmench/Documents/dev/wordle-solver/src/main/resources/sorted-words.txt"
var gameBoard = ""
var guessedLetters : ArrayList<String> = ArrayList()
val correctSpotChar = " 🟩"
val incorrectSpotChar = " 🟨"
val impossibleChar = " ⬛"

fun main() {
    // Set up the logic
    File(filePath).forEachLine {
        sortedWords.add(it)
    }
    wordle = sortedWords.random()
    possibleWords = sortedWords

    printInstructions()
    println("Enter 1 if you want to play a random wordle")
    println("Enter 2 if you want help solving your daily wordle")
    val gameChoice = readLine().toString()

    println("Please enter your first guess: ")
    for (i in 1..6) {
        println("Guess $i:")
        val guess = getGuess()
        if (gameChoice == "1") {
            updateGameBoard(guess, possibleWords)
        } else {
            val guessStatus = getGuessStatus()
            getPossibleWordsBasedOnGameBoard(guessStatus, guess, possibleWords)
        }
        println(gameBoard)
        if (guess.contentEquals(wordle)) {
            println("you win")
            return
        }
        println("Number of possible words: " + possibleWords.size)
        println(possibleWords)
    }
    print("you lose")
}

fun updateGameBoard(guess: String, possibleWords: ArrayList<String>) {
    gameBoard = ""
    val guessArr = (guess.toCharArray())
    val wordleArr = (wordle.toCharArray())
    for (i in guessArr.indices) {
        val letter = guessArr[i]
        if (guessArr[i] == wordleArr[i]) {
            gameBoard += correctSpotChar
            wordleArr[i] = '-'
            letterInCorrectSpot(letter, i, possibleWords)
        } else if (wordle.contains(guessArr[i])) {
            gameBoard += incorrectSpotChar
            letterInWrongSpot(letter, i, possibleWords)
        } else {
            gameBoard += impossibleChar
            letterNotInWord(letter, possibleWords)
        }
    }
}

fun getPossibleWordsBasedOnGameBoard(guessStatus: String, guess : String, possibleWords: ArrayList<String>) {
    var gameBoardArr = gameBoard.toCharArray()
    val guessArr = guessStatus.toCharArray()
    val guessLettersArr = guess.toCharArray()
    for (i in gameBoardArr.indices) {
        val letter = gameBoardArr[i]
        if (gameBoardArr[i] == 'g') {
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

fun getGuessStatus() : String {
    gameBoard = readLine().toString()
    return gameBoard
}

fun isValid(guess: String) : Boolean {
    return if (sortedWords.contains(guess)) {
        true
    } else {
        println("invalid word, please enter a valid 5 letter word")
        false
    }
}

fun getGuess() : String {
    var guess : String
    do {
        guess = readLine().toString()
    } while(!isValid(guess))
    possibleWords.remove(guess)
    return guess
}

fun printInstructions() {
    println("Welcome to the Wordle Solver!")
    println("Here's how this works:")
    println("After you enter your guess, the solver will print a list of all possible words")
    println("Choose a word from the list as your next guess, and repeat the process until you've solved the wordle!")
}