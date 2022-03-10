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
    //wordle = sortedWords.random()
    wordle = "mourn"
    possibleWords = sortedWords

    printInstructions()

    println("Please enter your first guess: ")
    for (i in 1..6) {
        println("Guess $i:")
        val guess = getGuess()
        val guessStatus = getGuessStatus()
        getPossibleWordsBasedOnGameBoard(guessStatus, guess, possibleWords)
        //updateGameBoard(guess, possibleWords)
        println(gameBoard)
//        if (guess.contentEquals(wordle)) {
//            println("you win")
//            return
//        }
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

fun getPossibleWordsBasedOnGameBoard(guessStatus: String, guess : String, possibleWords: ArrayList<String>) {
    // Need the letters at each position from the guess
    // Need the status of each letter in the position
    // same logic as above
    var gameBoardArr = gameBoard.toCharArray()
    val guessArr = guessStatus.toCharArray()
    val guessLettersArr = guess.toCharArray()
    for (i in gameBoardArr.indices) {
        // if the letter is in the correct spot
        println("letter is " + gameBoardArr[i])
        if (gameBoardArr[i] == 'g') {
            // get the letter from the guess
            val letter = guessLettersArr[i]
            print(letter + " is in the correct spot, removing words without this")
            // loop through possible words and keep only those with letter in correct spot
            val wordsToRemove : ArrayList<String> = ArrayList()
            for (word in possibleWords) {
                val charWord = word.toCharArray()
                if (charWord[i] != letter) {
                    wordsToRemove.add(word)
                }
            }
            possibleWords.removeAll(wordsToRemove.toSet())
        }
        // if the letter is there but in wrong spot
        else if (gameBoardArr[i] == 'y') {
            val letter = guessLettersArr[i]
            print(letter + " is in the word but not the correct spot")
            val wordsToRemove : ArrayList<String> = ArrayList()
            for (word in possibleWords) {
                val charWord = word.toCharArray()
                if (charWord[i] == letter) {
                    wordsToRemove.add(word)
                } else if (!word.contains(letter)) {
                    wordsToRemove.add(word)
                }
            }
            possibleWords.removeAll(wordsToRemove.toSet())
        }
        // if the letter is not in the word at all
        else {
            val letter = guessLettersArr[i]
            print(letter + " is not in the word at all")
            val wordsToRemove : ArrayList<String> = ArrayList()
            for (word in possibleWords) {
                if (word.contains(letter)) {
                    wordsToRemove.add(word)
                }
            }
            possibleWords.removeAll(wordsToRemove.toSet())
        }
    }
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