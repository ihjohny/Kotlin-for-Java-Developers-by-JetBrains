package nicestring

fun String.isNice(): Boolean {
    val case1 = setOf("bu", "ba", "be").none { this.contains(it) }
    val case2 = count { it in "aeiou" } >= 3
    val case3 = zipWithNext().any { it.first == it.second }
    return listOf(case1, case2, case3).count { it } > 1
}