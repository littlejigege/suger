package com.qgmobile.suger

import android.app.Notification
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
import com.google.gson.Gson
import com.mobile.utils.*
import com.mobile.utils.downloader.Downloader
import com.mobile.utils.permission.Permission
import com.mobile.utils.permission.PermissionCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.File
import java.io.IOException


class MainActivity : AlbumPickerActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        Thread.setDefaultUncaughtExceptionHandler { t, e -> e.printStackTrace() }


        Downloader.build {
            client = OkHttpClient()
            maxPieces = 30

        }.download("http://192.168.0.112:8080/photo/CloudMusic.zip","CloudMusic.zip")
//        val msg =     Downloader.DownloadMessage("ABC",123456,55, mutableListOf(),21312);
//       val json = Gson().toJson(msg);
//        Log.e("MainActivity",json)
//        val re = Gson().fromJson(json,Downloader.DownloadMessage::class.java)
//        Log.e("MainActivity"," ${re.donePices == null}")
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


