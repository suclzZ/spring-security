package com.sucl.security;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.security.PermitAll;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sucl
 * @since 2019/7/5
 */
@Controller
@RequestMapping
public class PageController {

    @RequestMapping("/admin/login")
    public String login(){
        return "login";
    }

    @RequestMapping("/admin/index")
    public String index(){
        return "index";
    }

    @ResponseBody
    @RequestMapping("/admin/student")
    public List<Student> Student(){
        List<Student > students = new ArrayList<>();
        students.add(Student.build().setName("张三").setNo("001"));
        students.add(Student.build().setName("李四").setNo("002"));
        return students;
    }

    @ResponseBody
    @RequestMapping("/page/student")
    public List<Student> Student2(){
        List<Student > students = new ArrayList<>();
        students.add(Student.build().setName("tom").setNo("003"));
        students.add(Student.build().setName("jack").setNo("004"));
        return students;
    }

    @ResponseBody
    @RequestMapping("/access/data")
    public String access(){
        return "access";
    }

    @PreAuthorize("hasPermission('','remove')")
    @ResponseBody
    @RequestMapping("/permission/remove")
    public String permission(){
        return "permission/remove";
    }


    @RequestMapping("/page/login")
    public String pagelogin(){
        return "login";
    }

    @RequestMapping("/page/index")
    public String pageindex(){
        return "index";
    }

    @Data
    @Accessors(chain = true)
    static class Student{
        private String name;
        private String no;

        public static Student build(){
            return new Student();
        }
    }


}
