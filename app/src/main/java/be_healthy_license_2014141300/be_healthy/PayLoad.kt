package be_healthy_license_2014141300.be_healthy

import java.util.*

class PayLoad {

    private val symbols = CharArray(37)
    init
    {
        for (idx in 0..10)
            symbols[idx] = ('0' + idx)
        for (idx in 10..36)
            symbols[idx] = ('a' + idx - 10)
    }

    fun getPayLoad(): String {
        val randomString = RandomString(36)
        return randomString.nextString()
    }

    inner class RandomString(length: Int) {
        private val random = Random()
        private val buf: CharArray
        init {
            if (length < 1)
                throw IllegalArgumentException("length < 1: $length")
            buf = CharArray(length)
        }
        fun nextString(): String {
            for (idx in buf.indices)
                buf[idx] = symbols[random.nextInt(symbols.size)]
            return String(buf)
        }
    }
}