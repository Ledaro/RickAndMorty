package com.example.rickandmorty.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.rickandmorty.util.Constants.Companion.STARTING_PAGE_INDEX
import com.example.rickandmorty.data.remote.Api
import retrofit2.HttpException
import java.io.IOException

class CharactersPagingSource(
    private val api: Api,
    private val query: String
) : PagingSource<Int, com.example.rickandmorty.model.Character>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, com.example.rickandmorty.model.Character> {
        val position = params.key ?: STARTING_PAGE_INDEX

        return try {
            val response = api.searchCharacter(position, query)
            val characters = response.results

            LoadResult.Page(
                data = characters,
                prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (characters.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, com.example.rickandmorty.model.Character>): Int? {
        TODO("Not yet implemented")
    }
}
