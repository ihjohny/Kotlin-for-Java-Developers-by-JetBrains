package mastermind

data class Evaluation(val rightPosition: Int, val wrongPosition: Int)

fun evaluateGuess(secret: String, guess: String): Evaluation{
    var right = 0
    var wrong = 0
    var lock = Array(4) {false}
    for ((i,s) in secret.withIndex()){
        for ((j,g) in guess.withIndex()){
            if(s == g && i == j ) {
                right++
                lock[j] = true
                break
            }
            else if(s == g &&
                    secret[j] != guess[j] &&
                    secret[i] != guess[i] &&
                    !lock[j]) {
                wrong++
                lock[j] = true
                break
            }
        }
    }
    return Evaluation(right, wrong)
}