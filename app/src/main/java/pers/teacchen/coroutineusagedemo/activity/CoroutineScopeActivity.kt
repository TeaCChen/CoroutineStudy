package pers.teacchen.coroutineusagedemo.activity

import android.os.Bundle
import android.view.View
import kotlinx.coroutines.*
import pers.teacchen.coroutineusagedemo.R
import pers.teacchen.coroutineusagedemo.base.BaseActivity
import pers.teacchen.coroutineusagedemo.databinding.ActivityCoroutineScopeBinding
import pers.teacchen.coroutineusagedemo.helper.*

class CoroutineScopeActivity : BaseActivity<ActivityCoroutineScopeBinding>() {

    /**
     * 其实这种写法本质等同于调用 MainScope()，这里的写法参考[MainScope]函数注释中的例子建议
     */
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = getString(R.string.coroutine_scope)
        onClickListener.attachToViews(
            viewBinding.launchByScope,
            viewBinding.cancelByScope,
            viewBinding.cancelChildren,
            viewBinding.launchByGlobalScope,
            viewBinding.launchByNewScope
        )
        viewBinding.msgShowRv.initLogConfig(this, msgAdapter)
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    override fun onClick(v: View) {
        when (v) {
            viewBinding.launchByScope -> {
                scopeLaunch(scope, "launchByScope")
            }

            viewBinding.cancelByScope -> {
                "Clicked cancelByScope".let {
                    myLog(it)
                    updateMsgShow(buildUIMsg(it), viewBinding.msgShowRv)
                }
                scope.cancel()
                "scope.cancel() finished".let {
                    myLog(it)
                    updateMsgShow(buildUIMsg(it), viewBinding.msgShowRv)
                }
            }

            viewBinding.cancelChildren -> {
                "Clicked cancelChildren".let {
                    myLog(it)
                    updateMsgShow(buildUIMsg(it), viewBinding.msgShowRv)
                }
                scope.coroutineContext.cancelChildren()
                "scope.coroutineContext.cancelChildren() finished".let {
                    myLog(it)
                    updateMsgShow(buildUIMsg(it), viewBinding.msgShowRv)
                }
            }

            viewBinding.launchByGlobalScope -> {
                scopeLaunch(GlobalScope, "launchByGlobalScope")
            }

            viewBinding.launchByNewScope -> {
                scopeLaunch(CoroutineScope(Dispatchers.Main), "launchByNewScope")
            }
        }
    }

    private fun scopeLaunch(scope: CoroutineScope, calledMsg: String) {
        "clicked $calledMsg, scopeRef:${scope.objIdentityStr}".let {
            myLog(it)
            updateMsgShow(buildUIMsg(it), viewBinding.msgShowRv)
        }
        scope.launch(Dispatchers.IO) {
            val stringBuilder = StringBuilder()
            "Coroutine IO runs (from ${calledMsg})".let {
                myLog(it)
                stringBuilder.append(buildUIMsg(it))
                stringBuilder.append("\n")
            }
            var curMillis = System.currentTimeMillis()
            var uiRecordMilli = curMillis + ONE_SECOND
            val targetMilli = curMillis + FIVE_SECONDS
            while (curMillis < targetMilli) {
                ensureActive()
                val msg = "looping (from ${calledMsg})"
                myLog(msg)
                /* 1秒拼接一次UI展示字符串避免过长 */
                if (curMillis - uiRecordMilli >= ONE_SECOND) {
                    stringBuilder.append(buildUIMsg(msg))
                    stringBuilder.append("\n")
                    uiRecordMilli = curMillis
                }
                curMillis = System.currentTimeMillis()
            }
            "loop finished (from ${calledMsg})".let {
                myLog(it)
                stringBuilder.append(buildUIMsg(it))
            }
            runOnUiThread {
                updateMsgShow(stringBuilder.toString(), viewBinding.msgShowRv)
            }
        }
    }
}