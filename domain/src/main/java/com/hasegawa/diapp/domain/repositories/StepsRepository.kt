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

package com.hasegawa.diapp.domain.repositories

import com.hasegawa.diapp.domain.entities.StepEntity
import com.hasegawa.diapp.domain.entities.StepLinkEntity
import rx.Observable

interface StepsRepository {
    fun getSteps(): Observable<List<StepEntity>>
    fun getStepLinks(): Observable<List<StepLinkEntity>>
    fun getStepById(id: String): Observable<StepEntity?>
    fun getStepByPosition(position: Int): Observable<StepEntity?>
    fun getStepLinksByStepId(stepId: String): Observable<List<StepLinkEntity>>
    fun getNumberOfCompletedSteps(): Observable<Int>

    fun addSteps(steps: List<com.hasegawa.diapp.domain.entities.StepEntity>): Observable<List<StepEntity>>
    fun addStepLinks(links: List<com.hasegawa.diapp.domain.entities.StepLinkEntity>): Observable<List<StepLinkEntity>>

    fun clearSteps(): Observable<Int>
    fun clearStepLinks(): Observable<Int>

    fun notifyChange()
}