# Android Annotation 利用註解也可以MD5

##### 常常要寫一堆相同程式碼，其實可以把它抽取出來利用註解(Annotation)的方式，好看也好用，連MD5也可以運行



---

#### 文章目錄
<ol>
    <li><a href="#a">創建Annotation</a></li>
    <li><a href="#b">創建Annotation實現的方法</a></li>
    <li><a href="#c">添加MD5工具類</a></li>
	<li><a href="#d">使用Annotation的Model</a></li>
	<li><a href="#e">程式碼範例</a></li>
	<li><a href="#f">效果展示</a></li>
	<li><a href="#g">Github</a></li>
</ol>

---

<a id="a"></a>
#### 1.創建Annotation
##### SerializedEncryption
```Kotlin
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class SerializedEncryption(
    val type: String = "Base64"
)
```

##### SerializedTrim
```Kotlin
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class SerializedTrim
```

<a id="b"></a>
#### 2.創建Annotation實現的方法
```Kotlin
object SerializedUtil {

    fun convertToRequestContent(model: Any?, field: Field): Array<Any>? {
        val key = field.name
        val originalValue = field[model]

        if (TextUtils.isEmpty(key) || originalValue == null) {
            return null
        }

        return if (originalValue is File) {
            arrayOf(key, originalValue)
        } else {
            var value = originalValue.toString()

            val serializedTrim = field.getAnnotation(SerializedTrim::class.java)
            if (null != serializedTrim) {
                value = value.replace("\\s*".toRegex(), "")
            }

            val serializedEncryption = field.getAnnotation(SerializedEncryption::class.java)
            if (null != serializedEncryption) {
                value = when (serializedEncryption.type) {
                    "MD5" -> {
                        MDUtil.encode(MDUtil.Type.MD5, value).uppercase(Locale.getDefault())
                    }
                    "SHA256" -> {
                        MDUtil.encode(MDUtil.Type.SHA256, value).uppercase(Locale.getDefault())
                    }
                    "SHA512" -> {
                        MDUtil.encode(MDUtil.Type.SHA512, value).uppercase(Locale.getDefault())
                    }
                    else -> {
                        Base64.encodeToString(value.toByteArray(), Base64.DEFAULT)
                    }
                }
            }

            arrayOf(key, value)
        }
    }
}
```

<a id="c"></a>
#### 3.添加MD5工具類
```Kotlin
object MDUtil {
    fun encode(type: Type, data: String): String {
        return try {
            val digest = MessageDigest.getInstance(type.value)
            digest.update(data.toByteArray())
            //32位補0
            String.format("%032X", BigInteger(1, digest.digest()))
        } catch (e: Exception) {
            data
        }
    }

    enum class Type(val value: String) {
        MD5("MD5"), SHA256("SHA-256"), SHA512("SHA-512");
    }
}
```

<a id="d"></a>
#### 4.使用Annotation的Model
```kotlin
data class Data(
    @SerializedTrim @SerializedEncryption(type = "SHA256")
    val sha256: String,
    @SerializedTrim @SerializedEncryption(type = "MD5")
    val md5: String,
    @SerializedTrim @SerializedEncryption
    val base64: String
)
```

<a id="e"></a>
#### 5.程式碼範例
```Kotlin
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
```

<a id="f"></a>
#### 6.效果展示
<a href="https://badgameshow.com/fly/wp-content/uploads/2021/12/螢幕擷取畫面-2021-12-21-133527.png"><img src="https://badgameshow.com/fly/wp-content/uploads/2021/12/螢幕擷取畫面-2021-12-21-133527.png" width="100%"/></a>

<a id="g"></a>
#### 7.Github
[Android Annotation 利用註解也可以MD5 Github](https://github.com/MuHongWeiWei/AnnotationDemo)
