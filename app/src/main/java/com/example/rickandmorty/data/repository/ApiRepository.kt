package com.example.rickandmorty.data.repository

import com.example.rickandmorty.api.Api
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiRepository @Inject constructor(private val api: Api) {
}