package calendar;

import javax.swing.event.ChangeListener;

public interface CalendarView extends ChangeListener {

    void next();

    void previous();
}
