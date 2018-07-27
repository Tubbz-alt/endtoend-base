/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.androidthings.endtoend.companion.auth

import com.google.firebase.auth.FirebaseAuth

/** AuthProvider implementation based on FirebaseAuth. */
object FirebaseAuthProvider: AuthProvider() {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private var authUiHelper: FirebaseAuthUiHelper? = null

    init {
        firebaseAuth.addAuthStateListener {
            _userLiveData.postValue(firebaseAuth.currentUser)
        }
        _userLiveData.postValue(firebaseAuth.currentUser)
    }

    /**
     * Set the FirebaseAuthUiHelper. Intended to be used in [Activity#oncreate]
     * [android.app.Activity.onCreate].
     */
    fun setAuthUiHelper(uiHelper: FirebaseAuthUiHelper) {
        authUiHelper = uiHelper
    }

    /**
     * Unset the FirebaseAuthUiHelper if the argument is the current helper. Intended to be used in
     * [Activity#onDestroy][android.app.Activity.onDestroy].
     */
    fun unsetAuthUiHelper(uiHelper: FirebaseAuthUiHelper) {
        if (authUiHelper == uiHelper) {
            authUiHelper = null
        }
    }

    override fun performSignIn() {
        requireNotNull(authUiHelper).performSignIn()
    }

    override fun performSignOut() {
        requireNotNull(authUiHelper).performSignOut()
    }
}

/** Separates auth UI from FirebaseAuthProvider logic. */
interface FirebaseAuthUiHelper {
    fun performSignIn()
    fun performSignOut()
}
