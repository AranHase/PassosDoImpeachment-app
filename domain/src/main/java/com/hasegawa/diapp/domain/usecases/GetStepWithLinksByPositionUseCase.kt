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

package com.hasegawa.diapp.domain.usecases

import com.hasegawa.diapp.domain.ExecutionThread
import com.hasegawa.diapp.domain.PostExecutionThread
import com.hasegawa.diapp.domain.entities.StepWithLinksEntity
import com.hasegawa.diapp.domain.repositories.StepsRepository
import rx.Observable
import rx.subjects.BehaviorSubject

class GetStepWithLinksByPositionUseCase(position: Int,
                                        val stepsRepository: StepsRepository,
                                        executionThread: ExecutionThread,
                                        postExecutionThread: PostExecutionThread) :
        UseCase<StepWithLinksEntity>(executionThread, postExecutionThread) {

    val subject: BehaviorSubject<Int>?
    var position: Int = position
        set(value) {
            field = value
            subject?.onNext(position)
        }

    init {
        subject = BehaviorSubject.create(position)
        this.position = position
    }

    override fun buildUseCaseObservable(): Observable<StepWithLinksEntity> {
        return subject!!.flatMap {
            stepsRepository.getStepByPosition(it)
                    .zipWith(stepsRepository.getStepLinksByStepPosition(it), { step, links ->
                        StepWithLinksEntity(step, links)
                    })
        }
    }
}