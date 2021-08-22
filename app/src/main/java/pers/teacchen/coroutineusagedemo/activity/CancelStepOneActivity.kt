package pers.teacchen.coroutineusagedemo.activity

import android.os.Bundle
import android.view.View
import kotlinx.coroutines.*
import pers.teacchen.coroutineusagedemo.R
import pers.teacchen.coroutineusagedemo.base.BaseActivity
import pers.teacchen.coroutineusagedemo.databinding.ActivityCancelStepOneBinding
import pers.teacchen.coroutineusagedemo.helper.*
import java.util.*

class CancelStepOneActivity : BaseActivity<ActivityCancelStepOneBinding>() {

    private val jobList = LinkedList<Job>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = getString(R.string.coroutine_cancel_step_one)
        onClickListener.attachToViews(
            viewBinding.loopBtn,
            viewBinding.loopBreakBtn,
            viewBinding.loopEnsureBtn,
            viewBinding.cancelBtn
        )
        viewBinding.msgShowRv.initLogConfig(this, msgAdapter)
    }

    override fun onClick(v: View) {
        when (v) {
            viewBinding.loopBtn -> {
                "loopBtn click".let {
                    myLog(it)
                    updateMsgShow(buildUIMsg(it), viewBinding.msgShowRv)
                }
                val job = GlobalScope.launch(Dispatchers.IO) {
                    val stringBuilder = StringBuilder()
                    "Coroutine IO runs (from loopBtn)".let {
                        myLog(it)
                        stringBuilder.append(buildUIMsg(it))
                        stringBuilder.append("\n")
                    }

                    var curMillis = System.currentTimeMillis()
                    var uiRecordMilli = curMillis + ONE_SECOND
                    val targetMilli = curMillis + FIVE_SECONDS
                    while (curMillis < targetMilli) {
                        val msg = "looping (from loopBtn)"
                        myLog(msg)
                        /* 1秒拼接一次UI展示字符串避免过长 */
                        if (curMillis - uiRecordMilli > ONE_SECOND) {
                            stringBuilder.append(buildUIMsg(msg))
                            stringBuilder.append("\n")
                            uiRecordMilli = curMillis
                        }
                        curMillis = System.currentTimeMillis()
                    }
                    "loop finished (from loopBtn)".let {
                        myLog(it)
                        stringBuilder.append(buildUIMsg(it))
                    }
                    runOnUiThread {
                        updateMsgShow(stringBuilder.toString(), viewBinding.msgShowRv)
                    }
                }
                jobList.add(job)
            }

            viewBinding.loopBreakBtn -> {
                "loopBreakBtn click".let {
                    myLog(it)
                    updateMsgShow(buildUIMsg(it), viewBinding.msgShowRv)
                }
                val job = GlobalScope.launch(Dispatchers.IO) {
                    val stringBuilder = StringBuilder()
                    "Coroutine IO runs (from loopBreakBtn)".let {
                        myLog(it)
                        stringBuilder.append(buildUIMsg(it))
                        stringBuilder.append("\n")
                    }
                    var curMillis = System.currentTimeMillis()
                    var uiRecordMilli = curMillis + ONE_SECOND
                    val targetMilli = curMillis + FIVE_SECONDS
                    while (curMillis < targetMilli) {
                        if (!isActive) {
                            break
                        }
                        val msg = "looping (from loopBreakBtn)"
                        myLog(msg)
                        /* 1秒拼接一次UI展示字符串避免过长 */
                        if (curMillis - uiRecordMilli >= ONE_SECOND) {
                            stringBuilder.append(buildUIMsg(msg))
                            stringBuilder.append("\n")
                            uiRecordMilli = curMillis
                        }
                        curMillis = System.currentTimeMillis()
                    }
                    "loop finished (from loopBreakBtn)".let {
                        myLog(it)
                        stringBuilder.append(buildUIMsg(it))
                    }
                    runOnUiThread {
                        updateMsgShow(stringBuilder.toString(), viewBinding.msgShowRv)
                    }
                }
                jobList.add(job)
            }

            viewBinding.loopEnsureBtn -> {
                "loopEnsureBtn click".let {
                    myLog(it)
                    updateMsgShow(buildUIMsg(it), viewBinding.msgShowRv)
                }
                val job = GlobalScope.launch(Dispatchers.IO) {
                    val stringBuilder = StringBuilder()
                    "Coroutine IO runs (from loopEnsureBtn)".let {
                        myLog(it)
                        stringBuilder.append(buildUIMsg(it))
                        stringBuilder.append("\n")
                    }
                    var curMillis = System.currentTimeMillis()
                    var uiRecordMilli = curMillis + ONE_SECOND
                    val targetMilli = curMillis + FIVE_SECONDS
                    while (curMillis < targetMilli) {
                        ensureActive()
                        val msg = "looping (from loopEnsureBtn)"
                        myLog(msg)
                        /* 1秒拼接一次UI展示字符串避免过长 */
                        if (curMillis - uiRecordMilli >= ONE_SECOND) {
                            stringBuilder.append(buildUIMsg(msg))
                            stringBuilder.append("\n")
                            uiRecordMilli = curMillis
                        }
                        curMillis = System.currentTimeMillis()
                    }
                    "loop finished (from loopEnsureBtn)".let {
                        myLog(it)
                        stringBuilder.append(buildUIMsg(it))
                    }
                    runOnUiThread {
                        updateMsgShow(stringBuilder.toString(), viewBinding.msgShowRv)
                    }
                }
                jobList.add(job)
            }

            viewBinding.cancelBtn -> {
                "cancelBtn click".let {
                    myLog(it)
                    updateMsgShow(buildUIMsg(it), viewBinding.msgShowRv)
                }
                jobList.forEach {
                    it.cancel()
                }
                "All jobs in list canceled".let {
                    myLog(it)
                    updateMsgShow(buildUIMsg(it), viewBinding.msgShowRv)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        jobList.forEach {
            it.cancel()
        }
    }
}