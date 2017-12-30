package com.example.zbyszek.stackmoney2

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.arch.persistence.room.Room
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.zbyszek.stackmoney2.model.Question
import com.example.zbyszek.stackmoney2.model.User
import com.example.zbyszek.stackmoney2.sql.AppDatabase
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.fragment_register.view.*
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.AnkoAsyncContext
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.doAsyncResult
import org.jetbrains.anko.uiThread

class RegisterFragment : Fragment() {
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private var mAuthTask: RegisterFragment.UserRegisterTask? = null
    lateinit var database : AppDatabase

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view =  inflater!!.inflate(R.layout.fragment_register, container, false)

        view.email_sign_up_button.setOnClickListener { attemptRegister() }
        databaseConnection()

        return view
    }

    fun databaseConnection(){
        database = AppDatabase.getInMemoryDatabase(context)
    }


    private fun attemptRegister() {
        if (mAuthTask != null) {
            return
        }

        // Reset errors.
        login.error = null
        password.error = null
        password_repeat.error = null
        question.error = null
        question_answer.error = null

        // Store values at the time of the login attempt.
        val loginStr = login.text.toString()
        val passwordStr = password.text.toString()
        val passwordRepeatStr = password_repeat.text.toString()
        val questionStr = question.text.toString()
        val questionAnswerStr = question_answer.text.toString()

        var cancel = false
        var focusView: View? = null

        // Check for a valid email address.
        if (TextUtils.isEmpty(loginStr)) {
            login.error = getString(R.string.error_field_required)
            focusView = login
            cancel = true
        } else if (!isLoginValid(loginStr)) {
            login.error = getString(R.string.error_invalid_login)
            focusView = login
            cancel = true
        }


        // Check for a valid password.
        if (TextUtils.isEmpty(passwordStr)) {
            password.error = getString(R.string.error_field_required)
            focusView = password
            cancel = true
        } else if (!isPasswordValid(passwordStr)) {
            password.error = getString(R.string.error_invalid_password)
            focusView = password
            cancel = true
        }


        // Check for a valid repeated password.
        if (TextUtils.isEmpty(passwordRepeatStr)) {
            password_repeat.error = getString(R.string.error_field_required)
            focusView = password_repeat
            cancel = true
        } else if (passwordRepeatStr != passwordStr) {
            password_repeat.error = getString(R.string.error_invalid_password)
            focusView = password_repeat
            cancel = true
        }


        // Check for a valid repeated password.
        if (TextUtils.isEmpty(questionStr)) {
            question.error = getString(R.string.error_field_required)
            focusView = question
            cancel = true
        } else if (!isQuestionValid(questionStr)) {
            question.error = getString(R.string.error_invalid_password)
            focusView = question
            cancel = true
        }


        // Check for a valid repeated password.
        if (TextUtils.isEmpty(questionAnswerStr)) {
            question_answer.error = getString(R.string.error_field_required)
            focusView = question_answer
            cancel = true
        } else if (!isQuestionAnswerValid(questionAnswerStr)) {
            question_answer.error = getString(R.string.error_invalid_password)
            focusView = question_answer
            cancel = true
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView?.requestFocus()
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true)
            mAuthTask = UserRegisterTask(loginStr, passwordStr, questionStr, questionAnswerStr)
            mAuthTask!!.execute(null as Void?)
        }
    }

    private fun isLoginValid(login: String): Boolean {
        //TODO: Replace this with your own logic
//        return email.contains("@")
        var result = true

        doAsync {
            result = database.userDAO().userLoginExists(login)
        }

        return login.length > 4 //&& !result
    }

    private fun isPasswordValid(password: String): Boolean {
        //TODO: Replace this with your own logic
        return password.length > 4
    }

    private fun isQuestionValid(password: String): Boolean {
        //TODO: Replace this with your own logic
        return password.length < 128
    }

    private fun isQuestionAnswerValid(password: String): Boolean {
        //TODO: Replace this with your own logic
        return password.length < 32
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private fun showProgress(show: Boolean) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

            login_form.visibility = if (show) View.GONE else View.VISIBLE
            login_form.animate()
                    .setDuration(shortAnimTime)
                    .alpha((if (show) 0 else 1).toFloat())
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            login_form.visibility = if (show) View.GONE else View.VISIBLE
                        }
                    })

            login_progress.visibility = if (show) View.VISIBLE else View.GONE
            login_progress.animate()
                    .setDuration(shortAnimTime)
                    .alpha((if (show) 1 else 0).toFloat())
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            login_progress.visibility = if (show) View.VISIBLE else View.GONE
                        }
                    })
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            login_progress.visibility = if (show) View.VISIBLE else View.GONE
            login_form.visibility = if (show) View.GONE else View.VISIBLE
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    inner class UserRegisterTask internal constructor(private val mEmail: String,
                                                      private val mPassword: String,
                                                      private val mQuestion: String,
                                                      private val mAnswer: String) : AsyncTask<Void, Void, Boolean>() {

        override fun doInBackground(vararg params: Void): Boolean? {
            var user = User(mEmail, mPassword)

            doAsync {
                var result = database.userDAO().insertUser(user)
                if (result != null){
                    var question = Question(result, mQuestion, mAnswer)
                    database.questionDAO().insertQuestion(question)
                }

                uiThread {
                    if (result != null){
                        finish()
                    }
                }
            }

            return false
        }

        override fun onPostExecute(success: Boolean?) {
            mAuthTask = null
            showProgress(false)

            if (success!!) {
                finish()
            } else {
                password.error = getString(R.string.error_incorrect_password)
                password.requestFocus()
            }
        }

        override fun onCancelled() {
            mAuthTask = null
            showProgress(false)
        }

        private fun finish(){
            val intent: Intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
        }
    }
    
    companion object {
        fun newInstance(): RegisterFragment {
            val fragment = RegisterFragment()
            return fragment
        }
    }
}