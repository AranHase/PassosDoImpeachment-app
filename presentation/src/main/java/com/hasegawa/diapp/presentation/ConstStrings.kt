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

package com.hasegawa.diapp.presentation

abstract class ConstStrings {
    abstract var syncDone: String

    abstract var appPlayUrl: String

    abstract var stepsToolbarShrunkTitle: String
    abstract var newsToolbarShrunkTitle: String

    abstract var navFeedbackUrl: String
    abstract var navOpenSourceUrl: String
    abstract var navSyncNeverUpdated: String

    abstract var creditsEmailChooserTitle: String
    abstract var creditsHaseEmail: String
    abstract var creditsHaseEmailSubject: String
    abstract var creditsHaseGitHubUrl: String

    abstract fun stepDetailShareBody(position: Int, total: Int, completed: Boolean,
                                     date: String, stepTitle: String): String
    abstract var stepDetailShareHeader: String
    abstract var stepDetailShareStateCompleted: String
    abstract var stepDetailShareStateIncomplete: String
}
