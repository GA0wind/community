package com.ncu.community.mapper;

import com.ncu.community.model.Question;

public interface QuestionExtMapper {
    int incView(Question question);

    int incCommentCount(Question question);
}