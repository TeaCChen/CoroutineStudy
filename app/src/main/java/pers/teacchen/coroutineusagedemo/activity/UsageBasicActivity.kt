package pers.teacchen.coroutineusagedemo.activity

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pers.teacchen.coroutineusagedemo.adapter.LogMsgAdapter
import pers.teacchen.coroutineusagedemo.base.BaseActivity
import pers.teacchen.coroutineusagedemo.databinding.ActivityUsageBasicBinding
import pers.teacchen.coroutineusagedemo.helper.buildUIMsg
import pers.teacchen.coroutineusagedemo.helper.myLog

class UsageBasicActivity : BaseActivity<ActivityUsageBasicBinding>() {

    override fun inflateViewBinding() = ActivityUsageBasicBinding.inflate(layoutInflater)

    private val msgAdapter = LogMsgAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding.launchIoBtn.setOnClickListener(onClickListener)
        viewBinding.launchMainBtn.setOnClickListener(onClickListener)
        viewBinding.msgShowRv.apply {
            layoutManager = LinearLayoutManager(this@UsageBasicActivity).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
            adapter = msgAdapter
        }
    }

    override fun onClick(v: View) {
        when (v) {
            viewBinding.launchIoBtn -> {
                GlobalScope.launch(Dispatchers.IO) {
                    val msg = "coroutine runs in IO"
                    val uiMsg = buildUIMsg(msg)
                    myLog(msg)
                    runOnUiThread {
                        msgAdapter.addMsg(uiMsg)
                        viewBinding.msgShowRv.smoothScrollToPosition(msgAdapter.itemCount - 1)
                    }
                }
            }

            viewBinding.launchMainBtn -> {
                GlobalScope.launch(Dispatchers.Main) {
                    val msg = "coroutine runs in Main"
                    val uiMsg = buildUIMsg(msg)
                    myLog(msg)
                    msgAdapter.addMsg(uiMsg)
                    viewBinding.msgShowRv.smoothScrollToPosition(msgAdapter.itemCount - 1)
                }
            }
        }
    }
}