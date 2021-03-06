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

package com.hasegawa.diapp.di

import com.hasegawa.diapp.activities.MainActivity
import com.hasegawa.diapp.controllers.*
import com.hasegawa.diapp.domain.ExecutionThread
import com.hasegawa.diapp.domain.PostExecutionThread
import com.hasegawa.diapp.domain.devices.LogDevice
import com.hasegawa.diapp.domain.devices.TextSharer
import com.hasegawa.diapp.domain.devices.UrlOpener
import com.hasegawa.diapp.domain.repositories.StepsRepository
import com.hasegawa.diapp.presentation.ConstStrings
import dagger.Component

@PerActivity
@Component(dependencies = arrayOf(AppComponent::class),
        modules = arrayOf(ActivityModule::class))
interface ActivityComponent {
    fun inject(a: MainActivity)
    fun inject(c: ScreenMainController)
    fun inject(c: BaseNavigationController)
    fun inject(c: ListNewsController)
    fun inject(c: ListStepsController)
    fun inject(c: ScreenCreditsController)
    fun inject(c: ScreenStepDetailController)
    fun inject(c: CreditsController)
    fun inject(c: ListStepDetailsController)

    fun getConstStrings(): ConstStrings
    fun getTextSharer(): TextSharer
    fun getLogDevice(): LogDevice
    fun getUrlOpener(): UrlOpener
    fun getStepsRepository(): StepsRepository
    fun getExecutionThread(): ExecutionThread
    fun getPostExecutionThread(): PostExecutionThread
}
