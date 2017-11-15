package com.qgmobile.suger

import android.os.*
import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.mobile.utils.*
import com.mobile.utils.permission.Permission
import com.mobile.utils.permission.PermissionCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AlbumPickerActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var datas : MutableList<User> = mutableListOf()
        for(i in 1..8){
            datas.add(User((('A'+i)).toString(),i))
        }


        var old = mutableListOf<User>();

        var t = 9;
        val adapter = MyAdapter(datas)
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(this)

        button.setOnClickListener {
            datas.add(User("E"+t++.toString(),t))
            adapter.easyNotify { user, user2 -> user.mid == user2.mid }
        }

        button2.setOnClickListener{
            datas.removeAt((Math.random()*datas.size).toInt())
            adapter.easyNotify{user, u2 -> user.mid == u2.mid}
        }
    }



}

class User(  val name : String,
               val mid : Int){

}
class MyAdapter(val datas : MutableList<User>) : AutoNotifyAdapter<User,MyAdapter.MyHolder>(datas) {

    override fun onBindViewHolder(holder: MyHolder?, position: Int) {
        holder?.tv?.text = datas[position].name
    }


    override fun getItemCount() = datas.size


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyHolder{
        val view =  LayoutInflater.from(parent!!.context).inflate(R.layout.item,parent,false)
        val holder = MyHolder(view)
        return holder
    }

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var tv : TextView

        init {
            tv = itemView.findViewById<TextView>(R.id.tv)
        }


    }

}


