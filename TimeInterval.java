package calendar;

import java.time.LocalTime;

public class TimeInterval {

    private final LocalTime startTime, endTime;

    public TimeInterval(
            LocalTime startTime,
            LocalTime endTime
    ) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public boolean hasOverlapWith(TimeInterval that) {
        LocalTime startTime1 = this.getStartTime();
        LocalTime endTime1 = this.getEndTime();
        LocalTime startTime2 = that.getStartTime();
        LocalTime endTime2 = that.getEndTime();

        return (startTime1.isBefore(endTime2) && endTime1.isAfter(startTime2))
                || (startTime2.isBefore(endTime1) && endTime2.isAfter(startTime1));
    }
}
