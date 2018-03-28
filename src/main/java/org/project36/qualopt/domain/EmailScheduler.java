package org.project36.qualopt.domain;

import org.quartz.Job;
import org.quartz.JobKey;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage; 
/**
 * The EmailScheduler class is used to define the execution of the Quartz trigger.
 * In this case, the execution sends an invitation email.
 */
public class EmailScheduler implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException
    {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        MimeMessage msg = (MimeMessage)dataMap.get("message");

        try {
            Transport.send(msg);
        } catch (MessagingException e){
            e.printStackTrace();
        }
    }
  }