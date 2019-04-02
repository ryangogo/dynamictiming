package com.example.dynamictiming.Controller;

import com.example.dynamictiming.HelloJob;
import com.example.dynamictiming.QuartzManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * describe：
 * Created with IDEA
 * author:ryan
 * Date:2019/3/11
 * Time:下午3:53
 */
@Controller
@RequestMapping("/test/")
public class TestController {


    private static final String JOB_NAME = "JobName"; //job名称
    private static final String JOB_GROUP_NAME = "JobGroupName"; //job组名称
    private static final String JOB_DATA_KEY = "name"; //定时执行的方法的参数的key
    private static final String JOB_DATA_VALUE = "quartz"; //定时执行的方法的参数的value
    private static final String CRON_TRIGGER = "CronTrigger"; //触发器名
    private static final String CRON_TRIGGER_GROUP = "CronTriggerGroup"; //触发器组名


    /**
     * 添加定时任务接口
     * @return
     */
    @RequestMapping("addTiming")
    @ResponseBody
    public String addTiming() {
        QuartzManager.addJob(JOB_NAME, JOB_GROUP_NAME, CRON_TRIGGER, CRON_TRIGGER_GROUP,
                HelloJob.class, JOB_DATA_KEY,JOB_DATA_VALUE, "0/5 * * * * ?");
        return "添加定时任务成功";
    }

    /**
     * 删除指定定时任务
     * @return
     */
    @RequestMapping("deleteTiming")
    @ResponseBody
    public String deleteTiming() {
        QuartzManager.removeJob(JOB_NAME, JOB_GROUP_NAME, CRON_TRIGGER, CRON_TRIGGER_GROUP);
        return "删除定时任务成功";
    }

    /**
     * 启动所有定时任务
     * @return
     */
//    @RequestMapping("startTiming")
//    @ResponseBody
//    public String startTiming() {
//        QuartzManager.startJobs();
//        return "启动所有定时任务成功";
//    }


    /**
     * 停止所有定时任务
     * @return
     */
    @RequestMapping("endTiming")
    @ResponseBody
    public String endTiming() {
        QuartzManager.shutdownJobs();
        return "停止所有定时任务成功";
    }

}
