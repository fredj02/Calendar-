package calendar;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.util.TreeSet;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;

public class DayView extends JPanel implements CalendarView {

    private final CalendarViewModel model;
    private LocalDate now;
    private final TreeSet<Event> events;

    public DayView(CalendarViewModel m) {
        this.model = m;
        events = m.getEvents();
        display();
    }

    public void display() {
        this.removeAll();
        this.revalidate();

        now = model.getSelectedDate();
        this.setLayout(new BorderLayout());

        final JLabel dayName = new JLabel(now.getDayOfWeek().name().substring(0, 3), SwingConstants.CENTER);

        JPanel dayPanel = new JPanel();
        dayPanel.setLayout(new GridLayout(4, 1));
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        JLabel dayNumber = new JLabel(Integer.toString(now.getDayOfMonth()), SwingConstants.CENTER);

        setBorder(new EmptyBorder(10, 15, 10, 10));

        c.gridx = 0;
        c.gridy = 0;
        dayPanel.add(new JLabel(now.getMonth().name() + " " + now.getDayOfMonth() + ", " + now.getYear()), c);

        c.gridx = 0;
        c.gridy = 1;

        dayName.setHorizontalAlignment(JLabel.LEFT);
        dayPanel.add(dayName, c);

        c.gridx = 0;
        c.gridy = 2;

        dayNumber.setHorizontalAlignment(JLabel.LEFT);
        dayPanel.add(dayNumber, c);

        this.add(dayPanel, BorderLayout.NORTH);

        this.add(displayEvents(), BorderLayout.CENTER);
    }

    public JTextArea displayEvents() {
        final LocalDate startDate = LocalDate.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth());
        final JTextArea eventsLabel = new JTextArea();
        final TreeSet<LocalDate> dates = new TreeSet<LocalDate>();
        LocalDate current = startDate;
        while (current.equals(startDate)) {
            dates.add(current);
            current = current.plusDays(1);
        }

        final StringBuilder eventDisplay = new StringBuilder();
        for (LocalDate day : dates) {
            for (Event e : events) {
                if (e.occursOn(day)) {
                    eventDisplay.append(e.toShortString()).append("\n");
                }
            }
        }
        eventsLabel.setText(eventDisplay.toString());

        return eventsLabel;
    }

    public void stateChanged(ChangeEvent arg0) {
        this.display();
    }

    public void next() {
        model.plusDays(1);
    }

    public void previous() {
        model.plusDays(-1);
    }
}
