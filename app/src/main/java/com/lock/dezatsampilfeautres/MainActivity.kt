package com.lock.dezatsampilfeautres

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.drawable.shapes.Shape
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.lock.dezatsampilfeautres.ui.theme.DezAtSampilFeautresTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DezAtSampilFeautresTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val context = LocalContext.current
                    val kisko = remember { mutableStateOf(false) }
                    Column {
                        val deviceManager = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
                        val compName = ComponentName(context, AdminService::class.java)
                        GeneralSettingItem(
                            icon = R.drawable.icon_home,
                            mainText = "Admin ",
                            subText = "give the app required permission ",
                            onClick = {
                                if (!deviceManager.isDeviceOwnerApp(context.getPackageName())){
                                    val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
                                    intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName)
                                    intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "You should enable the app!")
                                    startActivityForResult(intent, 1)
                                }else{
                                    Toast.makeText(context,"the admin permission is add ", Toast.LENGTH_LONG).show()
                                }
                            }
                        )
                        GeneralSettingItem(
                            icon = R.drawable.icon_home,
                            mainText = "hold mode  ",
                            subText = " hold the app in the view  ",
                            onClick = {
                               if (kisko.value == false){
                                   startLockTask()
                                   kisko.value = true
                               }else{
                                   stopLockTask()
                                   kisko.value = false
                               }

                            }
                        )
                        GeneralSettingItem(
                            icon = R.drawable.icon_home,
                            mainText = "3d panorama ",
                            subText = "   ",
                            onClick = {
                                val sceneViewerIntent = Intent(Intent.ACTION_VIEW)
                                val link: String =
                                    "https://firebasestorage.googleapis.com/v0/b/houseestimatorandmodeler.appspot.com/o/Armodels%2FWolves.glb?alt=media&token=ef5b90c0-8c04-4608-8888-a37547adbf84"

                                sceneViewerIntent.data =
                                    Uri.parse("https://arvr.google.com/scene-viewer/1.0").buildUpon()
                                        .appendQueryParameter(
                                            "file",
                                            link
                                        ).appendQueryParameter("title", "wolves").build()
                                sceneViewerIntent.setPackage("com.google.android.googlequicksearchbox")
                                context.startActivity(sceneViewerIntent)
//                                sceneViewerIntent.data =
//                                    Uri.parse("https://arvr.google.com/scene-viewer/1.0").buildUpon()
//                                        .appendQueryParameter("file", link)
//                                        .appendQueryParameter("mode", "ar_only")
//                                        .appendQueryParameter("title", "wolf")
//                                        .appendQueryParameter("resizable", "true")
//                                        .build()
//
//                                sceneViewerIntent.setPackage("com.google.ar.core")
                                context.startActivity(sceneViewerIntent)

                            }
                        )
                        Card(modifier = Modifier
                            .fillMaxWidth()
                            .height(350.dp)) {
                            val mUrl = "https://three60-image-viewer.onrender.com/"

                            // Adding a WebView inside AndroidView
                            // with layout as full screen
                            AndroidView(factory = {
                                WebView(it).apply {
                                    layoutParams = ViewGroup.LayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.MATCH_PARENT
                                    )
                                    webViewClient = WebViewClient()
                                    settings.javaScriptEnabled =true
                                    settings.useWideViewPort =true
                                    settings.javaScriptCanOpenWindowsAutomatically= true

                                    addJavascriptInterface(WebAppInterface(),"Android")
                                    loadUrl(mUrl)

                                }
                            }, update = {
                                it.loadUrl(mUrl)
                            })


                        }
                    }
                }
            }
        }
    }
}

 class WebAppInterface() {
    @JavascriptInterface
    fun getImagePathFromIslam() : String{
        return  "https://www.360cities.net/en/image/covolo-di-lusiana-parete-del-soio#274.89,12.90,70.0"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneralSettingItem(icon: Int, mainText: String, subText: String, onClick: () -> Unit) {
    Card(
        onClick = { onClick() },
        modifier = Modifier
            .padding(bottom = 8.dp)
            .fillMaxWidth()
            .background(Color.White),
    )
    {
        Row(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(34.dp)

                ) {
                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = "",
                        tint = Color.Unspecified,
                        modifier = Modifier.padding(8.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))
                Column(
                    modifier = Modifier.offset(y = (2).dp)
                ) {
                    Text(
                        text = mainText,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                    )

                    Text(
                        text = subText,
                        color = Color.Gray,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.offset(y = (-4).dp)
                    )
                }

            }


        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)


@Composable
fun ModelsThreeD(){
    val sceneViewerIntent = Intent(Intent.ACTION_VIEW)
    val context = LocalContext.current
    Text(text = "open 3 d Model", Modifier.clickable {
         val link: String =
            "https://firebasestorage.googleapis.com/v0/b/houseestimatorandmodeler.appspot.com/o/Armodels%2FWolves.glb?alt=media&token=ef5b90c0-8c04-4608-8888-a37547adbf84"

        sceneViewerIntent.data =
            Uri.parse("https://arvr.google.com/scene-viewer/1.0").buildUpon()
                .appendQueryParameter(
                    "file",
                    link
                ).appendQueryParameter("title", "wolves").build()
        sceneViewerIntent.setPackage("com.google.android.googlequicksearchbox")
        context.startActivity(sceneViewerIntent)
//        sceneViewerIntent.data =
//            Uri.parse("https://arvr.google.com/scene-viewer/1.0").buildUpon()
//                .appendQueryParameter("file", link)
//                .appendQueryParameter("mode", "ar_only")
//                .appendQueryParameter("title", "wolf")
//                .appendQueryParameter("resizable", "true")
//                .build()
//
//        sceneViewerIntent.setPackage("com.google.ar.core")
//            context.startActivity(sceneViewerIntent)

    })
}


@Composable
fun MyBanorama() {
    val context = LocalContext.current
    // Declare a string that contains a url
    val mUrl = "https://three60-image-viewer.onrender.com/"

    // Adding a WebView inside AndroidView
    // with layout as full screen
    AndroidView(factory = {
        WebView(it).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            webViewClient = WebViewClient()
            settings.javaScriptEnabled =true
            settings.useWideViewPort =true
            settings.javaScriptCanOpenWindowsAutomatically= true

//            addJavascriptInterface("islan")
            loadUrl(mUrl)

        }
    }, update = {
        it.loadUrl(mUrl)
    })
}

// For displaying preview in
// the Android Studio IDE emulator
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
}
