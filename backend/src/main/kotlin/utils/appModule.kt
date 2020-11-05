package utils

import com.google.gson.Gson
import graphql.AppSchema
import org.koin.dsl.module

val appModule = module {

    single { Gson() }
    single { AppSchema() }
}