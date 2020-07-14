package com.adam.demo.mq;

import com.adam.demo.config.RabbitMQConfig;
import com.adam.demo.entity.XcTask;
import com.adam.demo.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@Component
@Slf4j
public class ChooseCourseTask {

    @Autowired
    private TaskService taskService;

    //定义任务调试策略
//    @Scheduled(cron="0/3 * * * * *")//每隔3秒去执行
//       @Scheduled(fixedRate = 3000) //在任务开始后3秒执行下一次调度
//       @Scheduled(fixedDelay = 3000) //在任务结束后3秒后才开始执行
    public void task1(){
        log.info("===============测试定时任务1开始===============");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("===============测试定时任务1结束===============");

    }


    @Scheduled(cron="0/3 * * * * *")
    //定时发送加选课任务
    public void sendChoosecourseTask() {
        //得到1分钟之前的时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.set(GregorianCalendar.MINUTE,-1);
        Date time = calendar.getTime();
        List<XcTask> xcTaskList = taskService.findXcTaskList(time, 100);
        log.info("===========选课任务=========="+xcTaskList);
        //调用service发布消息，将添加选课的任务发送给mq
        for (XcTask xcTask : xcTaskList) {
            if(taskService.getTask(xcTask.getId(),xcTask.getVersion())>0) {
                //要发送的交换机
                String ex = xcTask.getMqExchange();
                //发送消息要带的routingKey
                String routingKey = xcTask.getMqRoutingkey();
                taskService.publish(xcTask,ex,routingKey);
                log.info("发送已购课程实体ID:{}",xcTask.getId());
            }
        }
    }


    /**
     * 接收完成选 课任务消息并进行处理
     * @param xcTask
     */
    @RabbitListener(queues = RabbitMQConfig.XC_LEARNING_FINISHADDCHOOSECOURSE)
    public void receiveFinishChoosecourseTask(XcTask xcTask){
        if(xcTask!=null && StringUtils.isNotEmpty(xcTask.getId())){
            taskService.finishTask(xcTask.getId());
        }
    }
}
