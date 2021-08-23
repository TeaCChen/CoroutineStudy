package pers.teacchen.coroutineusagedemo.activity

import android.os.Bundle
import android.view.View
import kotlinx.coroutines.*
import pers.teacchen.coroutineusagedemo.R
import pers.teacchen.coroutineusagedemo.base.BaseActivity
import pers.teacchen.coroutineusagedemo.databinding.ActivityStructuredStepOneBinding
import pers.teacchen.coroutineusagedemo.helper.*

class StructuredStepOneActivity : BaseActivity<ActivityStructuredStepOneBinding>() {

    private val scope = MainScope()
    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = getString(R.string.coroutine_structured_step_one)
        onClickListener.attachToViews(
            viewBinding.launchStructuredBtn,
            viewBinding.supervisorScopeBtn,
            viewBinding.joinBtn,
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
            viewBinding.launchStructuredBtn -> launchStructuredBtnClicked()
            viewBinding.supervisorScopeBtn -> supervisorScopeBtnClicked()
            viewBinding.joinBtn -> joinBtnClicked()
            viewBinding.cancelBtn -> cancelBtnClicked()
        }
    }

    private fun launchStructuredBtnClicked() {
        "launchStructuredBtnClicked".let {
            myLog(it)
            updateMsgShow(buildUIMsg(it), viewBinding.msgShowRv)
        }
        job?.cancel()
        job = scope.launch {
            "Coroutine Main runs (launchStructuredBtnClicked)".let {
                myLog(it)
                updateMsgShow(buildUIMsg(it), viewBinding.msgShowRv)
            }
            launch(Dispatchers.IO) {
                testIOCoroutine("launchStructuredBtnClicked A")
            }
            launch(Dispatchers.IO) {
                testIOCoroutine("launchStructuredBtnClicked B")
            }
            launch(Dispatchers.IO) {
                testIOCoroutine("launchStructuredBtnClicked C")
            }
            "Coroutine Main runs final statement (launchStructuredBtnClicked)".let {
                myLog(it)
                updateMsgShow(buildUIMsg(it), viewBinding.msgShowRv)
            }
        }
    }

    private fun supervisorScopeBtnClicked() {
        "supervisorScopeBtnClicked".let {
            myLog(it)
            updateMsgShow(buildUIMsg(it), viewBinding.msgShowRv)
        }
        job?.cancel()
        job = scope.launch {
            "Coroutine Main runs (supervisorScopeBtnClicked)".let {
                myLog(it)
                updateMsgShow(buildUIMsg(it), viewBinding.msgShowRv)
            }
            supervisorScope {
                "supervisorScope lambda runs (supervisorScopeBtnClicked)".let {
                    myLog(it)
                    updateMsgShow(buildUIMsg(it), viewBinding.msgShowRv)
                }
                launch(Dispatchers.IO) {
                    testIOCoroutine("supervisorScopeBtnClicked A")
                }
                launch(Dispatchers.IO) {
                    testIOCoroutine("supervisorScopeBtnClicked B")
                }
                launch(Dispatchers.IO) {
                    testIOCoroutine("supervisorScopeBtnClicked C")
                }
                "supervisorScope lambda runs final statement (supervisorScopeBtnClicked)".let {
                    myLog(it)
                    updateMsgShow(buildUIMsg(it), viewBinding.msgShowRv)
                }
            }
            "Coroutine Main runs final statement (supervisorScopeBtnClicked)".let {
                myLog(it)
                updateMsgShow(buildUIMsg(it), viewBinding.msgShowRv)
            }
        }
    }

    private fun joinBtnClicked() {
        "joinBtnClicked".let {
            myLog(it)
            updateMsgShow(buildUIMsg(it), viewBinding.msgShowRv)
        }
        scope.launch(Dispatchers.Main) {
            "Coroutine Main runs (joinBtnClicked)".let {
                myLog(it)
                updateMsgShow(buildUIMsg(it), viewBinding.msgShowRv)
            }
            val jobNonNull = job ?: return@launch
            jobNonNull.join()
            "Coroutine Main runs after join() (joinBtnClicked)".let {
                myLog(it)
                updateMsgShow(buildUIMsg(it), viewBinding.msgShowRv)
            }
        }
    }

    private fun cancelBtnClicked() {
        "cancelBtnClicked".let {
            myLog(it)
            updateMsgShow(buildUIMsg(it), viewBinding.msgShowRv)
        }
        scope.coroutineContext.cancelChildren()
    }

    private suspend fun testIOCoroutine(calledMsg: String) {
        val stringBuilder = StringBuilder()
        "Coroutine IO runs ($calledMsg)".let {
            myLog(it)
            stringBuilder.append(buildUIMsg(it))
            stringBuilder.append("\n")
        }
        /* 仅为样例代码，休眠线程其实是非常非常不建议的做法！！ */
        Thread.sleep(randomMilli)
        "Coroutine IO runs after thread sleep ($calledMsg)".let {
            myLog(it)
            stringBuilder.append(buildUIMsg(it))
            stringBuilder.append("\n")
        }
        withContext(Dispatchers.Main) {
            updateMsgShow(stringBuilder.toString(), viewBinding.msgShowRv)
        }
    }
}