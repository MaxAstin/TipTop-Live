package com.bunbeauty.tiptoplive.shared.domain

import com.bunbeauty.tiptoplive.common.domain.KeyValueStorage
import javax.inject.Inject

class GetUsernameUseCase @Inject constructor(
    private val keyValueStorage: KeyValueStorage
) {

    suspend operator fun invoke(defaultUsername: String = ""): String {
        return keyValueStorage.getUsername() ?: defaultUsername
    }

}