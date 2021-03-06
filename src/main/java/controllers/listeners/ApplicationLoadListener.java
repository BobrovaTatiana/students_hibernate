package controllers.listeners;

import common.utils.LectionNotificator;
import models.pojo.Lection;
import org.apache.log4j.Logger;
import services.LectionService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;

/**
 * Created by smoldyrev on 24.02.17.
 */
//начало работы сайта
public class ApplicationLoadListener implements ServletContextListener {

    private static Logger logger = Logger.getLogger(ApplicationLoadListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
//        logger.trace("site was started");
        notifyByNearestLection();

        Thread th1 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(60 * 60 * 1000);
                        notifyByNearestLection();
                    } catch (InterruptedException e) {
                        logger.error(e);
                    }
                }
            }
        });

        th1.start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.trace("site was stopped");
    }

    public void notifyByNearestLection() {
        List<Lection> lections = LectionService.getNearedLection();
        if (lections.size() > 0) {
            logger.trace("lections founded");
            for (Lection lection :
                    lections) {
                LectionNotificator.notifyByLection(lection);
            }
        } else {
            logger.trace("neared lections not found");
        }
    }
}
