package com.otf31.foro_hub.domain.topics.validations

import com.otf31.foro_hub.domain.topics.DataCreateUpdateTopic

interface ValidatorOfTopics {
  fun validate(dataCreateUpdateTopic: DataCreateUpdateTopic)
}
