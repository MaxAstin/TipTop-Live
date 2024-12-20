package com.bunbeauty.tiptoplive.features.domain

import com.bunbeauty.tiptoplive.common.domain.KeyValueStorage
import javax.inject.Inject

class SaveUsernameUseCase @Inject constructor(
    private val keyValueStorage: KeyValueStorage
) {

    suspend operator fun invoke(username: String) {
        keyValueStorage.saveUsername(username = username)
    }

}