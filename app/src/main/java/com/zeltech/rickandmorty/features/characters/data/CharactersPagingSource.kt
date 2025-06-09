package com.zeltech.rickandmorty.features.characters.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.zeltech.rickandmorty.common.domain.model.Character
import com.zeltech.rickandmorty.features.characters.domain.CharactersService

class CharactersPagingSource(
    val service: CharactersService,
    val name: String?,
    val status: String?,
    val gender: String?,
) : PagingSource<Int, Character>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
        val page = params.key ?: 1
        return try {
            val response = service.getFilteredCharacters(
                page = page,
                name = name,
                status = status,
                gender = gender
            )

            val characters = response?.results ?: emptyList()
            val nextPage = response?.info?.next
            val prevPage = response?.info?.prev

            LoadResult.Page(
                data = characters,
                prevKey = prevPage,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Character>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
