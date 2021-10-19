package com.example.aibflickrbrowserapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.coroutines.*
import org.json.JSONObject
import java.net.URL

class MainActivity : AppCompatActivity() {

    private lateinit var EDTS: EditText
    private lateinit var BTN: Button
    private lateinit var RV: RecyclerView
    private lateinit var LiLayout: LinearLayout
    private lateinit var ImageV: ImageView
    private lateinit var TheRVAdapter: RVAdapter
    private lateinit var images: ArrayList<Image>

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        RV = findViewById(R.id.rv)
        LiLayout = findViewById(R.id.llayout)
        EDTS = findViewById(R.id.edtSearch)
        BTN = findViewById(R.id.btn)
        ImageV = findViewById(R.id.imegev)

        images = arrayListOf()
        TheRVAdapter = RVAdapter(this, images)
        RV.adapter = TheRVAdapter
        RV.layoutManager = LinearLayoutManager(this)



        BTN.setOnClickListener {
            if(EDTS.text.isNotEmpty()){
                requestAPI()
            }else{
                Toast.makeText(this, "Search field is empty", Toast.LENGTH_LONG).show()
            }
        }


        ImageV.setOnClickListener {
            closeImg()
        }

    }
    private fun requestAPI(){
        CoroutineScope(Dispatchers.IO).launch {
            val data = async { getPhotos() }.await()
            if(data.isNotEmpty()){
                println(data)
                showPhotos(data)
            }else{
                Toast.makeText(this@MainActivity, "No Images Found", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun getPhotos(): String
    {
        var response = ""

        try
        {
            response = URL("https://api.flickr.com/services/rest/?method=flickr.photos.search&per_page=10&api_key=cb0cbca5c50568f7e3189b08d8e6a89b&tags=${EDTS.text}&format=json&nojsoncallback=1")
                .readText(Charsets.UTF_8)
        }
        catch(e: Exception)
        {
            println("ISSUE: $e")
        }
        return response
    }

    private suspend fun showPhotos(data: String)
    {

        withContext(Dispatchers.Main)
        {
            val jsonObj = JSONObject(data)
            val TheImage = jsonObj.getJSONObject("photos").getJSONArray("photo")

            println("photos")
            println(TheImage.getJSONObject(0))
            println(TheImage.getJSONObject(0).getString("farm"))

            for(i in 0 until TheImage.length())
            {
                val title = TheImage.getJSONObject(i).getString("title")
                val farmID = TheImage.getJSONObject(i).getString("farm")
                val serverID = TheImage.getJSONObject(i).getString("server")
                val id = TheImage.getJSONObject(i).getString("id")
                val secret = TheImage.getJSONObject(i).getString("secret")
                val photoLink = "https://farm$farmID.staticflickr.com/$serverID/${id}_$secret.jpg"

                images.add(Image(title, photoLink))
            }

            TheRVAdapter.notifyDataSetChanged()
        }
    }

    fun openImg(link: String)
    {
        Glide.with(this).load(link).into(ImageV)
        ImageV.isVisible = true
        RV.isVisible = false
        LiLayout.isVisible = false
    }

    private fun closeImg(){
        ImageV.isVisible = false
        RV.isVisible = true
        LiLayout.isVisible = true
    }
}