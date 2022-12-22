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
class GetDocsByIdViewModelTest{
    lateinit var getDocsByIdViewModel: GetDocsByIdViewModel

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
    fun `ViewModel with correct docId consumes the API and brings specific doc`() = runTest {
        //Given a fresh view model
        getDocsByIdViewModel = GetDocsByIdViewModel()

        //when method with a correct id is called
        getDocsByIdViewModel.getDocsViewModel("11")
        getDocsByIdViewModel.getDocsImgMutableLiveData.getOrAwaitValue()
        val value = getDocsByIdViewModel.getDocsImgMutableLiveData.value

        //assert that value size is not empty and contains the specific doc

        assert(value?.isEmpty() == false)


    }

    @Test
    fun `ViewModel with incorrect docId consumes the API and livedata is empty`() = runTest {
        //Given a fresh view model
        getDocsByIdViewModel = GetDocsByIdViewModel()

        //when method with a correct id is called
        getDocsByIdViewModel.getDocsViewModel("13213")
        getDocsByIdViewModel.getDocsImgMutableLiveData.getOrAwaitValue()
        val value = getDocsByIdViewModel.getDocsImgMutableLiveData.value

        //assert that value size is not empty and contains the specific doc

        assert(value?.isEmpty() == true)


    }


}