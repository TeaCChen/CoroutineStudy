package pers.teacchen.coroutineusagedemo.activity

import android.os.Bundle
import android.view.View
import kotlinx.coroutines.*
import pers.teacchen.coroutineusagedemo.R
import pers.teacchen.coroutineusagedemo.base.BaseActivity
import pers.teacchen.coroutineusagedemo.databinding.ActivityCoroutineExceptionBinding
import pers.teacchen.coroutineusagedemo.helper.*

class CoroutineExceptionActivity : BaseActivity<ActivityCoroutineExceptionBinding>() {

    private val scope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = getString(R.string.coroutine_exception_step_one)
        onClickListener.attachToViews(
            viewBinding.launchThrowException,
            viewBinding.launchWithTryCatch,
            viewBinding.launchWithExceptionHandler,
            viewBinding.cancelBtn
        )


        viewBinding.msgShowRv.initLogConfig(this, msgAdapter)
    }

    override fun onClick(v: View) {
        when (v) {
            viewBinding.launchThrowException -> launchThrowExceptionClicked()
            viewBinding.launchWithTryCatch -> launchWithTryCatchClicked()
            viewBinding.launchWithExceptionHandler -> launchWithExceptionHandlerClicked()
            viewBinding.cancelBtn -> cancelBtnClicked()
        }
    }

    private fun launchThrowExceptionClicked() {
        "launchThrowExceptionClicked".let {
            myLog(it)
            updateMsgShow(buildUIMsg(it), viewBinding.msgShowRv)
        }
        scope.launch(Dispatchers.IO) {
            val stringBuilder = StringBuilder()
            "Coroutine IO runs (launchThrowExceptionClicked)".let {
                myLog(it)
                stringBuilder.append(buildUIMsg(it))
                stringBuilder.append("\n")
            }
            someAPIMayThrowException(this)
            "Coroutine IO runs after thread sleep (launchThrowExceptionClicked)".let {
                myLog(it)
                stringBuilder.append(buildUIMsg(it))
                stringBuilder.append("\n")
            }
            scope.launch {
                updateMsgShow(stringBuilder.toString(), viewBinding.msgShowRv)
            }
        }
    }

    private fun launchWithTryCatchClicked() {
        "launchWithTryCatchClicked".let {
            myLog(it)
            updateMsgShow(buildUIMsg(it), viewBinding.msgShowRv)
        }
        scope.launch(Dispatchers.IO) {
            val stringBuilder = StringBuilder()
            "Coroutine IO runs (launchWithTryCatchClicked)".let {
                myLog(it)
                stringBuilder.append(buildUIMsg(it))
                stringBuilder.append("\n")
            }
            try {
                someAPIMayThrowException(this)
            } catch (e: IllegalStateException) {
                myLog("catch IllegalStateException:$e")
            }
            "Coroutine IO runs after thread sleep (launchWithTryCatchClicked)".let {
                myLog(it)
                stringBuilder.append(buildUIMsg(it))
                stringBuilder.append("\n")
            }
            scope.launch {
                updateMsgShow(stringBuilder.toString(), viewBinding.msgShowRv)
            }
        }
    }

    private fun launchWithExceptionHandlerClicked() {
        "launchWithExceptionHandlerClicked".let {
            myLog(it)
            updateMsgShow(buildUIMsg(it), viewBinding.msgShowRv)
        }
        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            myLog("exceptionHandler throwable:$throwable")
        }

        scope.launch(Dispatchers.IO + exceptionHandler) {
            val stringBuilder = StringBuilder()
            "Coroutine IO runs (launchWithExceptionHandlerClicked)".let {
                myLog(it)
                stringBuilder.append(buildUIMsg(it))
                stringBuilder.append("\n")
            }
            someAPIMayThrowException(this)
            "Coroutine IO runs after thread sleep (launchWithExceptionHandlerClicked)".let {
                myLog(it)
                stringBuilder.append(buildUIMsg(it))
                stringBuilder.append("\n")
            }
            scope.launch {
                updateMsgShow(stringBuilder.toString(), viewBinding.msgShowRv)
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

    private fun someAPIMayThrowException(scope: CoroutineScope) {
        val randomTime = randomMilli
        Thread.sleep(randomTime)
        scope.ensureActive()
        if (randomTime < TEN_SECONDS) {
            throw IllegalStateException("Throw Exception by code")
        }
    }
}