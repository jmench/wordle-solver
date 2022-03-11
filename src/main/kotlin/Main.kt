import java.io.File
import java.util.ArrayList

var wordle : String = ""
var sortedWords : ArrayList<String> = ArrayList()
var possibleWords : ArrayList<String> = ArrayList()
const val filePath = "./src/main/resources/sorted-words.txt"
var gameBoard = ""
const val correctSpotChar = " 🟩"
const val incorrectSpotChar = " 🟨"
const val impossibleChar = " ⬛"

fun main() {
    // Set up the logic
    File(filePath).forEachLine {
        sortedWords.add(it)
    }

    possibleWords = sortedWords

    printInstructions()
    val gameChoice = readLine().toString()

    println("Please enter your first guess: ")
    for (i in 1..6) {
        println("Guess $i:")
        val guess = getGuess()
        if (gameChoice == "1") {
            wordle = sortedWords.random()
            updateGameBoard(guess, possibleWords)
        } else {
            getGuessStatus(guess)
            getPossibleWordsBasedOnGameBoard(guess, possibleWords)
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

fun getPossibleWordsBasedOnGameBoard(guess: String, possibleWords: ArrayList<String>) {
    val gameBoardArr = gameBoard.toCharArray()
    val guessArr = guess.toCharArray()
    println("Getting possible words for - " + guess)
    println(gameBoard)
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

fun getGuessStatus(guess: String) {
    gameBoard = readLine().toString()
    // check for repeat letters and prioritize the one in the correct (green) spot
    // else choose first instance and make yellow, ignore second instance (make black but don't remove possible words)
    // otherwise it doesn't matter because the letter isnt in the word
    handleRepeatedLetters(guess)
}

fun handleRepeatedLetters(guess: String) {
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
    gameBoard = game
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
    println("Enter 1 if you want to play a random wordle")
    println("Enter 2 if you want help solving your daily wordle")
}