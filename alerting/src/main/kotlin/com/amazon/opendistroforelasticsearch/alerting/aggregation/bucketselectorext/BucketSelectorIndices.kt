/*
 *   Copyright 2021 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License").
 *   You may not use this file except in compliance with the License.
 *   A copy of the License is located at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   or in the "license" file accompanying this file. This file is distributed
 *   on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 *   express or implied. See the License for the specific language governing
 *   permissions and limitations under the License.
 */
package com.amazon.opendistroforelasticsearch.alerting.aggregation.bucketselectorext

import org.elasticsearch.common.io.stream.StreamOutput
import org.elasticsearch.common.xcontent.ToXContent.Params
import org.elasticsearch.common.xcontent.XContentBuilder
import org.elasticsearch.search.aggregations.InternalAggregation
import java.io.IOException
import java.util.Objects

open class BucketSelectorIndices(
    name: String?,
    private var parentBucketPath: String,
    var bucketIndices: List<Int?>,
    metaData: Map<String?, Any?>?
) : InternalAggregation(name, metaData) {

    @Throws(IOException::class)
    override fun doWriteTo(out: StreamOutput) {
        out.writeString(parentBucketPath)
        out.writeIntArray(bucketIndices.stream().mapToInt { i: Int? -> i!! }.toArray())
    }

    override fun getWriteableName(): String {
        return name
    }

    override fun reduce(aggregations: List<InternalAggregation>, reduceContext: ReduceContext): BucketSelectorIndices {
        throw UnsupportedOperationException("Not supported")
    }

    override fun mustReduceOnSingleInternalAgg(): Boolean {
        return false
    }

    override fun getProperty(path: MutableList<String>?): Any {
        throw UnsupportedOperationException("Not supported")
    }

    internal object Fields {
        const val PARENT_BUCKET_PATH = "parent_bucket_path"
        const val BUCKET_INDICES = "bucket_indices"
    }

    @Throws(IOException::class)
    override fun doXContentBody(builder: XContentBuilder, params: Params): XContentBuilder {
        builder.field(Fields.PARENT_BUCKET_PATH, parentBucketPath)
        builder.field(Fields.BUCKET_INDICES, bucketIndices)
        otherStatsToXContent(builder)
        return builder
    }

    @Throws(IOException::class)
    protected fun otherStatsToXContent(builder: XContentBuilder): XContentBuilder {
        return builder
    }

    override fun hashCode(): Int {
        return Objects.hash(super.hashCode(), parentBucketPath)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        if (!super.equals(other)) return false
        val otherCast = other as BucketSelectorIndices
        return name == otherCast.name && parentBucketPath == otherCast.parentBucketPath
    }
}