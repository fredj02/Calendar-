package calendar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.util.TreeSet;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;

public class WeekView extends JPanel implements CalendarView {

    private final CalendarViewModel model;
    private final TreeSet<Event> events;

    public WeekView(CalendarViewModel m) {
        this.model = m;
        events = m.getEvents();
        display();
    }

    public void display() {
        this.removeAll();
        this.revalidate();
        LocalDate now = model.getSelectedDate();

        this.setLayout(new BorderLayout());

        final String currentMonth = now.getMonth().name();
        final LocalDate sunday = giveSundayLocalDate();

        JLabel dayTitle;
        if (monthCheck(sunday, now) == 1) {
            dayTitle = new JLabel(currentMonth.substring(0, 3) + " - " + now.plusMonths(1).getMonth().name().substring(0, 3) + " " + now.plusMonths(1).getYear());
        } else if (monthCheck(sunday, now) == -1) {
            dayTitle = new JLabel(now.minusMonths(1).getMonth().name().substring(0, 3) + " - " + currentMonth.substring(0, 3) + " " + now.getYear());
        } else {
            dayTitle = new JLabel(currentMonth + " " + now.getYear());
        }

        JPanel firstPanel = new JPanel();
        GridBagConstraints c = new GridBagConstraints();
        firstPanel.add(dayTitle);

        JPanel secondPanel = new JPanel();
        secondPanel.setLayout(new GridLayout(2, 7));

        c.gridx = 0;
        c.gridy = 0;

        c.fill = GridBagConstraints.HORIZONTAL;
        this.add(firstPanel, BorderLayout.NORTH);

        c.gridy = 1;
        displayDayView(secondPanel, c, giveSundayLocalDate());
        displayEvents(giveSundayLocalDate(), c, secondPanel);
        this.add(secondPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public int monthCheck(LocalDate sunday, LocalDate now) {
        // if the day exceeds the length of the month and
        if (sunday.getMonthValue() < now.getMonthValue()) {
            return -1;
        }
        if (sunday.getDayOfMonth() + 6 > sunday.lengthOfMonth()) {
            // if Sunday's year is less than the selected date's year
            if (sunday.getYear() < now.getYear()) {
                return -1;
            }
            return 1;
        }
        return 0;
    }

    public LocalDate giveSundayLocalDate() {
        final LocalDate today = model.getSelectedDate();
        switch (today.getDayOfWeek().ordinal()) {
            case 0:
                return today.minusDays(1);
            case 1:
                return today.minusDays(2);
            case 2:
                return today.minusDays(3);
            case 3:
                return today.minusDays(4);
            case 4:
                return today.minusDays(5);
            case 5:
                return today.minusDays(6);
            case 6:
                return today;
        }
        return null;
    }

    public void displayDayView(JPanel secondPanel, GridBagConstraints c, LocalDate daysBeforeToday) {
        this.addDayNumber(daysBeforeToday, secondPanel, c);
    }

    public void addDayNumber(LocalDate day, JPanel secondPanel, GridBagConstraints c) {
        LocalDate today = model.getSelectedDate();
        c.gridy = 0;
        c.fill = GridBagConstraints.CENTER;

        for (int i = 0; i < 7; i++) {
            if (day.getDayOfMonth() == today.getDayOfMonth()) {
                c.gridx = i;

                String weekdayAndDateText = "<html>" + day.getDayOfWeek() + "<br>" + day.getDayOfMonth() + "</html>";
                JLabel todayLabel = new JLabel(weekdayAndDateText);
                todayLabel.setForeground(Color.RED);
                todayLabel.setBackground(Color.BLUE);
                todayLabel.setHorizontalAlignment(SwingConstants.CENTER);
                todayLabel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
                secondPanel.add(todayLabel, c);

                day = day.plusDays(1);
            } else {
                c.gridx = i;

                String weekdayAndDateText = "<html>" + day.getDayOfWeek() + "<br>"
                        + Integer.toString(day.getDayOfMonth()) + "</html>";
                JLabel dayNumberLabel = new JLabel(weekdayAndDateText);
                dayNumberLabel.setHorizontalAlignment(SwingConstants.CENTER);
                dayNumberLabel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
                secondPanel.add(dayNumberLabel, c);
                day = day.plusDays(1);
            }

        }
    }

    public void displayEvents(LocalDate sunday, GridBagConstraints c, JPanel secondPanel) {
        LocalDate startDate = LocalDate.of(sunday.getYear(), sunday.getMonthValue(), sunday.getDayOfMonth());
        LocalDate endDate = startDate.plusDays(7);
        TreeSet<LocalDate> dates = new TreeSet<LocalDate>();
        LocalDate current = startDate;
        c.gridx = 0;
        c.gridy = 1;

        while (!current.equals(endDate)) {
            dates.add(current);
            current = current.plusDays(1);
        }

        String eventDisplay = "";

        int i = 0;

        for (LocalDate day : dates) {
            for (Event e : events) {
                if (e.occursOn(day)) {
                    eventDisplay = eventDisplay + e.toShortString() + "\n";
                }
            }
            c.gridx = i;
            c.fill = GridBagConstraints.BOTH;
            JTextArea myTextArea = new JTextArea(eventDisplay);

            myTextArea.setLineWrap(true);
            myTextArea.setWrapStyleWord(true);
            JScrollPane scroll = new JScrollPane(myTextArea);

            scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
            scroll.setPreferredSize(new Dimension(105, 300));

            secondPanel.add(scroll, c);

            // resets eventDisplay to be empty for the next day
            eventDisplay = "";
            i++;
        }
    }

    public void stateChanged(ChangeEvent arg0) {
        this.display();
    }

    public void next() {
        model.plusWeeks(1);
    }

    public void previous() {
        model.plusWeeks(-1);
    }
}
