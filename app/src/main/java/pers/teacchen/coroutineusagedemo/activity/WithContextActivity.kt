package pers.teacchen.coroutineusagedemo.activity

import android.os.Bundle
import android.view.View
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pers.teacchen.coroutineusagedemo.R
import pers.teacchen.coroutineusagedemo.base.BaseActivity
import pers.teacchen.coroutineusagedemo.databinding.ActivityWithContextBinding
import pers.teacchen.coroutineusagedemo.helper.attachToViews
import pers.teacchen.coroutineusagedemo.helper.buildUIMsg
import pers.teacchen.coroutineusagedemo.helper.initLogConfig
import pers.teacchen.coroutineusagedemo.helper.myLog

class WithContextActivity : BaseActivity<ActivityWithContextBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = getString(R.string.coroutine_with_context)
        onClickListener.attachToViews(viewBinding.ioToMainBtn, viewBinding.mainToIoBtn)
        viewBinding.msgShowRv.initLogConfig(this, msgAdapter)
    }

    override fun onClick(v: View) {
        when (v) {
            viewBinding.ioToMainBtn -> {
                GlobalScope.launch(Dispatchers.IO) {
                    val msgIO = "coroutine runs in IO"
                    myLog(msgIO)
                    val msgIOBuilt = buildUIMsg(msgIO)
                    val ret = withContext(Dispatchers.Main) {
                        val msgWithMain = "withContext in Main"
                        myLog(msgWithMain)
                        val msgWithMainBuild = buildUIMsg(msgWithMain)
                        val combineUIMsg = "${msgIOBuilt}\n${msgWithMainBuild}"
                        updateMsgShow(combineUIMsg, viewBinding.msgShowRv)
                        msgWithMain
                    }
                    myLog(ret)
                }
            }

            viewBinding.mainToIoBtn -> {
                GlobalScope.launch(Dispatchers.Main) {
                    val msgMain = "coroutine runs in Main"
                    myLog(msgMain)
                    val msgMainBuilt = buildUIMsg(msgMain)
                    val ret = withContext(Dispatchers.IO) {
                        val msgWithIO = "withContext in IO"
                        myLog(msgWithIO)
                        val msgWithIOBuild = buildUIMsg(msgWithIO)
                        val combineUIMsg = "${msgMainBuilt}\n${msgWithIOBuild}"
                        runOnUiThread {
                            updateMsgShow(combineUIMsg, viewBinding.msgShowRv)
                        }
                        msgWithIO
                    }
                    myLog(ret)
                }
            }
        }
    }
}