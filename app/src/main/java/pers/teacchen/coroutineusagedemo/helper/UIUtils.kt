package pers.teacchen.coroutineusagedemo.helper

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import pers.teacchen.coroutineusagedemo.adapter.LogMsgAdapter
import java.lang.reflect.ParameterizedType

/**
 * @FileName: UIUtils
 * @description:
 *
 * @author TeaC
 * @date   2021-08-20
 */

fun ((View) -> Unit).attachToViews(
    vararg views: View
) {
    views.forEach {
        it.setOnClickListener(this)
    }
}

fun <T : AppCompatActivity> Activity.startTo(clazz: Class<T>) {
    startActivity(Intent(this, clazz))
}

fun RecyclerView.initLogConfig(context: Context, msgAdapter: LogMsgAdapter) {
    layoutManager = LinearLayoutManager(context).apply {
        orientation = LinearLayoutManager.VERTICAL
    }
    adapter = msgAdapter
}

/**
 *  与具体实现方式耦合，有较大局限性
 */
@Suppress("UNCHECKED_CAST")
internal fun <VB : ViewBinding> inflateBinding(
    contractHost: Any,
    inflater: LayoutInflater
): VB {
    val type = contractHost.javaClass.genericSuperclass as ParameterizedType
    val clazz = type.actualTypeArguments[0] as Class<VB>
    val method = clazz.getMethod("inflate", LayoutInflater::class.java)
    return method.invoke(null, inflater) as VB
}

