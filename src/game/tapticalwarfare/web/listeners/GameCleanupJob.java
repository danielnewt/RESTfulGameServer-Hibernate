package game.tapticalwarfare.web.listeners;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import game.tapticalwarfare.logger.Log;
import game.tapticalwarfare.workflow.GameWorkflow;

@WebListener
public class GameCleanupJob implements ServletContextListener {

    private ScheduledExecutorService scheduler;
    private final int daysToMillisModifier = 24 * 60 * 60 * 1000;
    private final int gameTimeToExpire = 14 * daysToMillisModifier;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new CleanUp(), 0, 1, TimeUnit.DAYS); //run clean up once a day
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        scheduler.shutdownNow();
    }

    public class CleanUp implements Runnable {
    	@Override
        public void run() {
    		try{
    			Log.logInfo("Running cleanup job");
    			GameWorkflow.RemoveUnusedGamesByAge(gameTimeToExpire);
    		} catch(Exception e){
    			Log.logError("Failed to execute cleanup job! Exception:" + e.getClass());
    			e.printStackTrace();
    		}
        }
    }
}
