/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.android.marsrealestate.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.marsrealestate.network.MarsApi
import com.example.android.marsrealestate.network.MarsProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * The [ViewModel] that is attached to the [OverviewFragment].
 */
enum class MarsApiStatus {
    LOADING, DONE, ERROR
}

enum class MarsApiFilter(val value: String) {
    SHOW_RENT("rent") ,SHOW_BUY("buy"), SHOW_ALL("all")
}

class OverviewViewModel : ViewModel() {

    val viewModelJob = Job()

    val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    // The internal MutableLiveData String that stores the status of the most recent request
    private val _status = MutableLiveData<MarsApiStatus>()

    // The external immutable LiveData for the request status String
    val status: LiveData<MarsApiStatus>
        get() = _status

    private val _marsProperties = MutableLiveData<List<MarsProperty>>()

    val marsProperties: LiveData<List<MarsProperty>>
    get() = _marsProperties

    private val _navigateToDetailsScreen = MutableLiveData<MarsProperty>()

    private lateinit var _allProperties : List<MarsProperty>

    val navigateToDetailsScreen : LiveData<MarsProperty>
        get() {
            return _navigateToDetailsScreen
        }

    /**
     * Call getMarsRealEstateProperties() on init so we can display status immediately.
     */
    init {
        getMarsRealEstateProperties()
        _navigateToDetailsScreen.value = null
    }

    /**
     * Sets the value of the status LiveData to the Mars API status.
     */
    private fun getMarsRealEstateProperties() {
        coroutineScope.launch {
            try {
                _status.value = MarsApiStatus.LOADING

                var listResult = MarsApi.retrofitService.getProperties()
                if(listResult.size > 0) {
                    _status.value = MarsApiStatus.DONE
                  _marsProperties.value = listResult
                    _allProperties = listResult
                }
            } catch (e: Throwable) {
                _status.value = MarsApiStatus.ERROR
            }
        }
    }

    fun refresh() {
        getMarsRealEstateProperties()
    }

    fun displayPropertyDetailsScreen(marsProperty: MarsProperty) {
        _navigateToDetailsScreen.value = marsProperty
    }

    fun doneNavigatingToDetailsScreen() {
        _navigateToDetailsScreen.value = null
    }

    fun onFilter(filter: MarsApiFilter) {
        _marsProperties.value = _allProperties.filter {
            when(filter) {
                MarsApiFilter.SHOW_ALL -> (it.type == MarsApiFilter.SHOW_BUY.value || it.type == MarsApiFilter.SHOW_RENT.value)
                else -> it.type == filter.value
            }

        }
    }
}