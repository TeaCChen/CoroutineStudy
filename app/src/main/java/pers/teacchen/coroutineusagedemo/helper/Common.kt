package pers.teacchen.coroutineusagedemo.helper

import java.util.*

val Any.objIdentityStr: String
    get() = "${javaClass.name}@${hashCode()}"

val currentTimeStr: String
    get() {
        val calendar = Calendar.getInstance(Locale.CHINA)
        return buildString {
            append(calendar.get(Calendar.HOUR_OF_DAY).timeStr)
            append(":")
            append(calendar.get(Calendar.MINUTE).timeStr)
            append(":")
            append(calendar.get(Calendar.SECOND).timeStr)
            append(".")
            append(calendar.get(Calendar.MILLISECOND).milliStr)
        }
    }

private val Int.timeStr: String
    get() = if (this < 10) {
        "0$this"
    } else {
        this.toString()
    }

private val Int.milliStr: String
    get() = when {
        this < 10 -> "00$this"
        this < 100 -> "0$this"
        else -> this.toString()
    }