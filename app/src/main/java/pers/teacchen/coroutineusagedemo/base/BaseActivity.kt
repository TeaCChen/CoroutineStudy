package pers.teacchen.coroutineusagedemo.base

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

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

    private var _onClickListener: ((View) -> Unit)? = null
    protected val onClickListener
        get() = _onClickListener ?: { v: View ->
            onClick(v)
        }.also { _onClickListener = it }

    protected abstract fun inflateViewBinding(): VB
    protected open fun onClick(v: View) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _viewBinding = inflateViewBinding()
        setContentView(viewBinding.root)
    }
}