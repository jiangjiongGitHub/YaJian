package yj.yajian.scheduled;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import yj.yajian.db.service.FileDatabaseService;
import yj.yajian.dbtemp.TempFileDatabaseService;
import yj.yajian.photo.controller.FileUploadController;

@Slf4j
@Service
public class ScheduledServer {

    private static Long RUN_COUNT = 0L;

    @Autowired
    private FileDatabaseService fileDatabaseService;

    @Autowired
    private TempFileDatabaseService tempFileDatabaseService;

    @Autowired
    private FileUploadController fileUploadController;


    @Scheduled(initialDelay = 30000, fixedDelay = 60000)
    public void schedule() {
        if (RUN_COUNT % 2 == 1)
            fileDatabaseService.put("DATE", DateUtil.today());

        if (RUN_COUNT % 4 == 1)
            tempFileDatabaseService.autoKeepAlive();

        if (RUN_COUNT % 4 == 1)
            tempFileDatabaseService.autoReadTemp();

        if (RUN_COUNT % 10 == 1)
            fileDatabaseService.autoSave();

        if (RUN_COUNT % 10 == 1)
            fileUploadController.autoDelete();

        RUN_COUNT++;
    }


}
