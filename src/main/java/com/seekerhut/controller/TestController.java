package com.seekerhut.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping(value = "/api/test")
public class TestController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);
    private static BlockingQueue<Runnable> bq = new LinkedBlockingQueue<>(6);
    private static ThreadPoolExecutor tpe = new ThreadPoolExecutor(5, 8, 0, TimeUnit.SECONDS, bq);

    public TestController() {
    }

    @RequestMapping(value = "base_test", method = RequestMethod.GET)
    public @ResponseBody String baseTest() {
        Integer[] a = new Integer[30];
        Arrays.sort(a, (Integer x1, Integer x2) -> {
            return x1 - x2;
        });
        return Success("");
    }
}
