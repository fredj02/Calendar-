package calendar;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Event implements Comparable<Event> {

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("M/d/yyyy");
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");
    private final String dowOrder = "MTWHFAS";

    private final String name;
    private final LocalDate date;
    private final TimeInterval timeInterval;
    private final boolean isRecurring;

    private ArrayList<DayOfWeek> days;
    private LocalDate startDate, endDate;

    /**
     * Recurring
     */
    public Event(String name, String days, LocalTime startTime, LocalTime endTime, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.date = startDate;

        this.days = new ArrayList<>();
        char[] daysChar = days.toUpperCase().toCharArray();
        for (char c : daysChar) {
            this.days.add(DayOfWeek.of(dowOrder.indexOf(c) + 1));
        }

        this.timeInterval = new TimeInterval(startTime, endTime);
        this.startDate = startDate;
        this.endDate = endDate;
        this.isRecurring = true;
    }

    /**
     * One time
     */
    public Event(String name, LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.name = name;
        this.date = date;
        this.timeInterval = new TimeInterval(startTime, endTime);
        this.isRecurring = false;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public boolean occursOn(LocalDate date) {
        if (!this.isRecurring && this.date.equals(date)) {
            return true;
        } else {
            return this.isRecurring()
                    && !this.getStartDate().isAfter(date)
                    && !this.getEndDate().isBefore(date)
                    && this.getDays().contains(date.getDayOfWeek());
        }
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public ArrayList<DayOfWeek> getDays() {
        return days;
    }

    public boolean isRecurring() {
        return isRecurring;
    }

    public boolean hasOverlapWith(Event that) {
        return this.occursOn(that.getDate()) && this.timeInterval.hasOverlapWith(that.timeInterval);
    }

    public int compareTo(Event that) {
        final LocalDateTime startTime1 = LocalDateTime.of(this.date, this.timeInterval.getStartTime());
        final LocalDateTime startTime2 = LocalDateTime.of(that.date, that.timeInterval.getStartTime());
        if (startTime1.compareTo(startTime2) != 0) {
            return startTime1.compareTo(startTime2);
        }
        return this.getName().compareTo(that.getName());
    }

    public String toString() {
        if (!this.isRecurring()) {
            return String.format(
                    "%s \n%s %s %s",
                    this.name,
                    this.date.format(dateFormatter),
                    this.timeInterval.getStartTime().format(timeFormatter),
                    this.timeInterval.getEndTime().format(timeFormatter)
            );
        } else {
            final StringBuilder daysAbbrev = new StringBuilder();
            for (DayOfWeek d : days) {
                daysAbbrev.append(dowOrder.charAt(d.getValue() - 1));
            }
            return String.format(
                    "%s \n%s %s %s %s %s",
                    this.name,
                    daysAbbrev.toString(),
                    this.timeInterval.getStartTime().format(timeFormatter),
                    this.timeInterval.getEndTime().format(timeFormatter),
                    this.startDate.format(dateFormatter),
                    this.endDate.format(dateFormatter)
            );
        }
    }

    public String toShortString() {
        return String.format(
                "%s - %s: %s",
                this.timeInterval.getStartTime().format(timeFormatter),
                this.timeInterval.getEndTime().format(timeFormatter),
                this.name
        );
    }
}
