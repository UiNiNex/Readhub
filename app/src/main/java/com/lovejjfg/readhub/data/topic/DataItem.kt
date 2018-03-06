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

package com.lovejjfg.readhub.data.topic

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DataItem(
        @field:SerializedName("isExband")
        var isExband: Boolean? = false,

        @field:SerializedName("summary")
        val summary: String? = null,

        @field:SerializedName("createdAt")
        val createdAt: String? = null,

//        @field:SerializedName("relatedTopicArray")
//        val relatedTopicArray: List<EntityRelatedTopicsItem?>? = null,

        @field:SerializedName("nelData")
        val nelData: NelData? = null,

        @field:SerializedName("publishDate")
        val publishDate: String? = null,

        @field:SerializedName("id")
        val id: String? = null,

        @field:SerializedName("newsArray")
        val newsArray: List<NewsArrayItem?>? = null,

        @field:SerializedName("title")
        val title: String? = null,

        @field:SerializedName("entityRelatedTopics")
        val entityRelatedTopics: List<EntityRelatedTopicsItem?>? = null,

        @field:SerializedName("order")
        val order: String? = null,

        @field:SerializedName("updatedAt")
        val updatedAt: String? = null,

        @field:SerializedName("authorName")
        val authorName: String? = null,

        @field:SerializedName("siteName")
        val siteName: String? = null,

        @field:SerializedName("url")
        val url: String? = null,

        @field:SerializedName("extra")
        val extra: Extra? = null,

        var isTop: Boolean = false


) : Parcelable