package com.bunbeauty.tiptoplive.shared.domain

import com.bunbeauty.tiptoplive.common.domain.KeyValueStorage
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetImageUriFlowUseCase @Inject constructor(
    private val keyValueStorage: KeyValueStorage
) {

    operator fun invoke(): Flow<String?> {
        return keyValueStorage.getImageUriFlow()
    }

}