package pers.teacchen.coroutineusagedemo.helper

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.CoroutineContext

fun buildUIMsg(msg: String) =
    buildString {
        val curThread = Thread.currentThread()
        append(currentTimeStr)
        append(" ")
        append(msg)
        append("\n  ::running in Thread:[id:")
        append(curThread.id)
        append("][name:")
        append(curThread.name)
        append("]")
    }

fun myLog(msg: String) {
    val curThread = Thread.currentThread()
    Log.d(
        "chenhj",
        "$msg ::running in Thread:[id:${curThread.id}][name:${curThread.name}]"
    )
}

fun CoroutineContext.printLog(msg: String) =
    myLog(
        "$msg, coroutineContext:${objIdentityStr}, " +
                "continuationInterceptor:${this[ContinuationInterceptor]?.objIdentityStr}"
    )

fun CoroutineScope.printLog(msg: String) = coroutineContext.printLog(msg)
