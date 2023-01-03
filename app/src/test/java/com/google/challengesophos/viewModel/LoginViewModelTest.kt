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
class LoginViewModelTest {

    private lateinit var loginViewModel: LoginViewModel

    private val correctEmail = "arturo3393@gmail.com"
    private val correctPassword = "05ftK5Ly0J9s"


    @get:Rule
    var rule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    //subject under test _ action or input _ result state
    @Test
    fun `loginViewModel with correct input consumes the API and returns access true`() = runTest {
        //Given a fresh viewModel
        loginViewModel = LoginViewModel()


        //When the user enters correct data
        loginViewModel.getLoginViewModel(correctEmail, correctPassword)
        val value = loginViewModel.loginApiResponse.getOrAwaitValue()
        //then access is given
         assert(value.body()?.acceso == true)
    }

    @Test
    fun `loginViewModel with valid but incorrect input consumes the API and returns access false`() =
        runTest {
            //Given a fresh viewModel
            loginViewModel = LoginViewModel()

            //When the user enters incorrect data
            loginViewModel.getLoginViewModel("a@a.com", "a")
            val value = loginViewModel.loginApiResponse.getOrAwaitValue()
            //then access is not allowed
            assert(value.body()?.acceso == false)
        }
    @Test
    fun `loginViewModel with no input consumes the API and returns access null`() =
        runTest {
            //Given a fresh viewModel
            loginViewModel = LoginViewModel()

            //When the user enters empty data
            loginViewModel.getLoginViewModel("","")
            val value = loginViewModel.loginApiResponse.getOrAwaitValue()
            //then access is not allowed
            assert(value.body()?.acceso == null)
        }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}




