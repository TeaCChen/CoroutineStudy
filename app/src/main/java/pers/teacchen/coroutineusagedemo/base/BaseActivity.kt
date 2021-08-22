package pers.teacchen.coroutineusagedemo.base

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import pers.teacchen.coroutineusagedemo.adapter.LogMsgAdapter
import pers.teacchen.coroutineusagedemo.databinding.ViewMsgShowRvBinding
import pers.teacchen.coroutineusagedemo.helper.inflateBinding

/**
 * @FileName: BaseActivity
 * @description:
 *
 * @author TeaC
 * @date   2021-08-20
 */
abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {
    private var _viewBinding: VB? = null
    protected val viewBinding
        get() = _viewBinding ?: throw IllegalStateException("ViewBinding not init yet!")

    /* In base, not elegant! Just for demo to reduce code! */
    private var _msgAdapter: LogMsgAdapter? = null
    protected val msgAdapter
        get() = _msgAdapter ?: LogMsgAdapter().also { _msgAdapter = it }

    private var _onClickListener: ((View) -> Unit)? = null
    protected val onClickListener
        get() = _onClickListener ?: { v: View ->
            onClick(v)
        }.also { _onClickListener = it }

    protected open fun onClick(v: View) {}

    protected fun updateMsgShow(uiMsg: String, rv: RecyclerView) {
        msgAdapter.addMsg(uiMsg)
        rv.smoothScrollToPosition(msgAdapter.itemCount - 1)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _viewBinding = inflateBinding(this, layoutInflater)
        setContentView(viewBinding.root)
    }
}