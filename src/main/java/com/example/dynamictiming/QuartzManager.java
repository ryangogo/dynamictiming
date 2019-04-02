package com.example.dynamictiming;

import org.quartz.*;
import org.quartz.impl.DirectSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;

public class QuartzManager {

    private static final Logger LOG = LoggerFactory.getLogger(QuartzManager.class);
    // private static SchedulerFactory schedulerFactory = new StdSchedulerFactory();
    private static DirectSchedulerFactory schedulerFactory = DirectSchedulerFactory.getInstance();

    static {
        try {
            schedulerFactory.createVolatileScheduler(1);
        } catch (SchedulerException e) {
            LOG.info("创建DirectSchedulerFactory的Scheduler失败！", e);
        }
    }

    /**
     * 判断一个job是否存在
     *
     * @param jobName      任务名
     * @param jobGroupName 任务组名
     * @return
     */
    public static boolean isExistJob(String jobName, String jobGroupName) {
        boolean exist = false;
        try {

            Scheduler sched = schedulerFactory.getScheduler();
            JobKey jobKey = new JobKey(jobName, jobGroupName);
            exist = sched.checkExists(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        if (exist) {
            System.out.println("触发器[" + jobName + "]重复");
        } else {
            System.out.println("触发器[" + jobName + "]可用");
        }
        return exist;

    }

    /**
     * @param jobName          任务名
     * @param jobGroupName     任务组名
     * @param triggerName      触发器名
     * @param triggerGroupName 触发器组名
     * @param jobClass         任务
     * @param cron             时间设置，参考quartz说明文档
     * @Description: 添加一个定时任务
     */
    @SuppressWarnings("unchecked")
    public static void addJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName,
                              @SuppressWarnings("rawtypes") Class jobClass, JobDataMap jMap, String cron) {
        try {
            Scheduler sched = schedulerFactory.getScheduler();
            // 任务名，任务组，任务执行类
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroupName).usingJobData(jMap)
                    .build();

            // 触发器
            TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
            // 触发器名,触发器组
            triggerBuilder.withIdentity(triggerName, triggerGroupName);
            triggerBuilder.startNow();
            // 触发器时间设定
            triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron));
            // 创建Trigger对象
            CronTrigger trigger = (CronTrigger) triggerBuilder.build();

            // 调度容器设置JobDetail和Trigger
            sched.scheduleJob(jobDetail, trigger);
            Trigger.TriggerState triggerState = sched.getTriggerState(trigger.getKey());
            // |-NONE 无
            // |-NORMAL 正常状态
            // |-PAUSED 暂停状态
            // |-COMPLETE 完成
            // |-ERROR 错误
            // |-BLOCKED 堵塞

            LOG.info("JobName：" + jobName + ",状态:" + triggerState + ",GroupName:" + jobGroupName);
            // 启动
            if (!sched.isShutdown()) {
                sched.start();
            }
            Thread.sleep(10000);
            // 按新的trigger重新设置job执行
            sched.rescheduleJob(trigger.getKey(), trigger);
        } catch (Exception e) {
            LOG.error("添加一个定时任务发生异常：", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * @param jobName
     * @param jobGroupName
     * @param triggerName      触发器名
     * @param triggerGroupName 触发器组名
     * @param cron             时间设置，参考quartz说明文档
     * @Description: 修改一个任务的触发时间
     */
    public static void modifyJobTime(String jobName, String jobGroupName, String triggerName, String triggerGroupName,
                                     @SuppressWarnings("rawtypes") Class jobClass, JobDataMap jMap, String cron) {
        /** 方式一 ：调用 rescheduleJob 开始 */
        // 触发器
        // TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
        // 触发器名,触发器组
        // triggerBuilder.withIdentity(triggerName, triggerGroupName);
        // triggerBuilder.startNow();
        // 触发器时间设定
        // triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron));
        // 创建Trigger对象
        // trigger = (CronTrigger) triggerBuilder.build();
        // 方式一 ：修改一个任务的触发时间
        // sched.rescheduleJob(triggerKey, trigger);
        /** 方式一 ：调用 rescheduleJob 结束 */

        /** 方式二：先删除，然后在创建一个新的Job */
        removeJob(jobName, jobGroupName, triggerName, triggerGroupName);
        addJob(jobName, jobGroupName, triggerName, triggerGroupName, jobClass, jMap, cron);
        /** 方式二 ：先删除，然后在创建一个新的Job */
    }

    /**
     * @param jobName
     * @param jobGroupName
     * @param triggerName
     * @param triggerGroupName
     * @Description: 移除一个任务
     */
    public static void removeJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName) {
        try {
            Scheduler sched = schedulerFactory.getScheduler();

            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);

            sched.pauseTrigger(triggerKey);// 停止触发器
            sched.unscheduleJob(triggerKey);// 移除触发器
            sched.deleteJob(JobKey.jobKey(jobName, jobGroupName));// 删除任务

            List<String> jobGroupNames = sched.getJobGroupNames();
            LOG.info("任务组开始-->groupsNames=[");
            for (String string : jobGroupNames) {
                GroupMatcher<JobKey> matcher = GroupMatcher.jobGroupEquals(string);
                Set<JobKey> jobKeys = sched.getJobKeys(matcher);
                LOG.info(string + "下的JOB为[");
                for (JobKey jobKey : jobKeys) {
                    LOG.info(jobKey.getName() + ",");
                }
                LOG.info("]");

            }
            LOG.info("]任务组结束。");
        } catch (Exception e) {
            LOG.error("移除job任务发生异常：", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * @Description:启动所有定时任务
     */
    public static void startJobs() {
        try {
            Scheduler sched = schedulerFactory.getScheduler();
            sched.start();
        } catch (Exception e) {
            LOG.error("启动所有定时任务发生异常：", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * @Description:关闭所有定时任务
     */
    public static void shutdownJobs() {
        try {
            Scheduler sched = schedulerFactory.getScheduler();
            if (!sched.isShutdown()) {
                sched.shutdown();
            }
        } catch (Exception e) {
            LOG.error("关闭所有定时任务发生异常：", e);
            throw new RuntimeException(e);
        }
    }

}
