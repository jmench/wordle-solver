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

    println("Please enter your first guess: ")
    for (i in 1..6) {
        println("Guess $i:")
        val guess = getGuess()
        updateGameBoard(guess, possibleWords)
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
        if (guessArr[i] == wordleArr[i]) {
            //println(guessArr[i] + " is in the correct position")
            gameBoard += correctSpotChar
            wordleArr[i] = '-'
            val wordsToRemove : ArrayList<String> = ArrayList()
            for (word in possibleWords) {
                val charWord = word.toCharArray()
                if (charWord[i] != guessArr[i]) {
                    wordsToRemove.add(word)
                }
            }
            possibleWords.removeAll(wordsToRemove.toSet())
        } else if (wordle.contains(guessArr[i])) {
            //println(guessArr[i] + " is in the word, but not in this position")
            gameBoard += incorrectSpotChar
            val wordsToRemove : ArrayList<String> = ArrayList()
            for (word in possibleWords) {
                val charWord = word.toCharArray()
                if (charWord[i] == (guessArr[i])) {
                    wordsToRemove.add(word)
                } else if (!word.contains(guessArr[i])) {
                    wordsToRemove.add(word)
                }
            }
            possibleWords.removeAll(wordsToRemove.toSet())
        } else {
            //println(guessArr[i] + " is not in the word at all")
            gameBoard += impossibleChar
            val wordsToRemove : ArrayList<String> = ArrayList()
            for (word in possibleWords) {
                if (word.contains(guessArr[i])) {
                    wordsToRemove.add(word)
                }
            }
            possibleWords.removeAll(wordsToRemove.toSet())
        }
    }
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