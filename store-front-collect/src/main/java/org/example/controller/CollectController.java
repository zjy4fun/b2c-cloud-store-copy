package org.example.controller;

import org.example.param.CollectParam;
import org.example.service.CollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("collect")
public class CollectController {

    @Autowired
    private CollectService collectService;

    @PostMapping("save")
    public Object save(@RequestBody CollectParam collectParam){
        return collectService.save(collectParam);
    }

    @PostMapping("list")
    public Object list(@RequestBody CollectParam collectParam){
        return collectService.list(collectParam);
    }
}
