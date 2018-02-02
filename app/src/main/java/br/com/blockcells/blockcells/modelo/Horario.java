package br.com.blockcells.blockcells.modelo;

import java.io.Serializable;

/**
 * Created by anderson on 09/12/16.
 */

public class Horario implements Serializable {
    private Long id;
    private boolean usefulMonday;
    private boolean usefulTuesday;
    private boolean usefulWednesday;
    private boolean usefulThursday;
    private boolean usefulFriday;
    private String hourUsefulStart;
    private String hourUsefulEnd;
    private boolean weekendSaturday;
    private boolean weekendSunday;
    private String hourWeekendStart;
    private String hourWeekendEnd;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isUsefulMonday() {
        return usefulMonday;
    }

    public void setUsefulMonday(boolean usefulMonday) {
        this.usefulMonday = usefulMonday;
    }

    public boolean isUsefulTuesday() {
        return usefulTuesday;
    }

    public void setUsefulTuesday(boolean usefulTuesday) {
        this.usefulTuesday = usefulTuesday;
    }

    public boolean isUsefulWednesday() {
        return usefulWednesday;
    }

    public void setUsefulWednesday(boolean usefulWednesday) {
        this.usefulWednesday = usefulWednesday;
    }

    public boolean isUsefulThursday() {
        return usefulThursday;
    }

    public void setUsefulThursday(boolean usefulThursday) {
        this.usefulThursday = usefulThursday;
    }

    public boolean isUsefulFriday() {
        return usefulFriday;
    }

    public void setUsefulFriday(boolean usefulFriday) {
        this.usefulFriday = usefulFriday;
    }

    public String getHourUsefulStart() {
        return hourUsefulStart;
    }

    public void setHourUsefulStart(String hourUsefulStart) {
        this.hourUsefulStart = hourUsefulStart;
    }

    public String getHourUsefulEnd() {
        return hourUsefulEnd;
    }

    public void setHourUsefulEnd(String hourUsefulEnd) {
        this.hourUsefulEnd = hourUsefulEnd;
    }

    public boolean isWeekendSaturday() {
        return weekendSaturday;
    }

    public void setWeekendSaturday(boolean weekendSaturday) {
        this.weekendSaturday = weekendSaturday;
    }

    public boolean isWeekendSunday() {
        return weekendSunday;
    }

    public void setWeekendSunday(boolean weekendSunday) {
        this.weekendSunday = weekendSunday;
    }

    public String getHourWeekendStart() {
        return hourWeekendStart;
    }

    public void setHourWeekendStart(String hourWeekendStart) {
        this.hourWeekendStart = hourWeekendStart;
    }

    public String getHourWeekendEnd() {
        return hourWeekendEnd;
    }

    public void setHourWeekendEnd(String hourWeekendEnd) {
        this.hourWeekendEnd = hourWeekendEnd;
    }
}
