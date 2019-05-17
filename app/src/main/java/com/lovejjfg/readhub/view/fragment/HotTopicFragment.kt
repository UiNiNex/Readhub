/*
 *
 *   Copyright (c) 2017.  Joe
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package com.lovejjfg.readhub.view.fragment

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.support.design.widget.BaseTransientBottomBar
import android.support.design.widget.Snackbar
import android.support.v4.widget.SwipeRefreshLayout
import android.text.TextUtils
import android.util.Log
import com.lovejjfg.powerrecycle.PowerAdapter
import com.lovejjfg.readhub.R
import com.lovejjfg.readhub.base.RefreshFragment
import com.lovejjfg.readhub.data.DataManager
import com.lovejjfg.readhub.data.topic.DataItem
import com.lovejjfg.readhub.utils.RxBus
import com.lovejjfg.readhub.utils.event.ScrollEvent
import com.lovejjfg.readhub.utils.isTopOrder
import com.lovejjfg.readhub.view.HomeActivity
import com.lovejjfg.readhub.view.recycerview.HotTopicAdapter
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.layout_refresh_recycler.refreshContainer

/**
 * ReadHub
 * Created by Joe at 2017/7/30.
 */
class HotTopicFragment : RefreshFragment() {

    private var refreshCount: Int = 0
    override fun createAdapter(): PowerAdapter<DataItem> {
        return HotTopicAdapter()
    }

    override fun afterCreatedView(savedInstanceState: Bundle?) {
        super.afterCreatedView(savedInstanceState)
        adapter.setOnItemClickListener { _, position, item ->
            item.isExband = !item.isExband
            adapter.notifyItemChanged(position)
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        println(" HotTopicFragment onHiddenChanged:: $hidden")
        if (!hidden) {
            checkNews()
        }
    }

    override fun saveListData(): Boolean {
        return true
    }

    private fun checkNews() {
        refreshCount = 0
        if (adapter.list.isEmpty()) {
            return
        }
        if (TextUtils.isEmpty(latestOrder)) {
            return
        }
        val latestOrder = latestOrder ?: return
        DataManager.subscribe(this, DataManager.init().newCount(latestOrder), Consumer {
            refreshCount = it.count
            if (adapter.list?.firstOrNull()?.isTop == true) {
                refreshCount--
            }
            if (refreshCount > 0) {
                showHint()
            }
        }, Consumer { it.printStackTrace() })
    }

    private fun showHint() {
        if (mIsVisible) {
            hideNav(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    makeSnack(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
                        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                            showNav()
                        }
                    })
                }
            })
        } else {
            makeSnack(null)
        }
    }

    private fun makeSnack(callback: BaseTransientBottomBar.BaseCallback<Snackbar>?) {
        if (mSnackBar?.isShown == true) {
            mSnackBar?.dismiss()
        }
        if (mSnackBar == null) {
            @Suppress("DEPRECATION")
            mSnackBar = Snackbar.make(
                refreshContainer, String.format(getString(R.string.hot_topic_update), refreshCount), Snackbar
                    .LENGTH_INDEFINITE
            )
                .setActionTextColor(resources.getColor(R.color.colorAccent))
                .setAction(R.string.refresh) {
                    RxBus.instance.post(ScrollEvent())
                    refreshContainer.isRefreshing = true
                    refresh(refreshContainer)
                    (activity as HomeActivity).selectItem(R.id.navigation_home)
                }
        }
        callback?.let {
            mSnackBar?.addCallback(callback)
        }

        mSnackBar?.setText(String.format(getString(R.string.hot_topic_update, refreshCount)))
        mSnackBar?.show()
    }

    override fun onResume() {
        super.onResume()
        checkNews()
    }

    override fun refresh(refresh: SwipeRefreshLayout?) {
        mSnackBar?.dismiss()
        DataManager.subscribe(this, DataManager.init().hotTopic(),
            Consumer { hotTopic ->
                if (hotTopic.data.isNotEmpty()) {
                    preOrder = latestOrder
                    latestOrder = hotTopic.data.first().order
                    if (latestOrder?.isTopOrder() == true) {
                        latestOrder = hotTopic.data[1].order
                        hotTopic.data.first().isTop = true
                    } else {
                        hotTopic.data.first().isTop = false
                    }
                    order = hotTopic.data.last().order
                    adapter.setList(hotTopic.data)
                    handleAlreadRead(false, hotTopic.data) {
                        TextUtils.equals(it?.order, preOrder)
                    }
                }
                refresh?.isRefreshing = false
            },
            Consumer {
                it.printStackTrace()
                adapter.showError(false)
                handleError(it)
                refresh?.isRefreshing = false
            })
    }

    override fun loadMore() {
        val order = order ?: return
        DataManager.subscribe(this, DataManager.init().hotTopicMore(order, 10),
            Consumer { hotTopic ->
                val data = hotTopic.data
                this.order = data.last().order
                adapter.appendList(data)
                handleAlreadRead(true, adapter.list) {
                    TextUtils.equals(it?.order, preOrder)
                }
                Log.i(TAG, "loadMore:order:$order")
            },
            Consumer {
                it.printStackTrace()
                adapter.loadMoreError()
            })
    }
}
