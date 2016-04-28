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

package com.hasegawa.diapp.domain

import com.hasegawa.diapp.domain.devices.SyncScheduler
import com.hasegawa.diapp.domain.entities.SyncEntity
import com.hasegawa.diapp.domain.repositories.SyncsRepository
import com.hasegawa.diapp.domain.usecases.AddPendingSyncUseCase
import org.hamcrest.Matchers.*
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import rx.Observable
import rx.Subscriber
import rx.schedulers.Schedulers
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class AddPendingSyncUseCaseTest {
    @Mock
    var syncsRepository: SyncsRepository? = null

    @Mock
    var syncScheduler: SyncScheduler? = null

    val pendingSync = SyncEntity(null, true, null, null)
    val pendingSyncRet = SyncEntity("A", true, 10, 10)

    init {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun addPendingSync() {
        `when`(syncsRepository!!.upsertSync(pendingSync))
                .thenReturn(Observable.just(pendingSyncRet))

        val useCase = AddPendingSyncUseCase(syncScheduler!!, syncsRepository!!,
                Schedulers.io(), Schedulers.newThread())

        var result: SyncEntity? = null
        val lock = CountDownLatch(1)
        useCase.execute(object : Subscriber<SyncEntity?>() {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
            }

            override fun onNext(t: SyncEntity?) {
                result = t
                lock.countDown()
            }
        })

        lock.await(10, TimeUnit.SECONDS)
        assertThat(result, `is`(pendingSyncRet))

        verify(syncScheduler!!).enqueueSync(true)
        verifyNoMoreInteractions(syncScheduler!!)
        verify(syncsRepository!!).upsertSync(pendingSync)
        verify(syncsRepository!!).notifyChange()
        verifyNoMoreInteractions(syncsRepository!!)
    }
}