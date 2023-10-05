package org.quizstorage.services;

import org.quizstorage.generator.dto.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MockQuizStorageApiService implements QuizStorageApiService {

    @Override
    public List<QuizSourceDto> getSourcesList() {
        return List.of(
                new QuizSourceDto("1", "Source 1", "description"),
                new QuizSourceDto("2", "Source 2", "description 2")
        );
    }

    @Override
    public List<InitField<?>> getInitFields(String sourceId) {
        return List.of(
                FieldType.SELECT.createInitField("multiSelect", "Choose a category", false,
                        new SelectFormat(true, List.of(new SelectOption("1", "option1"), new SelectOption("2", "option2")))
                ),
                FieldType.SELECT.createInitField("simpleSelect", "Select an option", false,
                        new SelectFormat(false, List.of(new SelectOption("1", "option1"), new SelectOption("2", "option2")))
                ),
                FieldType.NUMBER.createInitField("number1", "fill number", false, new NumberFormat(10, 5)),
                FieldType.NUMBER.createInitField("number2", "fill number", false, null)
        );
    }

}
