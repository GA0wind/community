package com.ncu.community.service;

import com.ncu.community.dto.CommentDTO;
import com.ncu.community.enums.CommentTypeEnum;
import com.ncu.community.exception.CustomizeErrorCode;
import com.ncu.community.exception.CustomizeException;
import com.ncu.community.mapper.CommentMapper;
import com.ncu.community.mapper.QuestionExtMapper;
import com.ncu.community.mapper.QuestionMapper;
import com.ncu.community.mapper.UserMapper;
import com.ncu.community.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Autowired
    private UserMapper userMapper;

    @Transactional
    public void insert(Comment comment) {
        if(comment.getParentId() == null || comment.getParentId() == 0){
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }

        if(comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())){
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }

        if (comment.getType() == CommentTypeEnum.COMMENT.getType()){
            //回复评论
            Comment dbcomment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if(dbcomment == null){
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            commentMapper.insert(comment);
        }else{
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if(question == null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }

            commentMapper.insert(comment);
            question.setCommentCount(1);
            questionExtMapper.incCommentCount(question);
            //回复问题
        }
    }

    public List<CommentDTO> listByQuestionId(Long id) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria()
                .andParentIdEqualTo(id)
        .andTypeEqualTo(CommentTypeEnum.QUESTION.getType());
        commentExample.setOrderByClause("gmt_create desc");

        List<Comment> commentList = commentMapper.selectByExample(commentExample);

        if(commentList.size() == 0){
            return new ArrayList<>();
        }

        //获取不重复的评论人id
        Set<Long> commentators = commentList.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        List<Long> userIds = new ArrayList<>();
        userIds.addAll(commentators);

        //从数据库中查出User对象
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andIdIn(userIds);
        List<User> userList = userMapper.selectByExample(userExample);

        //将userID和user对象存放在map中, 方便存入DTO中
        Map<Long, User> userMap = userList.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));

        List<CommentDTO> commentDTOList = commentList.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment,commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());

        return commentDTOList;
    }
}
