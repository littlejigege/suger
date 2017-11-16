package com.mobile.utils.downloader

import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import com.google.gson.Gson
import com.mobile.utils.*
import okhttp3.*
import java.io.BufferedInputStream
import java.io.File
import java.io.IOException
import java.io.RandomAccessFile
import java.util.*
import java.util.concurrent.ConcurrentSkipListSet
import java.util.concurrent.atomic.AtomicInteger
import kotlin.collections.HashSet
import kotlin.properties.Delegates

/**
 * Created by steve on 17-11-15.
 */
class Downloader private constructor(builder: Builder) : Handler() {


    var client: OkHttpClient
    var pieceSize: Int
    var fileDir: String
    //配置文件
    lateinit var config: DownloadMessage

    //根据picecSize以及文件总长，计算出需要分出片段的数量
    private  var maxPieces: Int = 0
    private lateinit var url: String
    private lateinit var fileName: String
    //已完成的片段
    private var donePieces = Collections.synchronizedSet(HashSet<Int>())
    private var nextPiece = 0 ;


    companion object {
        fun build(buildFunction: Builder.() -> Unit) = Downloader(Builder(buildFunction))
    }

    init {
        client = builder.client
        pieceSize = builder.pieceSize
        fileDir = builder.fileDir
        File(fileDir).toggleDir()
    }

    fun download(url: String, fileName: String) {

        this.url = url
        this.fileName = fileName

        //创建最终文件
        val file = File(fileDir+"/"+fileName)
        if(file.exists()){
            // TODO 监听
            return
        }else{
            file.toggleFile()
        }

        val request = Request.Builder().url(url).build();

        //首次请求获取文件总长，用于后面的请求
        OkHttpClient().newCall(request).enqueue(object : okhttp3.Callback {
            val msg = Message()

            override fun onFailure(call: Call?, e: IOException?) {
                //TODO 监听
                msg.obj = -2
                sendMessage(msg)
            }

            override fun onResponse(call: Call?, response: Response?) {
                val length = response?.header("Content-Length")?.toLong()

                //读取/创建配置文件
                if (!File("$fileDir/$fileName.conf").exists()) {
                    //配置文件不存在
                    maxPieces = Math.ceil(length!!.toDouble() / pieceSize).toInt()
                    config = DownloadMessage(url, length, maxPieces, mutableListOf())
                    writeFileFromIS(File("$fileDir/$fileName.conf"), Gson().toJson(config).byteInputStream(), false)
                } else {
                    //配置文件存在
                    config = Gson().fromJson(readAll("$fileDir/$fileName.conf"), DownloadMessage::class.java)
                    maxPieces = config.maxPieces
                    donePieces.addAll(config.donePices)
                    nextPiece = config.donePices.size
                }

                msg.obj = length
                //开始下载的处理
                sendMessage(msg)
            }

        })
    }

    override fun handleMessage(msg: Message?) {
        Log.e("Downloader", msg.toString())
        when (msg?.obj.toString().toLong()) {

            -1L -> {
                Log.e("Downloader", "UnSupprt")
            }

            //片段下载完成，合并数据
            0L -> kotlin.run {

                while(donePieces.contains(nextPiece)){
                    val output = File(fileDir + "/" + fileName)

                    merge(output, true, File("$fileDir/${fileName + nextPiece}.tmp"))

                    //最后一个片段合并完成，删除配置文件
                    if(nextPiece == maxPieces-1){
                        Log.e("Downloader","done")
                        File("$fileDir/$fileName.conf").smartDelete()
                    }

                    nextPiece++;
                }

            }

            //传入总长拼命下载
            else -> kotlin.run {

                val length = msg?.obj.toString().toLong()
                var curlen = 0L

                //生成各个片段大小，分片断下载
                splitLen(length, maxPieces).forEachIndexed { index, piece ->

                    //下载完成未合并
                    if (donePieces.size == maxPieces) {
                        val m = Message()
                        m.obj == 0
                        sendMessage(m)
                        return
                    }

                    val req = Request.Builder()
                            .addHeader("Range", "bytes ${curlen}-${curlen + piece - 1}")
                            .url(url)
                            .build()
                    curlen += piece

                    //扔有未下载的片段
                    if (!donePieces.contains(index))
                        processRequest(req, index)

                }

            }

        }
    }

    /**
     * 处理下载的请求
     * @param req 请求
     * @pieceIndex 片段的位置
     */
    private fun processRequest(req: Request?, pieceIndex: Int) {

        //创建临时文件
        val file = File("$fileDir/${fileName + pieceIndex}.tmp")
        file.toggleFile()

        client.newCall(req).enqueue(object : okhttp3.Callback {

            override fun onFailure(call: Call?, e: IOException?) {

            }

            override fun onResponse(call: Call, response: Response) {

                val fis = response.body()?.byteStream()

                //读取数据写入临时文件
                writeFileFromIS(file, fis!!, false)

                //写入完成加入已完成的片段
                donePieces.add(pieceIndex)

                //修改配置文件
                config.donePices.add(pieceIndex)
                writeFileFromIS(File("$fileDir/$fileName.conf"),
                        Gson().toJson(config).byteInputStream(), false)

                //通知合并数据
                val msg = Message()
                msg.obj = 0 //merge
                sendMessage(msg)

            }

        })

    }


    private fun splitLen(length: Long, span: Int): LongArray {
        var len = length
        val piece = length / span
        val pieces = mutableListOf<Long>()
        for (i in 0 until span - 1) {
            len -= piece
            pieces.add(piece)
        }
        pieces.add(len)
        return pieces.toLongArray()
    }


    class Builder constructor(buildFunction: Builder.() -> Unit) {

        var client: OkHttpClient = OkHttpClient()
        var pieceSize = 1024 * 1024 * 20
        var maxPieces =  3
        var fileDir = Environment.getExternalStorageDirectory().path + "/download/test"

        init {
            this.buildFunction()
        }

    }

    /**
     * @param maxPieces 配置时允许最大分页数量
     * @param donePices 已完成的数量
     * @json 格式为JSON
     */
    data class DownloadMessage(val url: String,
                               val length: Long,
                               val maxPieces: Int,
                               var donePices: MutableList<Int> )


}