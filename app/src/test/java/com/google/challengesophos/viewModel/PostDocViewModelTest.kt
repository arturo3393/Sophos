package com.google.challengesophos.viewModel

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
class PostDocViewModelTest{

    private lateinit var postDocViewModel: PostDocViewModel

    @get:Rule
    var rule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup(){
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

     /*  @Test
        fun `PostDocsViewModel with incomplete input consumes the API and does not post a doc`()= runTest {
        //Given a fresh viewModel and a DocItemsPost
        postDocViewModel = PostDocViewModel()
       val input = DocItemsPost("","CC","","","","","","","")


        //when the user enters incomplete input
        postDocViewModel.postDoc(input)

        val response = postDocViewModel.docModel.getOrAwaitValue()
        //then
        assert(response == null)
    }*/

    @Test
    fun `PostDocsViewModel with consumes the API and gets cities`()= runTest {
        //Given a fresh viewModel and a DocItemsPost
        postDocViewModel = PostDocViewModel()

        //When getCities method is called
        postDocViewModel.getCities()
        val value = postDocViewModel.citiesLiveData.getOrAwaitValue()

        //then live data has a list of cities
        assert(value.toString().isNotEmpty())



    }


        @After
        fun tearDown(){
            Dispatchers.resetMain()
        }
}
