package pers.teacchen.coroutineusagedemo.activity

import android.os.Bundle
import android.view.View
import pers.teacchen.coroutineusagedemo.base.BaseActivity
import pers.teacchen.coroutineusagedemo.databinding.ActivityMainBinding
import pers.teacchen.coroutineusagedemo.helper.attachToViews
import pers.teacchen.coroutineusagedemo.helper.startTo

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onClickListener.attachToViews(
            viewBinding.baseBtn,
            viewBinding.withContextBtn
        )
    }

    override fun onClick(v: View) {
        when (v) {
            viewBinding.baseBtn -> {
                startTo(UsageBasicActivity::class.java)
            }
            viewBinding.withContextBtn -> {
                startTo(WithContextActivity::class.java)
            }
        }
    }
}
