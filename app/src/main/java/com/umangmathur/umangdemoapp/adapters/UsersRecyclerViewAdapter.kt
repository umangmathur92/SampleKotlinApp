package com.umangmathur.umangdemoapp.adapters

import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.umangmathur.umangdemoapp.R
import com.umangmathur.umangdemoapp.adapters.UsersRecyclerViewAdapter.UserDetailViewHolder
import com.umangmathur.umangdemoapp.model.User.User

class UsersRecyclerViewAdapter(
    private val userList: ArrayList<User>,
    private val toolbarMenuItemClickHandler: MenuItemClickHandler,
    private val listItemClickHandler: ListItemClickHandler
) : Adapter<UserDetailViewHolder>() {

    override fun onBindViewHolder(holder: UserDetailViewHolder, position: Int) {
        val user: User = userList[position]
        holder.txtName.text = user.name
        holder.txtTitle.text = user.company.name
        holder.txtCity.text = user.address.city
        if (holder.toolbar.menu?.size() == 0) {
            holder.toolbar.inflateMenu(R.menu.user_links_menu)
        }
        holder.toolbar.setOnMenuItemClickListener(getMenuItemClickListener(user))
        holder.parentConstraintLayout.setOnClickListener { listItemClickHandler.onListItemClick(user) }
    }

    private fun getMenuItemClickListener(user: User): (MenuItem) -> Boolean {
        return { item: MenuItem ->
            toolbarMenuItemClickHandler.onMenuItemClick(item, user)
            true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserDetailViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.user_item_layout, parent, false)
        return UserDetailViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class UserDetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtName: TextView = itemView.findViewById(R.id.txtName)
        val txtTitle: TextView = itemView.findViewById(R.id.txtTitle)
        val txtCity: TextView = itemView.findViewById(R.id.txtCity)
        val toolbar: Toolbar = itemView.findViewById(R.id.toolbar)
        val parentConstraintLayout: ConstraintLayout = itemView.findViewById(R.id.parentConstraintLayout)
    }

    interface MenuItemClickHandler {
        fun onMenuItemClick(item: MenuItem, user: User)
    }

    interface ListItemClickHandler {
        fun onListItemClick(user: User)
    }

}