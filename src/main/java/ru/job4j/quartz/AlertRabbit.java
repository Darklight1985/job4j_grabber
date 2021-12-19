package ru.job4j.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;

public class AlertRabbit {
    public static void main(String[] args) {
        File file = new File("./src/main/resources/rabbit.properties");
        Properties properties = new Properties();
        String driver;
        String url;
        String login;
        String password;
        try {
            properties.load(new FileReader(file));
            driver = properties.getProperty("jdbc.driver");
            url = properties.getProperty("jdbc.url");
            login = properties.getProperty("jdbc.username");
            password = properties.getProperty("jdbc.password");
            Class.forName(driver);
           try (Connection connection = DriverManager.getConnection(url, login, password)) {
               properties.load(new FileReader(file));
               Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
               scheduler.start();
               JobDataMap data = new JobDataMap();
               data.put("connect", connection);
               JobDetail job = newJob(Rabbit.class)
                       .usingJobData(data)
                       .build();
               SimpleScheduleBuilder times = simpleSchedule()
                       .withIntervalInSeconds(5)
                       .repeatForever();
               Trigger trigger = newTrigger()
                       .startNow()
                       .withSchedule(times)
                       .build();
               scheduler.scheduleJob(job, trigger);
               Thread.sleep(10000);
               scheduler.shutdown();
           }
        } catch (Exception se) {
            se.printStackTrace();
        }
    }

    public static class Rabbit implements Job {

        public Rabbit() {
            System.out.println(hashCode());
        }

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            System.out.println("Rabbit runs here ...");
            Connection connection =
                    (Connection) context.getJobDetail().getJobDataMap().get("connect");
           try (Statement statement = connection.createStatement()) {
               String sql = "Insert into rabbit(created_date) values ("
                       + System.currentTimeMillis() + ");";
               statement.execute(sql);
           } catch (SQLException throwables) {
               throwables.printStackTrace();
           }
        }
    }
}