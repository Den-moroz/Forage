package com.example.forage.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.forage.data.ForageableDao
import com.example.forage.model.Forageable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ForageableViewModel(
    private val forageableDao: ForageableDao): ViewModel() {

    val allItems: LiveData<List<Forageable>> = forageableDao.getForageables().asLiveData()

    fun retrieveForageable(id: Long): LiveData<Forageable> {
        return forageableDao.getForageable(id).asLiveData()
    }

    fun addForageable(
        name: String,
        address: String,
        inSeason: Boolean,
        notes: String
    ) {
        val forageable = getNewForageableEntry(name, address, inSeason, notes)
        viewModelScope.launch {
            forageableDao.insert(forageable)
        }
    }

    fun updateForageable(
        id: Long,
        name: String,
        address: String,
        inSeason: Boolean,
        notes: String
    ) {
        val forageable = getUpdatedForageableEntry(id, name, address, inSeason, notes)
        viewModelScope.launch(Dispatchers.IO) {
            forageableDao.update(forageable)
        }
    }

    fun deleteForageable(forageable: Forageable) {
        viewModelScope.launch(Dispatchers.IO) {
            forageableDao.delete(forageable)
        }
    }

    fun isValidEntry(name: String, address: String): Boolean {
        return name.isNotBlank() && address.isNotBlank()
    }

    private fun getUpdatedForageableEntry(
        id: Long,
        name: String,
        address: String,
        inSeason: Boolean,
        notes: String
    ): Forageable {
        return Forageable(
            id = id,
            name = name,
            address = address,
            inSeason = inSeason,
            notes = notes
        )
    }

    private fun getNewForageableEntry(
        name: String,
        address: String,
        inSeason: Boolean,
        notes: String
    ): Forageable {
        return Forageable(
            name = name,
            address = address,
            inSeason = inSeason,
            notes = notes
        )
    }
}

class ForageableViewModelFactory(private val forageableDao: ForageableDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ForageableViewModel::class.java)) {
            @Suppress
            return ForageableViewModel(forageableDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
