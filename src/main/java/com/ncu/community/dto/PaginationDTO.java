package com.ncu.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PaginationDTO {
    private List<QuestionDTO> questions;
    private boolean showPrevious;
    private boolean showFirstPage;
    private boolean showNext;
    private boolean showEndPage;

    private Integer page;
    private Integer totalPage = 0;
    private List<Integer> pages = new ArrayList<>();

    public void setPagination(Integer totalCount, Integer page, Integer size) {

        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }
        this.page = page;

        pages.add(page);
        for (int i = 1; i < 4; i++) {
            if (page + i <= totalPage) {       //加后面的数字
                pages.add(page + i);
            }
            if (page - i > 0) {          //加前面的数字
                pages.add(0, page - i);
            }
        }


        //是否展示上一页
        showPrevious = (page != 1);

        //是否展示下一页
        showNext = (!page.equals(totalPage));

        //是否展示第一页
        showFirstPage = !pages.contains(1);

        //是否展示最后一页
        showEndPage = !pages.contains(totalPage);
    }
}
