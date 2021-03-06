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

package com.hasegawa.diapp.db.repositories.contentprovider.mappings

import android.content.ContentValues
import android.database.Cursor
import com.hasegawa.diapp.db.repositories.contentprovider.DiContract.GCMRegistrationsContract
import com.hasegawa.diapp.db.utils.getLongByColumnName
import com.hasegawa.diapp.db.utils.getStringByColumnName
import com.hasegawa.diapp.domain.entities.GCMRegistrationEntity
import com.pushtorefresh.storio.contentresolver.ContentResolverTypeMapping
import com.pushtorefresh.storio.contentresolver.operations.delete.DefaultDeleteResolver
import com.pushtorefresh.storio.contentresolver.operations.get.DefaultGetResolver
import com.pushtorefresh.storio.contentresolver.operations.put.DefaultPutResolver
import com.pushtorefresh.storio.contentresolver.queries.DeleteQuery
import com.pushtorefresh.storio.contentresolver.queries.InsertQuery
import com.pushtorefresh.storio.contentresolver.queries.UpdateQuery

object GCMRegistrationEntityMapping {
    fun typeMapping() = ContentResolverTypeMapping.builder<GCMRegistrationEntity>()
            .putResolver(putResolver())
            .getResolver(getResolver())
            .deleteResolver(deleteResolver())
            .build()

    fun getResolver() = object : DefaultGetResolver<GCMRegistrationEntity>() {
        override fun mapFromCursor(c: Cursor): GCMRegistrationEntity {
            return GCMRegistrationEntity(
                    c.getStringByColumnName(GCMRegistrationsContract.COL_TOKEN),
                    c.getLongByColumnName(GCMRegistrationsContract.COL_TIME_CREATED)
            )
        }
    }

    fun putResolver() = object : DefaultPutResolver<GCMRegistrationEntity>() {
        override fun mapToContentValues(s: GCMRegistrationEntity): ContentValues {
            return s.toContentValues()
        }

        override fun mapToUpdateQuery(s: GCMRegistrationEntity): UpdateQuery {
            return UpdateQuery.builder()
                    .uri(GCMRegistrationsContract.URI)
                    .where("${GCMRegistrationsContract.COL_TOKEN} = ?")
                    .whereArgs(s.token)
                    .build()
        }

        override fun mapToInsertQuery(`object`: GCMRegistrationEntity): InsertQuery {
            return InsertQuery.builder()
                    .uri(GCMRegistrationsContract.URI)
                    .build()
        }
    }

    fun deleteResolver() = object : DefaultDeleteResolver<GCMRegistrationEntity>() {
        override fun mapToDeleteQuery(s: GCMRegistrationEntity): DeleteQuery {
            return DeleteQuery.builder()
                    .uri(GCMRegistrationsContract.URI)
                    .where("${GCMRegistrationsContract.COL_TOKEN} = ?")
                    .whereArgs(s.token)
                    .build()
        }
    }
}

fun GCMRegistrationEntity.toContentValues(): ContentValues {
    val c = ContentValues()
    c.put(GCMRegistrationsContract.COL_TOKEN, this.token)
    c.put(GCMRegistrationsContract.COL_TIME_CREATED, this.timeCreated)
    return c
}
