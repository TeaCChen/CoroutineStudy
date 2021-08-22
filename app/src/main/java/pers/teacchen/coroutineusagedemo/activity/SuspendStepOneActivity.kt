package pers.teacchen.coroutineusagedemo.activity

import android.os.Bundle
import android.view.View
import kotlinx.coroutines.*
import pers.teacchen.coroutineusagedemo.R
import pers.teacchen.coroutineusagedemo.base.BaseActivity
import pers.teacchen.coroutineusagedemo.databinding.ActivitySuspendStepOneBinding
import pers.teacchen.coroutineusagedemo.helper.*

class SuspendStepOneActivity : BaseActivity<ActivitySuspendStepOneBinding>() {

    private val scope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = getString(R.string.coroutine_suspend_step_one)
        onClickListener.attachToViews(
            viewBinding.launchBtn,
            viewBinding.joinBtn,
            viewBinding.asyncBtn,
            viewBinding.awaitBtn
        )
        viewBinding.msgShowRv.initLogConfig(this, msgAdapter)
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    private var job: Job? = null
    private var deferred: Deferred<String>? = null

    override fun onClick(v: View) {
        when (v) {
            viewBinding.launchBtn -> {
                "Clicked launchBtn".let {
                    myLog(it)
                    updateMsgShow(buildUIMsg(it), viewBinding.msgShowRv)
                }
                job?.cancel()
                job = scope.launch(Dispatchers.IO) {
                    val stringBuilder = StringBuilder()
                    "Coroutine IO runs (from launchBtn)".let {
                        myLog(it)
                        stringBuilder.append(buildUIMsg(it))
                        stringBuilder.append("\n")
                    }
                    Thread.sleep(FIVE_SECONDS)
                    "Coroutine IO runs after thread sleep (from launchBtn)".let {
                        myLog(it)
                        stringBuilder.append(buildUIMsg(it))
                        stringBuilder.append("\n")
                    }
                    withContext(Dispatchers.Main) {
                        updateMsgShow(stringBuilder.toString(), viewBinding.msgShowRv)
                    }
                }
            }

            viewBinding.joinBtn -> {
                "Clicked joinBtn".let {
                    myLog(it)
                    updateMsgShow(buildUIMsg(it), viewBinding.msgShowRv)
                }
                scope.launch(Dispatchers.Main) {
                    "Coroutine Main runs (from joinBtn)".let {
                        myLog(it)
                        updateMsgShow(buildUIMsg(it), viewBinding.msgShowRv)
                    }
                    val jobNonNull = job ?: throw IllegalStateException("No job launched yet!")
                    jobNonNull.join()
                    "Coroutine Main runs after join() (from joinBtn)".let {
                        myLog(it)
                        updateMsgShow(buildUIMsg(it), viewBinding.msgShowRv)
                    }
                }
            }

            viewBinding.asyncBtn -> {
                "Clicked asyncBtn".let {
                    myLog(it)
                    updateMsgShow(buildUIMsg(it), viewBinding.msgShowRv)
                }
                deferred?.cancel()
                deferred = scope.async(Dispatchers.IO) {
                    val stringBuilder = StringBuilder()
                    "Coroutine IO runs (from asyncBtn)".let {
                        myLog(it)
                        stringBuilder.append(buildUIMsg(it))
                        stringBuilder.append("\n")
                    }
                    Thread.sleep(FIVE_SECONDS)
                    "TeaC".apply {
                        "Coroutine IO runs after thread sleep: $this (from asyncBtn)".let {
                            myLog(it)
                            stringBuilder.append(buildUIMsg(it))
                            stringBuilder.append("\n")
                        }
                        withContext(Dispatchers.Main) {
                            updateMsgShow(stringBuilder.toString(), viewBinding.msgShowRv)
                        }
                    }
                }
            }

            viewBinding.awaitBtn -> {
                "Clicked awaitBtn".let {
                    myLog(it)
                    updateMsgShow(buildUIMsg(it), viewBinding.msgShowRv)
                }
                scope.launch(Dispatchers.Main) {
                    "Coroutine Main runs (from awaitBtn)".let {
                        myLog(it)
                        updateMsgShow(buildUIMsg(it), viewBinding.msgShowRv)
                    }
                    val deferredNonNull =
                        deferred ?: throw IllegalStateException("No deferred async yet!")
                    val ret = deferredNonNull.await()
                    "Coroutine Main runs after await(): $ret (from awaitBtn)".let {
                        myLog(it)
                        updateMsgShow(buildUIMsg(it), viewBinding.msgShowRv)
                    }
                }
            }

            viewBinding.cancelBtn -> {
                "Clicked cancelBtn".let {
                    myLog(it)
                    updateMsgShow(buildUIMsg(it), viewBinding.msgShowRv)
                }
                scope.coroutineContext.cancelChildren()
            }
        }
    }
}