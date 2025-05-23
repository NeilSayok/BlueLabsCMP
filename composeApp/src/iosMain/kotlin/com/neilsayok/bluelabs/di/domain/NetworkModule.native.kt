package com.neilsayok.bluelabs.di.domain

import com.vipulasri.kachetor.KachetorStorage
import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.dsl.module


actual fun provideNetworkModule(): Module {
    return module {
        single {
            HttpClient(Darwin) {
                install(ContentNegotiation) {
                    json(Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    })
                }
                install(HttpCache) {
                    publicStorage(KachetorStorage(10 * 1024 * 1024)) // 10MB
                }
            }
        }

    }
}