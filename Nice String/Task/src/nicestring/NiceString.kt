package nicestring

fun String.isNice(): Boolean {
    var trueCount = 0
    if (badSubStrings()) trueCount++
    if (vowelCount()) trueCount++
    if (doubleLetters()) trueCount++
    return trueCount >= 2
}

fun String.badSubStrings(): Boolean {
    return !this.contains("b[aeu]".toRegex())
}

fun String.vowelCount(): Boolean {
    return "aeiou".sumBy { ch ->
        this.count { it == ch}
    } >= 3
}

fun String.doubleLetters(): Boolean {
    return this.zipWithNext().any { it.first == it.second }
}