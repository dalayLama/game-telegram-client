package org.quizstorage.services;

import org.quizstorage.generator.dto.InitField;
import org.quizstorage.generator.dto.QuizSourceDto;

import java.util.List;

public interface QuizStorageApiService {
    List<QuizSourceDto> getSourcesList();

    List<InitField<?>> getInitFields(String sourceId);
}
