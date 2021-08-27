package pers.teacchen.coroutineusagedemo.activity

import android.os.Bundle
import android.view.View
import kotlinx.coroutines.*
import pers.teacchen.coroutineusagedemo.R
import pers.teacchen.coroutineusagedemo.base.BaseActivity
import pers.teacchen.coroutineusagedemo.databinding.ActivitySupervisorBinding
import pers.teacchen.coroutineusagedemo.helper.*

class SupervisorActivity : BaseActivity<ActivitySupervisorBinding>() {

    private val jobScope = CoroutineScope(Job() + Dispatchers.Main)
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        myLog("CoroutineExceptionHandler:$throwable")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = getString(R.string.coroutine_exception_supervisor)
        onClickListener.attachToViews(
            viewBinding.launchWithJob,
            viewBinding.launchWithSupervisorJob,
            viewBinding.suspendWithJob,
            viewBinding.suspendWithSupervisorJob,
            viewBinding.cancelBtn
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        jobScope.cancel()
        scope.cancel()
    }

    override fun onClick(v: View) {
        when (v) {
            viewBinding.launchWithJob -> launchWithJobClicked()
            viewBinding.launchWithSupervisorJob -> launchWithSupervisorJobClicked()
            viewBinding.suspendWithJob -> suspendWithJobClicked()
            viewBinding.suspendWithSupervisorJob -> suspendWithSupervisorJobClicked()
            viewBinding.cancelBtn -> cancelBtnClicked()
        }
    }

    private fun launchWithJobClicked() {
        myLog("launchWithJobClicked")
        launchTenSecondsCoroutine(jobScope, "launchWithJobClicked A")
        launchTenSecondsCoroutine(jobScope, "launchWithJobClicked B")
        launchCoroutineThrowException(jobScope, "launchWithJobClicked")
    }

    private fun launchWithSupervisorJobClicked() {
        launchTenSecondsCoroutine(scope, "launchWithSupervisorJobClicked A")
        launchTenSecondsCoroutine(scope, "launchWithSupervisorJobClicked B")
        launchCoroutineThrowException(scope, "launchWithSupervisorJobClicked")
    }

    private fun suspendWithJobClicked() {
        myLog("suspendWithJobClicked")
        scope.launch(exceptionHandler) {
            myLog("suspendWithJobClicked parent coroutine")
            val ret = coroutineScope {
                launchTenSecondsCoroutine(this, "suspendWithJobClicked A")
                launchTenSecondsCoroutine(this, "suspendWithJobClicked B")
                launchCoroutineThrowException(this, "suspendWithJobClicked")
                "coroutineScope final line"
            }
            myLog("suspendWithJobClicked parent coroutine final line: $ret")
        }
    }

    private fun suspendWithSupervisorJobClicked() {
        myLog("suspendWithSupervisorJobClicked")
        scope.launch {
            myLog("suspendWithSupervisorJobClicked parent coroutine")
            val ret = supervisorScope {
                launchTenSecondsCoroutine(this, "suspendWithSupervisorJobClicked A")
                launchTenSecondsCoroutine(this, "suspendWithSupervisorJobClicked B")
                launchCoroutineThrowException(this, "suspendWithSupervisorJobClicked")
                "supervisorScope final line"
            }
            myLog("suspendWithSupervisorJobClicked parent coroutine final line: $ret")
        }
    }

    private fun cancelBtnClicked() {
        myLog("cancelBtnClicked")
        jobScope.coroutineContext.cancelChildren()
        scope.coroutineContext.cancelChildren()
    }

    private fun launchTenSecondsCoroutine(scope: CoroutineScope, extraMsg: String) {
        scope.launch(Dispatchers.IO) {
            val targetMilli = System.currentTimeMillis() + TEN_SECONDS
            while (true) {
                ensureActive()
                if (System.currentTimeMillis() > targetMilli) {
                    break
                }
                myLog("launchLoopingCoroutine $extraMsg")
                Thread.sleep(ONE_SECOND)
            }
        }
    }

    private fun launchCoroutineThrowException(scope: CoroutineScope, extraMsg: String) {
        scope.launch(exceptionHandler + Dispatchers.IO) {
            Thread.sleep(FIVE_SECONDS)
            throw IllegalStateException("$extraMsg Throw exception!")
        }
    }
}