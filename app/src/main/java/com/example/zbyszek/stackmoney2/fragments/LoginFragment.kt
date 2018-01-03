package com.example.zbyszek.stackmoney2.fragments

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.zbyszek.stackmoney2.R
import com.example.zbyszek.stackmoney2.sql.AppDatabase
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import com.example.zbyszek.stackmoney2.activities.MainActivity
import com.example.zbyszek.stackmoney2.helpers.Preferences


class LoginFragment : Fragment() {
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private var mAuthTask: UserLoginTask? = null
    lateinit var database : AppDatabase

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.fragment_login, container, false)

        view.email_sign_in_button.setOnClickListener { attemptLogin() }
        databaseConnection()

        return view
    }

    private fun attemptLogin() {
        if (mAuthTask != null) {
            return
        }
        // Store values at the time of the login attempt.
        val loginStr = login.text.toString()
        val passwordStr = password.text.toString()

        var cancel = false
        var focusView: View? = null

        // Check for a valid email address.
        if (TextUtils.isEmpty(passwordStr)) {
            password.error = getString(R.string.error_field_required)
            focusView = password
            cancel = true
        }
        // Check for a valid email address.
        if (TextUtils.isEmpty(loginStr)) {
            login.error = getString(R.string.error_field_required)
            focusView = login
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
            mAuthTask = UserLoginTask(loginStr, passwordStr)
            mAuthTask!!.execute(null as Void?)
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private fun showProgress(show: Boolean) {
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
    }

    fun databaseConnection(){
        database = AppDatabase.getInMemoryDatabase(context!!)
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    inner class UserLoginTask internal constructor(private val mEmail: String, private val mPassword: String) : AsyncTask<Void, Void, Boolean>() {

        override fun doInBackground(vararg params: Void): Boolean? {
            // TODO: attempt authentication against a network service.

            doAsync {
                val result = database.userDAO().userExists(mEmail, mPassword)
                val user = database.userDAO().getUser(mEmail, mPassword)

                uiThread {
                    if (result){
                        Preferences.setUserId(user!!.id, activity!!)
                        finish()
                    }
                    else {
                        password.error = getString(R.string.error_incorrect_password)
                        password.requestFocus()
                    }
                }
            }

            return false
        }

        override fun onPostExecute(success: Boolean?) {
            mAuthTask = null
            showProgress(false)


//            if (success!!) {
//                finish()
//            } else {
//                password.error = getString(R.string.error_incorrect_password)
//                password.requestFocus()
//            }
        }

        override fun onCancelled() {
            mAuthTask = null
            showProgress(false)
        }

        private fun finish(){
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra("page_id", 1)
            startActivity(intent)
        }
    }

    companion object {
        fun newInstance(): LoginFragment {
            val fragment = LoginFragment()
            return fragment
        }
    }
}
