package com.mobile.utils.fileModifyHelper

import android.os.FileObserver
import java.io.File

/**
 * Created by steve on 17-11-23.
 */


abstract class FileObserver{
    private lateinit var fileObserver : FileObserver
    //    var observers = mutableMapOf<String,FileObserver>()
    private var observers = mutableSetOf<FileObserver>()

    fun startLoad(rootpath :String){

        File(rootpath).listFiles().forEach {
            if (it.isDirectory){
                startLoad(it.path)
            }
        }

        //actually , it can't detect MOVE_FROM, MOVE_TO i don't know why.
        // it'can't detected directory created as well
        // so this utils actually is not that useful.
        fileObserver = object : FileObserver(rootpath, FileObserver.CREATE or FileObserver.DELETE or
                FileObserver.MOVED_FROM or FileObserver.MOVED_TO or DELETE_SELF or MOVE_SELF){

            override fun onEvent(event: Int, path: String?) {
//                val curFile = File("$rootpath/$path")
//                when(event){
//
//                    FileObserver.CREATE or FileObserver.MOVED_FROM -> if(curFile.isDirectory){
//                        startLoad(curFile.path)
//                    }
//
//                    FileObserver.DELETE or FileObserver.MOVED_TO -> if(curFile.isDirectory){
//                        observers.get(curFile.path)?.stopWatching()
//                        observers.remove(curFile.path)
//                    }
//
//                    else -> Log.e("tag",event.toString())
//                }
                onDirctroyChange(rootpath)
            }
        }

//        observers.put(rootpath,fileObserver)
        observers.add(fileObserver)
        fileObserver.startWatching()
    }

    abstract fun onDirctroyChange(rootpath: String)
}
