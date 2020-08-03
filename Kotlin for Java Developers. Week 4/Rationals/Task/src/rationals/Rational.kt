package rationals

import java.math.BigInteger
import java.math.BigInteger.ONE
import java.math.BigInteger.ZERO


class Rational private constructor(val n: BigInteger, val d: BigInteger): Comparable<Rational> {

    companion object{
        fun create(n: BigInteger, d: BigInteger): Rational {
            return normalize(n, d)
        }
        private fun normalize(n: BigInteger, d: BigInteger): Rational {
            require(d != ZERO) { "Denominator must be non-zero" }
            val g = n.gcd(d)
            val sign = d.signum().toBigInteger()
            return Rational(n / g * sign, d / g * sign)
        }
    }

    override fun toString(): String {
        return if(d == ONE)
            "$n"
        else
            "$n/$d"
    }

    operator fun unaryMinus(): Rational = create(-this.n, this.d)

    operator fun plus(rational: Rational): Rational = create(n * rational.d + rational.n * d,
            d * rational.d)

    operator fun minus(rational: Rational): Rational = create(n * rational.d - rational.n * d,
            d * rational.d)

    operator fun times(rational: Rational): Rational = create(n * rational.n, rational.d * d)

    operator fun div(rational: Rational): Rational = create(n * rational.d , rational.n * d)

    override operator fun compareTo(other: Rational): Int {
        return (n * other.d - other.n * d).signum()
    }

    override fun equals(other: Any?): Boolean {
        return this.toString() == other.toString()
    }

    override fun hashCode(): Int {
        var result = n.hashCode()
        result = 31 * result + d.hashCode()
        return result
    }

    operator fun contains(rational: Rational): Boolean {
        return this == rational
    }
}


fun main() {
    val half = 1 divBy 2
    val third = 1 divBy 3

    val sum: Rational = half + third
    println(5 divBy 6 == sum)

    val difference: Rational = half - third
    println(1 divBy 6 == difference)

    val product: Rational = half * third
    println(1 divBy 6 == product)

    val quotient: Rational = half / third
    println(3 divBy 2 == quotient)

    val negation: Rational = -half
    println(-1 divBy 2 == negation)

    println((2 divBy 1).toString() == "2")
    println((-2 divBy 4).toString() == "-1/2")
    println("117/1098".toRational().toString() == "13/122")

    val twoThirds = 2 divBy 3
    println(half < twoThirds)

    println(half in third..twoThirds)

    println(2000000000L divBy 4000000000L == 1 divBy 2)

    println("912016490186296920119201192141970416029".toBigInteger() divBy
            "1824032980372593840238402384283940832058".toBigInteger() == 1 divBy 2)
}

infix fun Int.divBy(i: Int): Rational {
    return Rational.create(n = this.toBigInteger(), d= i.toBigInteger())
}

infix fun Long.divBy(l: Long): Rational {
    return Rational.create(n = this.toBigInteger(), d= l.toBigInteger())
}

infix fun BigInteger.divBy(b: BigInteger): Rational {
    return Rational.create(n = this, d= b)
}

operator fun Any.contains(rational: Rational): Boolean {
    return this == rational
}

fun String.toRational(): Rational {
    fun String.toBigIntegerOrFail() =
            toBigIntegerOrNull() ?: throw IllegalArgumentException(
                    "Expecting rational in the form n/d or n"
            )
    if(!this.contains('/'))
        return Rational.create(toBigIntegerOrFail(), ONE)
    val n = this.substringBefore('/').toBigIntegerOrFail()
    val d = this.substringAfter('/').toBigIntegerOrFail()
    return Rational.create(n, d)
}


