package com.example.dynamictiming;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * describe：
 * Created with IDEA
 * author:ryan
 * Date:2019/3/11
 * Time:下午4:15
 */

public class HelloJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        JobDetail detail = jobExecutionContext.getJobDetail();
        String name = detail.getJobDataMap().getString("name");
        System.out.println("say hello " + name)
        ;

    }
}
