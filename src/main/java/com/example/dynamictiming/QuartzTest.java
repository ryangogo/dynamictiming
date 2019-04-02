package com.example.dynamictiming;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * describe：
 * Created with IDEA
 * author:ryan
 * Date:2019/3/11
 * Time:下午4:18
 */

public class QuartzTest {

    private static final Logger log = LoggerFactory.getLogger(QuartzTest.class);


    public static void main(String[] args) throws InterruptedException {
        // 创建工厂
        SchedulerFactory schedulerfactory = new StdSchedulerFactory();
        Scheduler scheduler = null;
        try {
            // 通过schedulerFactory获取一个调度器
            scheduler = schedulerfactory.getScheduler();
            // 指明job的名称，所在组的名称，以及绑定job类
            JobDetail job = JobBuilder.newJob(HelloJob.class).
                    withIdentity("JobName", "JobGroupName").
                    usingJobData("name", "quartz").build();
            // 触发器
            TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
            // 触发器名,触发器组
            triggerBuilder.withIdentity("CronTrigger", "CronTriggerGroup");
            triggerBuilder.startNow();

            // 触发器时间设定（通过表达式来定义执行时间）
            triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ?"));
            // 触发器时间设定（直接定义每隔多少秒执行一次）
            //triggerBuilder.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(5).repeatForever());
            // 创建Trigger对象
            Trigger trigger = triggerBuilder.build();


            // 调度容器设置JobDetail和Trigger
            scheduler.scheduleJob(job, trigger);
            Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
            // |-NONE 无
            // |-NORMAL 正常状态
            // |-PAUSED 暂停状态
            // |-COMPLETE 完成
            // |-ERROR 错误
            // |-BLOCKED 堵塞

            log.info("JobName：" + "JobName" + ",状态:" + triggerState + ",GroupName:" + "JobGroupName");

            // 启动
            if (!scheduler.isShutdown()) {
                scheduler.start();
            }
            //Thread.sleep(6000);
            // 停止调度
            //scheduler.shutdown();
            // 按新的trigger(触发器)重新设置job执行
            //scheduler.rescheduleJob(trigger.getKey(), trigger);

        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }


}
