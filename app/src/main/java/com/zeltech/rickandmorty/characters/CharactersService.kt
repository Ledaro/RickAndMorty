package com.zeltech.rickandmorty.characters

import com.zeltech.rickandmorty.common.data.model.Character

interface CharactersService {
    fun getCharacters(): List<Character>
    fun getCharacter(id: Int): Character
}
