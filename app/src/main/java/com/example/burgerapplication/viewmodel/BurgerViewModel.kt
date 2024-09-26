package com.example.burgerapplication.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.burgerapplication.dto.Burger
import com.example.burgerapplication.repository.BurgerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BurgerViewModel @Inject constructor(private val repository: BurgerRepository) : ViewModel() {

    private val _burgers = MutableLiveData<List<Burger>>()
    val burgers: LiveData<List<Burger>> get() = _burgers

    fun loadBurgers() {
        viewModelScope.launch {
            val burgerList = repository.getBurgers()
            _burgers.value = burgerList
        }
    }
}