package calendar;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.TreeSet;

public class CalendarViewModel {

    private final TreeSet<Event> events = new TreeSet<>();
    private final ArrayList<ChangeListener> listeners = new ArrayList<>();
    private static LocalDate selectedDate = LocalDate.now();

    public TreeSet<Event> getEvents() {
        return events;
    }

    public LocalDate getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(LocalDate date) {
        selectedDate = date;
        update();
    }

    public void plusDays(int i) {
        selectedDate = selectedDate.plusDays(i);
        update();
    }

    public void plusWeeks(int i) {
        selectedDate = selectedDate.plusWeeks(i);
        update();
    }

    public void plusMonths(int i) {
        selectedDate = selectedDate.plusMonths(i).withDayOfMonth(1);
        update();
    }

    public void attachChangeListener(ChangeListener listener) {
        listeners.add(listener);
    }

    private void update() {
        listeners.forEach(l -> l.stateChanged(new ChangeEvent(this)));
    }

    public boolean addEvent(Event e) {
        for (Event i : events) {
            if (i.hasOverlapWith(e)) {
                return false;
            }
        }
        events.add(e);
        update();
        return true;
    }
}
