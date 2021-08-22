package pers.teacchen.coroutineusagedemo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pers.teacchen.coroutineusagedemo.databinding.ViewItemLogMsgBinding
import pers.teacchen.coroutineusagedemo.helper.myLog

/**
 * @FileName: LogMsgAdapter
 * @description:
 *
 * @author TeaC
 * @date   2021-08-20
 */
class LogMsgAdapter : RecyclerView.Adapter<LogMsgAdapter.ViewHolder>() {

    private val msgList = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ViewItemLogMsgBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = msgList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.msgTv.text = msgList[position]
    }

    fun addMsg(msg: String) {
        msgList.add(msg)
        notifyItemInserted(msgList.size - 1)
    }

    class ViewHolder(
        val binding: ViewItemLogMsgBinding
    ) : RecyclerView.ViewHolder(binding.root)

}