package com.ben.datamodule

import com.ben.data.Server
import org.koin.dsl.module

val dataModule = module {
    single { ServerImpl() as Server }
}