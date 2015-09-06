package com.dotnar.mongo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author chovans on 15/8/24.
 */
@Service
@EnableScheduling
public class BackupService {

    @Value("${db.backup.source.dir}")
    private String mongoSrouce;
    @Value("${db.backup.dir}")
    private String backupDir;
    @Value("${db.backup.host}")
    private String host;
    @Value("${db.backup.port}")
    private String port;

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH");

    /**
     * 定时备份 单位：ms
     * 3小时备份一次
     */
    @Scheduled(fixedRate = 10800000)
    public void backup() {
        Process process = null;
        try {
            String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "shell" + File.separator + "backupMongo.sh";
            String tempBackupDir = backupDir+File.separator+sdf.format(new Date());
            process = Runtime.getRuntime().exec("sh " + basePath + " " + mongoSrouce + " " + host + " " + port + " " + tempBackupDir);
            process.waitFor();
            System.out.println("==== 备份数据库完成，文件："+ tempBackupDir +" ====");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 定时清除
     * 每天凌晨2点清除一次
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void clearTimeoutBackup() {
        Process process = null;
        try {
            String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "shell" + File.separator + "clearBackup.sh";
            process = Runtime.getRuntime().exec("sh " + basePath + " " + backupDir);
            process.waitFor();
            System.out.println("==== 清理数据库文件 ====");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
