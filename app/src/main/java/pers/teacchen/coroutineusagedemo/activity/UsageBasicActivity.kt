package pers.teacchen.coroutineusagedemo.activity

import android.os.Bundle
import android.view.View
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pers.teacchen.coroutineusagedemo.R
import pers.teacchen.coroutineusagedemo.base.BaseActivity
import pers.teacchen.coroutineusagedemo.databinding.ActivityUsageBasicBinding
import pers.teacchen.coroutineusagedemo.helper.attachToViews
import pers.teacchen.coroutineusagedemo.helper.buildUIMsg
import pers.teacchen.coroutineusagedemo.helper.initLogConfig
import pers.teacchen.coroutineusagedemo.helper.myLog

class UsageBasicActivity : BaseActivity<ActivityUsageBasicBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = getString(R.string.coroutine_launch)
        onClickListener.attachToViews(viewBinding.launchIoBtn, viewBinding.launchMainBtn)
        viewBinding.msgShowRv.initLogConfig(this, msgAdapter)
    }

    override fun onClick(v: View) {
        when (v) {
            viewBinding.launchIoBtn -> {
                GlobalScope.launch(Dispatchers.IO) {
                    val msg = "coroutine runs in IO"
                    myLog(msg)
                    val uiMsg = buildUIMsg(msg)
                    runOnUiThread {
                        updateMsgShow(uiMsg, viewBinding.msgShowRv)
                    }
                }
            }

            viewBinding.launchMainBtn -> {
                GlobalScope.launch(Dispatchers.Main) {
                    val msg = "coroutine runs in Main"
                    myLog(msg)
                    val uiMsg = buildUIMsg(msg)
                    updateMsgShow(uiMsg, viewBinding.msgShowRv)
                }
            }
        }
    }
}