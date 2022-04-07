package com.lazyafei.springpractice.controller;

import com.lazyafei.springpractice.service.IPracticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PracticeController {
    @Autowired
    private IPracticeService practiceService;

    @GetMapping("/practice/insert")
    public String insertTest(Integer nums){
        if(nums == null || nums <= 0){
            return "参数有误！";
        }
        return practiceService.insert(nums);
    }

    @GetMapping("/practice/batch_insert")
    public String batchInsertTest(Integer nums){
        if(nums == null || nums <= 0){
            return "参数有误！";
        }
        return practiceService.batchInsert(nums);
    }

}
