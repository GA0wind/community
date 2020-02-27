package com.ncu.community.controller;

import com.ncu.community.dto.CommentCreateDTO;
import com.ncu.community.dto.CommentDTO;
import com.ncu.community.dto.QuestionDTO;
import com.ncu.community.service.CommentService;
import com.ncu.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {


    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") Long id,
                           Model model) {
        QuestionDTO questionDTO = questionService.getById(id);
        //累加阅读数

        List<CommentDTO> commentDTOList = commentService.listByQuestionId(id);

        questionService.incView(id);

        model.addAttribute("question", questionDTO);
        model.addAttribute("comments",commentDTOList);
        return "question";
    }

}

