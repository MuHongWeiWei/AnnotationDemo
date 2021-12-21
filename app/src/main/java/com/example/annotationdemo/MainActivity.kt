package com.example.annotationdemo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.annotationdemo.util.SerializedUtil
import java.lang.reflect.Field

/**
 * Author: FlyWei
 * E-mail: tony91097@gmail.com
 * Date: 2021/12/21
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val data = Data("demo", "demo", "demo")

        //取得所有屬性
        val fields = data.javaClass.declaredFields
        Field.setAccessible(fields, true)

        for (field in fields) {
            val result = SerializedUtil.convertToRequestContent(data, field)
            result?.apply {
                Log.e("RESULT", get(0).toString() + " : " + get(1).toString())
            }
        }
    }
}