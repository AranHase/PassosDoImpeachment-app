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

package com.hasegawa.diapp.presentation.mvps

import com.hasegawa.diapp.domain.usecases.NumCompletedAndTotal
import com.hasegawa.diapp.presentation.presenters.BasePresenter

object ListStepDetailsMvp {
    interface View {
        fun renderStepCompleted(completed: Boolean)
        fun renderStepsByPosition(positions: List<Int>)
        fun renderNumSteps(numbers: NumCompletedAndTotal)
        fun renderStepPosition(position: Int)
    }

    abstract class Presenter : BasePresenter<View>() {
        abstract fun handleShareBtTouch(position: Int)

        /** Called when the user actively changes the current Step selection.
         *
         * @param[position] Step's position.
         */
        abstract fun handleCurrentStepChange(position: Int)
    }
}
