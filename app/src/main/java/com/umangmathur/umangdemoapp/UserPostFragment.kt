package com.umangmathur.umangdemoapp

import android.os.Bundle
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import com.umangmathur.umangdemoapp.adapters.PostsRecyclerViewAdapter
import com.umangmathur.umangdemoapp.model.User.User
import com.umangmathur.umangdemoapp.model.UserPost.UserPost
import com.umangmathur.umangdemoapp.network.PlaceholderApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit

class UserPostFragment : BaseFragment(), OnClickListener {

    private lateinit var postsRecyclerView: RecyclerView
    private var allPostsList: ArrayList<UserPost> = ArrayList()
    private lateinit var postsRecyclerViewAdapter: PostsRecyclerViewAdapter
    private val compositeDisposable = CompositeDisposable()
    private lateinit var user: User
    private lateinit var progressBar : ProgressBar
    private lateinit var errorLayout : LinearLayout
    private lateinit var btnRetry : AppCompatButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_userpost_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postsRecyclerView = view.findViewById(R.id.postsRecyclerView)
        progressBar = view.findViewById(R.id.progressBar)
        errorLayout = view.findViewById(R.id.errorLayout)
        btnRetry = view.findViewById(R.id.btnRetry)
        btnRetry.setOnClickListener(this)
        postsRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        postsRecyclerViewAdapter = PostsRecyclerViewAdapter(allPostsList)
        postsRecyclerView.adapter = postsRecyclerViewAdapter
        user = this.arguments?.get("user") as User
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setActionBarTitle(user.name)
        if (savedInstanceState == null) {
            fetchAllPosts(user.id)
        } else {
            user = savedInstanceState.getParcelable("user")
            val postsList: ArrayList<UserPost> = savedInstanceState.getParcelableArrayList("allPostsList")
            notifyAdapterAndUpdateUi(postsList)
        }
    }

    private fun notifyAdapterAndUpdateUi(postsList: ArrayList<UserPost>) {
        allPostsList.clear()
        allPostsList.addAll(postsList)
        postsRecyclerViewAdapter.notifyDataSetChanged()
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btnRetry -> fetchAllPosts(user.id)
        }
    }

    private fun fetchAllPosts(userId: Int) {
        progressBar.visibility = View.VISIBLE
        errorLayout.visibility = View.GONE
        val retrofit: Retrofit = initializeRetrofit()
        val apiPlaceHolder: PlaceholderApi = retrofit.create(PlaceholderApi::class.java)
        val fetchPostsDisposable: Disposable = apiPlaceHolder.getPostsForuser(userId)
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ apiResponse: ArrayList<UserPost> ->
                val postsList: ArrayList<UserPost> = apiResponse
                notifyAdapterAndUpdateUi(postsList)
                progressBar.visibility = View.GONE
                errorLayout.visibility = View.GONE
            }, { throwable: Throwable ->
                progressBar.visibility = View.GONE
                errorLayout.visibility = View.VISIBLE
                Log.d(TAG, "Something went wrong: " + throwable.message)
            })
        compositeDisposable.add(fetchPostsDisposable)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList("allPostsList", allPostsList)
        outState.putParcelable("user", user)
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
        setActionBarTitle(getString(R.string.app_name))
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

}
