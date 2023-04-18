package com.example.navegador

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import com.example.navegador.databinding.ActivityMainBinding
import java.net.InetAddress
import java.net.UnknownHostException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                //Verifica se string consegue conectar no servidor
                Thread {
                    val url = try {
                        InetAddress.getByName(query)
                        "https://$query"
                    } catch (e: UnknownHostException) {
                        "https://www.google.com/search?query=$query"
                    }

                    //url montada acessar web view
                    runOnUiThread {
                        binding.web.loadUrl(url)
                    }
                }.start()

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })
        //ativando javaScript
        binding.web.settings.javaScriptEnabled = true
        binding.web.loadUrl("https://google.com")
        //Falando que é para renderizar as paginas internas dentro da web view
        binding.web.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                //verificando se pode voltar ou nao
                binding.imgBack.isEnabled = binding.web.canGoBack()
                binding.imgForward.isEnabled = binding.web.canGoForward()
                binding.search.setQuery(url, false)

                //tratando a cor dos botoes
                binding.imgBack.imageTintList = if (binding.imgBack.isEnabled)
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.teal_200
                        )
                    )
                else
                    ColorStateList.valueOf(Color.GRAY)

                binding.imgForward.imageTintList = if (binding.imgForward.isEnabled)
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.teal_200
                        )
                    )
                else
                    ColorStateList.valueOf(Color.GRAY)


                super.onPageFinished(view, url)
            }
        }

        //redirecionar no navedor
        binding.imgBack.setOnClickListener {
            binding.web.goBack()
        }

        binding.imgForward.setOnClickListener {
            binding.web.goForward()
        }
    }

}