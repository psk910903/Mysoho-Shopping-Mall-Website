package com.study.ulidongneprojectapp

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Message
import android.provider.MediaStore
import android.util.Log
import android.view.ViewGroup
import android.webkit.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout


class MainActivity : AppCompatActivity(){

    lateinit var webView: WebView
    lateinit var webPopupView: WebView
    lateinit var containerView: ConstraintLayout
    private var mFilePathCallback: ValueCallback<Array<Uri?>?>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webView) // 웹뷰
        webPopupView = findViewById(R.id.webView) // 팝업창
        containerView = findViewById(R.id.containerView) // 웹뷰 + 팝업창

        val webSettings: WebSettings = webView.settings

        // 세팅 설정
        webSettings.javaScriptEnabled = true // 자바스크립트 사용 여부
        webSettings.setJavaScriptEnabled(true); // 자바스크립트 사용 여부
        webSettings.loadWithOverviewMode = true //메타태크 허용여부
        webSettings.domStorageEnabled = true //로컬저장소 허용여부
        webSettings.allowContentAccess = true
        webSettings.allowFileAccess = true // 파일 접근 허용 설정
        webSettings.loadWithOverviewMode = true //컨텐츠가 웹뷰보다 클 때, 스크린 크기에 맞추기
        webSettings.javaScriptCanOpenWindowsAutomatically = true
        webSettings.setSupportMultipleWindows(true); // 새창 띄우기 허용

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.mixedContentMode  = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW //https, http 호환 여부(https에서 http컨텐츠도 보여질수 있도록 함)
        }

        /*
        webSettings.javaScriptCanOpenWindowsAutomatically = true // window.open() 동작 허용
        webSettings.loadsImagesAutomatically = true // 웹뷰에서 앱에 등록되어있는 이미지 리소스를 사용해야 할 경우 자동으로 로드 여부
        webSettings.cacheMode = WebSettings.LOAD_NO_CACHE // LOAD_NO_CACHE -> 캐시 사용 x 네트워크로만 호출, LOAD_NORMAL -> 기본적인 모드로 캐시 사용, LOAD_DEFAULT  -> 평소엔 LOAD_NORAML 캐시가 만료된 경우 네트워크를 사용, LOAD_CACHE_ONLY -> 캐시만 사용, LOAD_CACHE_ELSE_NETWORK 캐시가 없을 경우 네트워크 사용
        webSettings.userAgentString = "app"// 웹에서 해당속성을 통해 앱으로 인지 하도록
        */

        webView.webViewClient = ViewClient()
        webView.webChromeClient = ChromeClient()

        webView.loadUrl("http://ourtwon.store/") // url 입력

    }

    override fun onBackPressed() {//뺵버튼 기능 누를 시 수행할 로직 구현


        if (webView == webPopupView){ // 팝업창이 아닐때, 백버튼 구현
            if(webView.canGoBack()){// 웹사이트에서 뒤로 갈 페이지가 존재 한다면
                webView.goBack() //웹사이트 뒤로가기
            }else{
                super.onBackPressed() //본래의 빽버튼 기능 수정(안드로이드)
            }
        }else{ // 팝업창일때
            // 백버튼을 누르면 창 닫게 만들기
            containerView.removeView(webPopupView)
            webPopupView.destroy()
            webPopupView = webView
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            2000 ->  //fileChooser 로 파일 선택 후 onActivityResult 에서 결과를 받아 처리함
                if (resultCode == RESULT_OK) {
                    //파일 선택 완료 했을 경우
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mFilePathCallback!!.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data))
                    } else {
                        mFilePathCallback!!.onReceiveValue(arrayOf(data?.data))
                    }
                    mFilePathCallback = null
                } else {
                    //cancel 했을 경우
                    if (mFilePathCallback != null) {
                        mFilePathCallback!!.onReceiveValue(null)
                        mFilePathCallback = null
                    }
                }
            else -> {}
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private inner class ViewClient : WebViewClient() {

        //인터넷경로 URL을 시작할때 호출됨.
        override fun shouldOverrideUrlLoading(
            view: WebView,
            request: WebResourceRequest): Boolean
        {
            val url = request.url.toString()

            if (url.startsWith("intent:")) { // 다른 앱을 실행해야 할 때
                try {
                    val intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
                    val existPackage =
                        packageManager.getLaunchIntentForPackage(intent.getPackage()!!)
                    if (existPackage != null) { // 앱이 설치되어있을 때 앱 실행
                        startActivity(intent)
                    } else { // 앱이 설지 되어있지 않을 때 구글 스토어로 이동
                        val marketIntent = Intent(Intent.ACTION_VIEW)
                        marketIntent.data = Uri.parse("market://details?id=" + intent.getPackage())
                        startActivity(marketIntent)
                    }
                    return true
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else { // 해당 앱의 url일 때
                view.loadUrl(url)
            }

            return super.shouldOverrideUrlLoading(view, request)
        }
    }

    private inner class ChromeClient() : WebChromeClient() {

        //JS로 얼럿창을 띄울때 호출됨.
        override fun onJsAlert(
            view: WebView?,
            url: String?,
            message: String?,
            result: JsResult
        ): Boolean {
            AlertDialog.Builder(view!!.context)
                .setMessage(message)
                .setPositiveButton(
                    android.R.string.ok
                ) { dialog, which -> result.confirm() }
                .setNegativeButton(
                    android.R.string.cancel
                ) { dialog, which -> result.cancel() }
                .create()
                .show()
            return true
        }

        // JS로 confirm창을 띄울때 호출됨.
        override fun onJsConfirm(
            view: WebView?,
            url: String?,
            message: String?,
            result: JsResult
        ): Boolean {
            AlertDialog.Builder(view!!.context)
                .setTitle("title")
                .setMessage(message)
                .setPositiveButton(
                    android.R.string.ok
                ) { dialog, which -> result.confirm() }
                .setNegativeButton(
                    android.R.string.cancel
                ) { dialog, which -> result.cancel() }
                .setCancelable(false)
                .create()
                .show()
            return true
        }

        // input file(파일 선택 버튼)을 눌렀을 때 호출됨.
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        override fun onShowFileChooser(
            webView: WebView?,
            filePathCallback: ValueCallback<Array<Uri?>?>,
            fileChooserParams: FileChooserParams?
        ): Boolean {

            if (mFilePathCallback != null) {
                mFilePathCallback!!.onReceiveValue(null)
                mFilePathCallback = null
            }
            mFilePathCallback = filePathCallback

            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            val REQUEST_CODE = 2000
            startActivityForResult(intent, REQUEST_CODE)

            return true
        }

        // JS로 window.open() 함수가 실행할 때 호출됨.
        @SuppressLint("SetJavaScriptEnabled")
        override fun onCreateWindow(view: WebView?, isDialog: Boolean, isUserGesture: Boolean, resultMsg: Message?): Boolean {
                webPopupView = WebView(this@MainActivity).apply {
                    webViewClient = WebViewClient()
                    // 팝업창의 세팅 설정
                    settings.javaScriptEnabled = true
                    settings.setJavaScriptEnabled(true); // 자바스크립트 사용 여부
                    settings.loadWithOverviewMode = true //메타태크 허용여부
                    settings.domStorageEnabled = true //로컬저장소 허용여부
                    settings.allowContentAccess = true
                    settings.allowFileAccess = true // 파일 접근 허용 설정
                    settings.loadWithOverviewMode = true //컨텐츠가 웹뷰보다 클 때, 스크린 크기에 맞추기
                    settings.javaScriptCanOpenWindowsAutomatically = true
                    settings.setSupportMultipleWindows(true);   // 새창 띄우기 허용
                }

                // 팝업창 크기 설정
                webPopupView.setLayoutParams(
                    ConstraintLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.MATCH_PARENT,
                        ConstraintLayout.LayoutParams.MATCH_PARENT
                    )
                )

                containerView.addView(webPopupView) // 팝업창 containerView에 add

                webPopupView.setWebChromeClient(object : WebChromeClient() {
                    // JS로 window.close() 함수가 실행할 때 호출됨.
                    override fun onCloseWindow(window: WebView) {
                        containerView.removeView(window)
                        window.destroy()
                        webPopupView = webView
                    }
                })

                webPopupView.setWebViewClient(object : WebViewClient() {

                    //팝업창이 인터넷경로 URL을 시작할때 호출됨.
                    override fun shouldOverrideUrlLoading(
                        view: WebView,
                        request: WebResourceRequest): Boolean
                    {
                        val url = request.url.toString()

                        // 구글 맵의 경우 구글 맵이 실행되지 않아서 따로 처리
                        if (url.startsWith("intent://maps.app.goo.gl")){
                            val intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
                            val existPackage =
                                packageManager.getLaunchIntentForPackage(intent.getPackage()!!)
                            startActivity(intent)
                            // 팝업창 닫기
                            containerView.removeView(webPopupView)
                            webPopupView.destroy()
                            webPopupView = webView
                            return true
                        }

                        // 다른 앱을 실행해야 할 때
                        if (url.startsWith("intent:")) {
                            try {
                                val intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
                                val existPackage =
                                    packageManager.getLaunchIntentForPackage(intent.getPackage()!!)
                                if (existPackage != null) { // 앱이 설치되어있을 때 앱 실행
                                    startActivity(intent)
                                } else { // 앱이 설지 되어있지 않을 때 구글 스토어로 이동
                                    val marketIntent = Intent(Intent.ACTION_VIEW)
                                    marketIntent.data = Uri.parse("market://details?id=" + intent.getPackage())
                                    startActivity(marketIntent)
                                }
                                // 팝업창 닫기
                                containerView.removeView(webPopupView)
                                webPopupView.destroy()
                                webPopupView = webView
                                return true
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        } else { // 해당 앱의 url일 때
                            view.loadUrl(url)
                        }

                        return super.shouldOverrideUrlLoading(view, request)
                    }
                })

                (resultMsg?.obj as WebView.WebViewTransport).webView = webPopupView
                resultMsg.sendToTarget()
                return true
        }
    }


}