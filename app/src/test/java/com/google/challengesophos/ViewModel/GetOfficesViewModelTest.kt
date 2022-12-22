package com.google.challengesophos.ViewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.challengesophos.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class GetOfficesViewModelTest{

    private lateinit var getOfficesViewModel: GetOfficesViewModel

    @get:Rule
    var rule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup(){
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }

    @Test
    fun `ViewModel  consumes the API and brings offices information`() = runTest {
        //Given a fresh viewModel
        getOfficesViewModel = GetOfficesViewModel()

        //when getOffices method is called
        getOfficesViewModel.getOffices()
        getOfficesViewModel.citiesLiveData.getOrAwaitValue()
        val value = getOfficesViewModel.citiesLiveData.value

        assert(!value.isNullOrEmpty())
    }

}