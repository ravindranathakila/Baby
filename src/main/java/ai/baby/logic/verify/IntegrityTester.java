package ai.baby.logic.verify;

import ai.baby.util.Loggers;
import ai.baby.util.management.MemorySafe;
import ai.baby.util.management.MemoryWarningSystem;
import ai.baby.logic.crud.HumanCRUDHumanLocal;
import ai.baby.rbs.RBGet;
import ai.scribble.License;
import ai.scribble._note;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Startup
@Singleton
@_note(note = "USE s.o.p FOR LOGGING AS LOGGER MIGHT FAIL TOO!")
public class IntegrityTester implements IntegrityTesterLocal {

    @EJB
    private MemcLocal memcLocal_;

    @EJB
    private HumanCRUDHumanLocal humanCRUDHumanLocal_;


    public IntegrityTester() {
    }

    @PostConstruct
    public void postConstruct() {
        memcLocal_.logTime();

        MemoryWarningSystem.setPercentageUsageThreshold(Double.parseDouble(RBGet.globalConfig.getString("MEMORY_THRESHOLD")));
        MemoryWarningSystem.setPercentageUsageNormal(Double.parseDouble(RBGet.globalConfig.getString("MEMORY_NORMAL")));
        MemorySafe.allocate(Runtime.getRuntime().freeMemory());

        MemoryWarningSystem mws = new MemoryWarningSystem();
        mws.addListener(new MemoryWarningSystem.MemoryListener() {

            public void memoryUsageLow(long usedMemory, long maxMemory) {
                Loggers.STATUS.warn("MEMORY USAGE TOO HIGH!!!");
                MemorySafe.deallocate();
                Memc.sendAlertMail("MEMORY USAGE TOO HIGH!!!");
            }

            public void memoryUsageNormal(long usedMemory, long maxMemory) {
                Loggers.STATUS.info("Memory usage back to normal.");
                MemorySafe.allocate(maxMemory - usedMemory);
            }
        });
    }

    @Override
    public boolean gc() {
        System.gc();
        return true;
    }
}
