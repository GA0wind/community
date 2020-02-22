package com.ncu.community.mapper;

import com.ncu.community.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuestionMapper {

    @Insert("insert into question (title,description,gmt_create,gmt_modified,creator,tag) values (#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{tag})")
    void create(Question question);

    @Select("select * from question limit #{offset},#{size}")
    List<Question> list(@Param("offset") Integer offset,
                        @Param("size") Integer size);       //对象可以不要Param, 直接映射即可, Param里的字符对应#{}里的字符

    @Select("select count(1) from question")
    Integer count();

    @Select("select * from question where creator = #{userId}limit #{offset},#{size}")
    List<Question> listByUserId(@Param("userId") Integer userId,
                        @Param("offset") Integer offset,
                        @Param("size") Integer size);

    @Select("select count(1) from question where creator = #{userId}")
    Integer countByUserId(@Param("userId") Integer userId);

    @Select("select * from question where id = #{id}")
    Question getById(@Param("id")Integer id);

    @Update("update question set title = #{title}, description = #{description}, tag = #{tag}, gmt_modified = #{gmtModified} where id = #{id}")
    void update(Question question);
}
