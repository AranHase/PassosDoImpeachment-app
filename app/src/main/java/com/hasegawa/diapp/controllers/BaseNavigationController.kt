/*
 * Copyright 2016 Allan Yoshio Hasegawa
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hasegawa.diapp.controllers

import android.os.Build
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.bluelinelabs.conductor.Controller
import com.hasegawa.diapp.DiApp
import com.hasegawa.diapp.R
import com.hasegawa.diapp.activities.MainActivity
import com.hasegawa.diapp.domain.devices.LogDevice
import com.hasegawa.diapp.domain.devices.ScreenDevice
import com.hasegawa.diapp.presentation.presenters.NavigationPresenter
import com.hasegawa.diapp.presentation.views.MainMvpView
import com.hasegawa.diapp.presentation.views.NavigationMvpView
import com.hasegawa.diapp.utils.BundleBuilder
import com.hasegawa.diapp.utils.ResourcesUtils
import javax.inject.Inject


fun NavigationMvpView.Item.toMainRoute(): MainMvpView.Route =
        when (this) {
            NavigationMvpView.Item.StepsList -> MainMvpView.Route.Steps
            NavigationMvpView.Item.NewsList -> MainMvpView.Route.News
            NavigationMvpView.Item.Credits -> MainMvpView.Route.Credits
            else -> throw RuntimeException("$this cannot be converted to MainMvpView.Route.")
        }

abstract class BaseNavigationController : Controller,
        NavigationView.OnNavigationItemSelectedListener {

    @Inject lateinit var navigationPresenter: NavigationPresenter
    @Inject lateinit var logDevice: LogDevice
    @Inject lateinit var screenDevice: ScreenDevice

    protected lateinit var baseContainer: FrameLayout
    protected lateinit var navView: NavigationView
    private var drawerLayout: DrawerLayout? = null

    private var itemSelected: NavigationMvpView.Item

    constructor(itemSelected: NavigationMvpView.Item) : this(BundleBuilder(Bundle())
            .putString(BKEY_ITEM_SELECTED, itemSelected.toString()).build())

    constructor(bundle: Bundle) : super(bundle) {
        val itemString = bundle.getString(BKEY_ITEM_SELECTED)
        if (itemString != null) {
            itemSelected = NavigationMvpView.Item.valueOf(itemString)
        } else {
            itemSelected = NavigationMvpView.Item.OpenSource // invalid selection :)
        }
        DiApp.activityComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val root = inflater.inflate(R.layout.screen_base_navigation, container, false)

        navView = root.findViewById(R.id.nav_view) as NavigationView
        baseContainer = root.findViewById(R.id.base_container) as FrameLayout

        if (!screenDevice.isTablet()) {
            drawerLayout = root.findViewById(R.id.drawer_layout) as DrawerLayout
        }

        navView.setNavigationItemSelectedListener(this)

        navigationPresenter.bindView(baseMvpView)
        navigationPresenter.onResume()
        baseMvpView.itemSelectionListener(itemSelected)

        return root
    }

    override fun onDestroyView(view: View?) {
        super.onDestroyView(view)
        navigationPresenter.onPause()
    }

    protected fun setupToolbar(toolbar: Toolbar, padTop: Boolean = false,
                               showUpButton: Boolean = false,
                               touchAction: () -> Unit = {}) {
        val aca = (activity as AppCompatActivity)

        if (padTop && Build.VERSION.SDK_INT >= 21) {
            val statusBarHeight = ResourcesUtils.statusBarHeight(activity)
            toolbar.setPadding(0, statusBarHeight, 0, 0)
        }

        if (!screenDevice.isTablet()) {
            aca.setSupportActionBar(toolbar)
            aca.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            aca.supportActionBar?.setDisplayShowTitleEnabled(false)
            (aca as MainActivity).homeUpButtonTouchListener = touchAction

            if (!showUpButton) {
                val toggle = ActionBarDrawerToggle(aca, drawerLayout,
                        R.string.nav_drawer_open, R.string.nav_drawer_close)
                drawerLayout?.addDrawerListener(toggle)
                toggle.syncState()
                toolbar.setNavigationOnClickListener {
                    drawerLayout?.openDrawer(GravityCompat.START)
                }
            }
        }
    }

    protected var baseMvpView = object : NavigationMvpView() {

        override fun actItemTouched(item: Item) {
            when (item) {
                NavigationMvpView.Item.StepsList,
                NavigationMvpView.Item.NewsList,
                NavigationMvpView.Item.Credits -> {
                    this.drawerStateListener(NavigationMvpView.DrawerState.Closed)
                    onNavigationRouteRequested(item.toMainRoute())
                }
                else -> Unit
            }
        }

        override fun renderItemSelected(item: NavigationMvpView.Item) {
            when (item) {
                Item.StepsList -> navView.setCheckedItem(R.id.nav_steps_list)
                Item.NewsList -> navView.setCheckedItem(R.id.nav_news_list)
                Item.Credits -> navView.setCheckedItem(R.id.nav_credits)
                else -> return
            }
            itemSelected = item
        }

        override fun renderUpdateDateText(text: String) {
            val item = navView.menu.findItem(R.id.nav_last_update)
            item.title = text
        }

        override fun renderClosedNavView() {
            drawerLayout?.closeDrawer(navView)
        }

        override fun renderOpenedNavView() {
            drawerLayout?.openDrawer(navView)
        }

    }

    override fun handleBack(): Boolean {
        if (drawerLayout?.isDrawerOpen(navView) ?: false) {
            baseMvpView.drawerStateListener(NavigationMvpView.DrawerState.Closed)
            return true
        }
        return super.handleBack()
    }

    abstract fun onNavigationRouteRequested(route: MainMvpView.Route)

    override fun onNavigationItemSelected(item: MenuItem?): Boolean {
        if (item == null) return false
        val itemEnum = when (item.itemId) {
            R.id.nav_steps_list -> NavigationMvpView.Item.StepsList
            R.id.nav_news_list -> NavigationMvpView.Item.NewsList
            R.id.nav_credits -> NavigationMvpView.Item.Credits
            R.id.nav_send_feedback -> NavigationMvpView.Item.Feedback
            R.id.nav_opensource_link -> NavigationMvpView.Item.OpenSource
            R.id.nav_last_update -> NavigationMvpView.Item.Update
            else -> throw RuntimeException("unknown item id ${item.itemId}")
        }
        baseMvpView.itemTouchListener(itemEnum)
        return true
    }

    companion object {
        const val BKEY_ITEM_SELECTED = "item_selected"
    }
}
