package com.vaibhav.newsarticles.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class NewsRepository {

    // A suspended function for fetching all news articles
    suspend fun <T> fetch() : NewsResult<T>{
        return withContext(Dispatchers.IO) {
            try {
                // Requested and extract json response
                val url = URL(API)
                val connection = url.openConnection() as HttpURLConnection
                var res = ""
                val responseCode = connection.responseCode
                if (responseCode.equals(HttpURLConnection.HTTP_OK)){
                    val reader = BufferedReader(InputStreamReader(connection.inputStream))
                    var line =reader.readLine()
                    while(line != null){
                        res+=line
                        line=reader.readLine()
                    }
                    val list = extract(res)
                    return@withContext NewsResult.Success(data = list)
                }else{
                    return@withContext NewsResult.Error("Something Went Wrong!")
                }
            }catch (e:Exception){
                e.printStackTrace()
                return@withContext NewsResult.Error("An Error Occurred !")
            }

        }
    }

    // extract actual values from json
    private fun extract(response: String): List<News>{
            val list = mutableListOf<News>()
            response?.let {
                val jsonObjet = JSONObject(it)
                // get status from json
                val status = jsonObjet.get("status")
                if (status.equals("ok")) {
                    // get the all arrays
                    val jsonArray = jsonObjet.getJSONArray("articles")
                    for (i in 0 until jsonArray.length()) {
                        // get all objects from array
                        val content = jsonArray.getJSONObject(i)
                        val source = content.getJSONObject("source")
                        val id = source.getString("id")
                        val name = source.getString("name")
                        val author = content.getString("author")
                        val title = content.getString("title")
                        val description = content.getString("description")
                        val url = content.getString("url")
                        val urlToImage = content.getString("urlToImage")
                        val publishedAt = content.getString("publishedAt")
                        val newsContent = content.getString("content")

                        // added all data in list
                        list.add(
                            News(
                                Source(id, name),
                                author,
                                title,
                                description,
                                url,
                                urlToImage,
                                publishedAt,
                                newsContent
                            )
                        )
                    }
                }

            }


          return  list

    }
}