package com.example.dynamictiming.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;


/**
 * describe：
 * Created with IDEA
 * author:ryan
 * Date:2019/3/11
 * Time:下午3:53
 */
@Controller("/test/")
public class TestController {

    private static String LIVE_JOB_GROUP_NAME = "EXTJWEB_JOBGROUP_COURSELIVE";
    private static String LIVE_TRIGGER_GROUP_NAME = "EXTJWEB_TRIGGERGROUP_COURSELIVE";
    // 赋值参数-公用

    private static final Logger log = LoggerFactory.getLogger(TestController.class);


    public void startTiming() {
//        // 赋值参数-公用
//        JobDataMap jobDataMap = new JobDataMap();
//        jobDataMap.put("startTime", courseScheduleSetDe.getStartTime());
//        jobDataMap.put("endTime", courseScheduleSetDe.getEndTime());
//
//        // 直播触发器
//        String liveJobName = "LIVE_" + courseScheduleSetDe.getStartTime() + "-" +courseScheduleSetDe.getEndTime();
//        // 晚上11点30触发
//        String liveQuartzTime = "0 30 23 * * ?";
//        // 触发器不存在进行触发器保存
//        if (!QuartzManager.isExistJob(liveJobName, LIVE_JOB_GROUP_NAME)) {
//            QuartzManager.addJob(liveJobName, LIVE_JOB_GROUP_NAME, liveJobName, LIVE_TRIGGER_GROUP_NAME,CourseLiveQuartzJob.class, jobDataMap, liveQuartzTime);
//            log.info("创建直播[" + liveJobName + "]触发器,执行频率[" + liveQuartzTime + "]");
//        }
    }


}
