package com.umangmathur.umangdemoapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import com.umangmathur.umangdemoapp.adapters.UsersRecyclerViewAdapter
import com.umangmathur.umangdemoapp.adapters.UsersRecyclerViewAdapter.MenuItemClickHandler
import com.umangmathur.umangdemoapp.model.User.User
import com.umangmathur.umangdemoapp.network.PlaceholderApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit

class UsersFragment : BaseFragment(), OnClickListener {

    private lateinit var usersRecyclerView: RecyclerView
    private var allUserList: ArrayList<User> = ArrayList()
    private lateinit var usersRecyclerViewAdapter: UsersRecyclerViewAdapter
    private val compositeDisposable = CompositeDisposable()
    private lateinit var progressBar :ProgressBar
    private lateinit var errorLayout : LinearLayout
    private lateinit var btnRetry : AppCompatButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_users_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        usersRecyclerView = view.findViewById(R.id.usersRecyclerView)
        progressBar = view.findViewById(R.id.progressBar)
        errorLayout = view.findViewById(R.id.errorLayout)
        btnRetry = view.findViewById(R.id.btnRetry)
        btnRetry.setOnClickListener(this)
        usersRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        usersRecyclerViewAdapter =
                UsersRecyclerViewAdapter(
                    allUserList,
                    getMenuItemClickHandler(),
                    getListItemClickHandler()
                )
        usersRecyclerView.adapter = usersRecyclerViewAdapter
        if (savedInstanceState == null) {
            fetchAllUsers()
        } else {
            val usrList: ArrayList<User> = savedInstanceState.getParcelableArrayList("allUserList")
            notifyAdapterAndUpdateUi(usrList)
        }
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putParcelableArrayList("allUserList", allUserList)
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }

    private fun fetchAllUsers() {
        progressBar.visibility = View.VISIBLE
        errorLayout.visibility = View.GONE
        val retrofit: Retrofit = initializeRetrofit()
        val apiPlaceHolder: PlaceholderApi = retrofit.create(PlaceholderApi::class.java)
        val fetchAllUsersDisposable: Disposable = apiPlaceHolder.getAllUsers()
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ apiResponse: ArrayList<User> ->
                val usrList: ArrayList<User> = apiResponse
                notifyAdapterAndUpdateUi(usrList)
                progressBar.visibility = View.GONE
                errorLayout.visibility = View.GONE
            }, { throwable: Throwable ->
                progressBar.visibility = View.GONE
                errorLayout.visibility = View.VISIBLE
                Log.d(TAG, "Something went wrong: " + throwable.message)
            })
        compositeDisposable.add(fetchAllUsersDisposable)
    }

    private fun notifyAdapterAndUpdateUi(usrList: ArrayList<User>) {
        allUserList.clear()
        allUserList.addAll(usrList)
        usersRecyclerViewAdapter.notifyDataSetChanged()
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btnRetry -> fetchAllUsers()
        }
    }

    private fun getListItemClickHandler(): UsersRecyclerViewAdapter.ListItemClickHandler {
        return object : UsersRecyclerViewAdapter.ListItemClickHandler {
            override fun onListItemClick(user: User) {
                val userPostFragment: Fragment = UserPostFragment()
                var argBundle = Bundle()
                argBundle.putParcelable("user", user)
                userPostFragment.arguments = argBundle
                activity?.supportFragmentManager?.beginTransaction()?.add(R.id.content_fragment, userPostFragment)
                    ?.addToBackStack(null)?.commit()
            }
        }
    }

    private fun getMenuItemClickHandler(): MenuItemClickHandler {
        return object : MenuItemClickHandler {
            override fun onMenuItemClick(item: MenuItem, user: User) {
                when (item.itemId) {
                    R.id.userEmail -> composeEmail(user.email)
                    R.id.userPhone -> dialPhoneNumber(user.phone)
                    R.id.userWebsite -> openWebPage("http://www." + user.website)
                    R.id.userLocation -> showMap(
                        Uri.parse("geo:${user.address.geoCoordinates.latitude},${user.address.geoCoordinates.longitude}")
                    )
                }
            }
        }
    }

    private fun composeEmail(address: String) {
        val intent: Intent = Intent(Intent.ACTION_SENDTO).apply {
            this.data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(address))
        }
        if (intent.resolveActivity(context!!.packageManager) != null) {
            startActivity(intent)
        }
    }

    private fun dialPhoneNumber(phoneNumber: String) {
        val intent: Intent = Intent(Intent.ACTION_DIAL).apply {
            this.data = Uri.parse("tel:$phoneNumber")
        }
        if (intent.resolveActivity(context!!.packageManager) != null) {
            startActivity(intent)
        }
    }

    private fun openWebPage(url: String) {
        val webPage: Uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, webPage)
        if (intent.resolveActivity(context!!.packageManager) != null) {
            startActivity(intent)
        }
    }

    private fun showMap(geoLocation: Uri) {
        val intent: Intent = Intent(Intent.ACTION_VIEW).apply {
            this.data = geoLocation
        }
        if (intent.resolveActivity(context!!.packageManager) != null) {
            startActivity(intent)
        }
    }

}