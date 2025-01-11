package com.bunbeauty.tiptoplive.shared.domain

import com.bunbeauty.tiptoplive.common.domain.KeyValueStorage
import javax.inject.Inject

class SaveImageUriUseCase @Inject constructor(
    private val keyValueStorage: KeyValueStorage
) {

    suspend operator fun invoke(uri: String) {
        keyValueStorage.saveImageUri(uri = uri)
    }

}