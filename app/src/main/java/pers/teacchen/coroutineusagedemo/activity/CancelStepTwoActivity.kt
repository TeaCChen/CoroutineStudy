package pers.teacchen.coroutineusagedemo.activity

import android.os.Bundle
import android.view.View
import kotlinx.coroutines.*
import pers.teacchen.coroutineusagedemo.R
import pers.teacchen.coroutineusagedemo.base.BaseActivity
import pers.teacchen.coroutineusagedemo.databinding.ActivityCancelStepTwoBinding
import pers.teacchen.coroutineusagedemo.helper.*

class CancelStepTwoActivity : BaseActivity<ActivityCancelStepTwoBinding>() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = getString(R.string.coroutine_cancel_step_two)
        onClickListener.attachToViews(
            viewBinding.launchBtn,
            viewBinding.cancelBtn
        )
        viewBinding.msgShowRv.initLogConfig(this, msgAdapter)
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    override fun onClick(v: View) {
        when (v) {
            viewBinding.launchBtn -> {
                "Clicked launchBtn".let {
                    myLog(it)
                    updateMsgShow(buildUIMsg(it), viewBinding.msgShowRv)
                }
                scope.launch(Dispatchers.IO) {
                    val stringBuilder = StringBuilder()
                    "Coroutine IO runs (from launchBtn)".let {
                        myLog(it)
                        stringBuilder.append(buildUIMsg(it))
                        stringBuilder.append("\n")
                    }
                    Thread.sleep(FIVE_SECONDS)
                    "Coroutine IO runs after thread sleep".let {
                        myLog(it)
                        stringBuilder.append(buildUIMsg(it))
                        stringBuilder.append("\n")
                    }
                    withContext(Dispatchers.Main) {
                        "withContext(Dispatchers.Main) lambda".let {
                            myLog(it)
                            stringBuilder.append(buildUIMsg(it))
                        }
                        updateMsgShow(stringBuilder.toString(), viewBinding.msgShowRv)
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