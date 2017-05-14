package de.julianSauer.minecraftSurvivalEvolved.tribes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TribeLogger {

    private List<String> log;

    private Tribe tribe;

    private DateFormat dateFormat;

    public TribeLogger() {
        log = new ArrayList<>();
        dateFormat = new SimpleDateFormat("MM.dd.yyyy HH:mm:ss");
    }

    public TribeLogger(List<String> log) {
        this.log = log;
    }

    public List<String> getLog() {
        return log;
    }

    /**
     * Should only be called when loading the tribe from disk.
     *
     * @param log
     */
    public void setLog(List<String> log) {
        this.log = log;
    }

    public List<String> getLatestEntries(int entries) {
        int size = log.size();
        if (entries > size)
            entries = size;
        return log.subList(size - entries, size);
    }

    public Tribe getTribe() {
        return tribe;
    }

    public void setTribe(Tribe tribe) {
        this.tribe = tribe;
    }

    public void log(String message) {
        String logEntry = dateFormat.format(new Date()) + " | " + message;
        log.add(logEntry);
    }

}
