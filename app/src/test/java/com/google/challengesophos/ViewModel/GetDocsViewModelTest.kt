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
class GetDocsViewModelTest{

    lateinit var getDocsViewModel: GetDocsViewModel

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
    fun `ViewModel with correct email consumes the API and brings docs`() = runTest {
        //Given a fresh viewModel
        getDocsViewModel= GetDocsViewModel()

        //When a valid input is given
        getDocsViewModel.getDocsList("arturo3393@gmail.com")
        getDocsViewModel.getDocsModelLiveData.getOrAwaitValue()
        val value = getDocsViewModel.getDocsModelLiveData.value

        //then the value size is greater than 1
        assert(value?.size!! >=1)

    }

    @Test
    fun `ViewModel with incorrect email consumes the API and brings docs`() = runTest {
        //Given a fresh viewModel
        getDocsViewModel= GetDocsViewModel()

        //When a valid input is given
        getDocsViewModel.getDocsList("example@gmail.com")
        getDocsViewModel.getDocsModelLiveData.getOrAwaitValue()
        val value = getDocsViewModel.getDocsModelLiveData.value

        //then value size is equal to 0
        assert(value?.size == 0)

    }

}

