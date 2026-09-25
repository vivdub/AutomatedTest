package com.ai.automated.tests.agents

import com.ai.automated.tests.agent.OpenRouterModel
import io.lettuce.core.json.DefaultJsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.net.URI
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.file.Files
import java.time.Duration

//----------
class OpenRouterModels {

    companion object{
        private val json = Json {
            prettyPrint = true
            ignoreUnknownKeys = true
            encodeDefaults = true
        }
        /** These models don't work out of the box or have some restriction while working */
        private val Restricted = listOf("thinkingmachines/inkling-small:free", "qwen/qwen3.8-27b:free", "thinkingmachines/inkling:free", "poolside/laguna-xs-2.1:free",
            "z-ai/glm-5.2:free", "nvidia/nemotron-3.5-content-safety:free", "google/gemma-4-26b-a4b-it:free", "google/gemma-4-31b-it:free")

        //----
        fun parseJson(strJson:String): MutableList<OpenRouterModel> {
            if(strJson.isBlank()) return mutableListOf()
            val jsonVal = DefaultJsonParser().createJsonValue(strJson)
            val modelsJsonArr = when{
                jsonVal.isJsonObject -> jsonVal.asJsonObject().get("data").asJsonArray() // during api call
                else -> jsonVal.asJsonArray() //from saved data
            }
            val models = mutableListOf<OpenRouterModel>()
            modelsJsonArr.asList().forEach { jsonObj ->
                val model = json.decodeFromString<OpenRouterModel>(jsonObj.toString())
                jsonObj.asJsonObject().apply {
                    try{ model.price = get("pricing").asJsonObject().get("prompt").asString().toDouble()*1000000 }catch (e:Exception){} }
                if(model.price>=0 && !Restricted.contains(model.id))
                    models.add(model)
            }
            return models
        }
    }

    private val http: java.net.http.HttpClient = java.net.http.HttpClient.newBuilder()
        // Deliberately short: if nothing is listening we want to fail over to the
        // cold path immediately rather than making the user wait twice.
        .connectTimeout(Duration.ofMillis(400))
        .build()


    //------
    fun fetch(onFetched:(List<OpenRouterModel>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val url = "https://openrouter.ai/api/v1/models";
            val request = HttpRequest.newBuilder(URI.create(url))
                .header("Content-Type", "application/json")
                .GET()
                .build()
            val resp = http.send(request,HttpResponse.BodyHandlers.ofString()).body()
            onFetched.invoke(parseJson(resp))
        }
    }
}