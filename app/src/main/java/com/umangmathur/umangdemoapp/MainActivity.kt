package com.umangmathur.umangdemoapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            this.supportFragmentManager.beginTransaction().run {
                replace(R.id.content_fragment, UsersFragment()).
                commit()
            }
        }
    }

    fun setActionBarTitle(name: String) {
        supportActionBar?.title = name
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                this.supportFragmentManager.popBackStack()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}