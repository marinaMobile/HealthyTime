package com.AgainstGravity.RecRoo.bp

import android.content.Context
import com.AgainstGravity.RecRoo.bp.Constant.C11
import com.AgainstGravity.RecRoo.bp.Constant.DL1
import com.orhanobut.hawk.Hawk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

class Soup(val context: Context) {

    private var jsoup: String? = "null"
    private val hawk : String? = Hawk.get(C11,"null")
    private val hawkTheII : String? = Hawk.get(DL1, "null")
    private var forJsoupSet: String = Constant.mainU + Constant.carryMe + hawk + "&" + Constant.carryMeTwice + hawkTheII

    suspend fun getDocSecretKey(): String?{
        withContext(Dispatchers.IO){
            val doc = Jsoup.connect(forJsoupSet).get()
            jsoup = doc.text().toString()
        }
        return jsoup
    }
}
