package com.ncu.community.advice;

import com.alibaba.fastjson.JSON;
import com.ncu.community.dto.ResultDTO;
import com.ncu.community.exception.CustomizeErrorCode;
import com.ncu.community.exception.CustomizeException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@ControllerAdvice
public class CustomizeExceptionHandler {

    @ExceptionHandler(Exception.class)
    ModelAndView handle(HttpServletRequest request,
                  Throwable ex, Model model,
                  HttpServletResponse response){

        String contentType = request.getContentType();
        ex.printStackTrace();
        if("application/json".equals(contentType)){
            //返回json
            ResultDTO resultDTO = null;
            if (ex instanceof CustomizeException){

                resultDTO = ResultDTO.errorOf((CustomizeException) ex);
            } else {
                resultDTO =  ResultDTO.errorOf(CustomizeErrorCode.SYS_ERROR);
            }

            try {
                response.setCharacterEncoding("utf-8");
                response.setContentType("application/json");
                response.setStatus(200);
                PrintWriter writer = response.getWriter();
                writer.write(JSON.toJSONString(resultDTO));
                writer.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            return null;
        } else {
            //跳转错误页面
            if (ex instanceof CustomizeException){
                model.addAttribute("message", ex.getMessage());
                ex.printStackTrace();
            } else {
                model.addAttribute("message",CustomizeErrorCode.SYS_ERROR.getMessage());
                ex.printStackTrace();
            }
            return new ModelAndView("error");
        }
    }




    private HttpStatus getStatus(HttpServletRequest request){
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if(statusCode == null){
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }

}
