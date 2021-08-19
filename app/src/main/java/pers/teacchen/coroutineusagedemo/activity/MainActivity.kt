package pers.teacchen.coroutineusagedemo.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import pers.teacchen.coroutineusagedemo.base.BaseActivity
import pers.teacchen.coroutineusagedemo.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun inflateViewBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding.baseBtn.setOnClickListener(onClickListener)
    }

    override fun onClick(v: View) {
        when (v) {
            viewBinding.baseBtn -> {
                val intent = Intent(this, UsageBasicActivity::class.java)
                startActivity(intent)
            }
        }
    }
}