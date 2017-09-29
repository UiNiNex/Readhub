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

package com.lovejjfg.readhub.data.topic.develop

import com.google.gson.annotations.SerializedName
import com.lovejjfg.readhub.data.topic.DataItem
import javax.annotation.Generated

@Generated("com.robohorse.robopojogenerator")
data class Develop(

        @field:SerializedName("totalItems")
        val totalItems: Int? = null,

        @field:SerializedName("data")
        val data: List<DataItem?>? = null,

        @field:SerializedName("totalPages")
        val totalPages: Int? = null,

        @field:SerializedName("pageSize")
        val pageSize: Int? = null
)