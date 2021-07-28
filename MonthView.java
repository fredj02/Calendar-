package calendar;

import java.time.LocalDate;
import java.util.TreeSet;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;

public class MonthView extends JPanel implements CalendarView {

    private final CalendarViewModel model;
    private int lengthOfMonth;
    private boolean sameMonth;
    private boolean sameYear;
    private final TreeSet<Event> events;
    private final String thisMonth;
    private final int thisYear;

    public MonthView(CalendarViewModel m) {
        this.model = m;
        LocalDate cal = m.getSelectedDate();
        lengthOfMonth = cal.lengthOfMonth();
        sameMonth = true;
        sameYear = true;
        thisMonth = cal.getMonth().name();
        thisYear = cal.getYear();
        events = m.getEvents();
        display();
    }

    /**
     * Gives this month
     *
     * @return thisMonth
     */
    public String getThisMonth() {
        return thisMonth;
    }

    /**
     * Gives this year
     *
     * @return thisYear
     */
    public int getThisYear() {
        return thisYear;
    }

    /**
     * Creates the calendar and also updates it as needed.
     * It also shows the different events.
     */
    public void display() {
        this.removeAll();
        this.revalidate();
        // Get today's date and month
        LocalDate cal = model.getSelectedDate();
        int today = LocalDate.now().getDayOfMonth();

        // Creating the title and adding the left and right button for navigation
        for (int i = 0; i < 7; i++) {
            if (i == 0) {
                JLabel month = new JLabel(cal.getMonth().name(), SwingConstants.CENTER);
                add(month);
            } else if (i == 1) {
                JLabel year = new JLabel("" + cal.getYear(), SwingConstants.CENTER);
                add(year);
            } else {
                JLabel empty = new JLabel("");
                add(empty);
            }
        }

        // Show the days on top
        String[] days = {"S", "M", "T", "W", "T", "F", "S"};

        for (int i = 0; i < 7; i++) {
            add(new JLabel(days[i], SwingConstants.CENTER));
        }

        // Creaing the calendar with each date as a button
        JLabel[][] daysHolder = new JLabel[6][7];
        this.setLayout(new GridLayout(8, 7));
        for (int m = 0; m < 6; m++) {
            for (int y = 0; y < 7; y++) {
                daysHolder[m][y] = new JLabel();
                JScrollPane scroller = new JScrollPane(daysHolder[m][y], JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                daysHolder[m][y].setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                add(scroller);
            }
        }

        // First day of the month
        cal = LocalDate.of(cal.getYear(), cal.getMonthValue(), 1);
        String firstDay = cal.getDayOfWeek().name();

        // Where to start for the first day of the month
        int counter = 0;
        if (firstDay.compareTo("MONDAY") == 0) {
            counter = counter + 1;
            daysHolder[0][0].setText("");
        } else if (firstDay.compareTo("TUESDAY") == 0) {
            counter = counter + 2;
            for (int i = 0; i <= 1; i++) {
                daysHolder[0][i].setText("");
            }
        } else if (firstDay.compareTo("WEDNESDAY") == 0) {
            counter = counter + 3;
            for (int i = 0; i <= 2; i++) {
                daysHolder[0][i].setText("");
            }
        } else if (firstDay.compareTo("THURSDAY") == 0) {
            counter = counter + 4;
            for (int i = 0; i <= 3; i++) {
                daysHolder[0][i].setText("");
            }
        } else if (firstDay.compareTo("FRIDAY") == 0) {
            counter = counter + 5;
            for (int i = 0; i <= 4; i++) {
                daysHolder[0][i].setText("");
            }
        } else if (firstDay.compareTo("SATURDAY") == 0) {
            counter = counter + 6;
            for (int i = 0; i <= 5; i++) {
                daysHolder[0][i].setText("");
            }
        }

        // Populating the calendar and identifying today
        int i = 1;
        int week = 0;
        lengthOfMonth = cal.lengthOfMonth();
        sameMonth = (cal.getMonth().name().compareTo(this.getThisMonth()) == 0);
        sameYear = (cal.getYear() == this.getThisYear());

        while (i <= lengthOfMonth) {
            if (i == 1) {
                daysHolder[week][counter].setText(
                        "<html>" + i + "<br>" + getEvents(LocalDate.of(cal.getYear(), cal.getMonth(), i)) + "</html>");
                daysHolder[week][counter].setForeground(Color.RED);
            }
            if (counter < 6 && i < 10 && i != today) {
                daysHolder[week][counter].setText(
                        "<html>" + i + "<br>" + getEvents(LocalDate.of(cal.getYear(), cal.getMonth(), i)) + "</html>");
                counter++;
                i++;
            } else if (counter < 6 && i < 10 && !sameMonth) {
                daysHolder[week][counter].setText(
                        "<html>" + i + "<br>" + getEvents(LocalDate.of(cal.getYear(), cal.getMonth(), i)) + "</html>");
                counter++;
                i++;
            } else if (counter < 6 && i < 10 && sameYear) {
                daysHolder[week][counter].setText(
                        "<html>" + i + "<br>" + getEvents(LocalDate.of(cal.getYear(), cal.getMonth(), i)) + "</html>");
                // today
                daysHolder[week][counter].setForeground(Color.BLUE);
                counter++;
                i++;
            } else if (counter == 6 && i < 10 && i != today) {
                daysHolder[week][counter].setText(
                        "<html>" + i + "<br>" + getEvents(LocalDate.of(cal.getYear(), cal.getMonth(), i)) + "</html>");
                counter = 0;
                i++;
                week++;
            } else if (counter == 6 && i < 10 && !sameMonth) {
                daysHolder[week][counter].setText(
                        "<html>" + i + "<br>" + getEvents(LocalDate.of(cal.getYear(), cal.getMonth(), i)) + "</html>");
                counter = 0;
                i++;
                week++;
            } else if (counter == 6 && i < 10 && !sameYear) {
                daysHolder[week][counter].setText(
                        "<html>" + i + "<br>" + getEvents(LocalDate.of(cal.getYear(), cal.getMonth(), i)) + "</html>");
                counter = 0;
                i++;
                week++;
            } else if (counter == 6 && i < 10) {
                daysHolder[week][counter].setText(
                        "<html>" + i + "<br>" + getEvents(LocalDate.of(cal.getYear(), cal.getMonth(), i)) + "</html>");
                // today
                daysHolder[week][counter].setForeground(Color.BLUE);
                counter = 0;
                i++;
                week++;
            } else if (counter < 6 && i >= 10 && i < lengthOfMonth && i != today) {
                daysHolder[week][counter].setText(
                        "<html>" + i + "<br>" + getEvents(LocalDate.of(cal.getYear(), cal.getMonth(), i)) + "</html>");
                counter++;
                i++;
            } else if (counter < 6 && i >= 10 && i < lengthOfMonth && !sameMonth) {
                daysHolder[week][counter].setText(
                        "<html>" + i + "<br>" + getEvents(LocalDate.of(cal.getYear(), cal.getMonth(), i)) + "</html>");
                counter++;
                i++;
            } else if (counter < 6 && i >= 10 && i < lengthOfMonth && !sameYear) {
                daysHolder[week][counter].setText(
                        "<html>" + i + "<br>" + getEvents(LocalDate.of(cal.getYear(), cal.getMonth(), i)) + "</html>");
                counter++;
                i++;
            } else if (counter < 6 && i >= 10 && i < lengthOfMonth) {
                daysHolder[week][counter].setText(
                        "<html>" + i + "<br>" + getEvents(LocalDate.of(cal.getYear(), cal.getMonth(), i)) + "</html>");
                // today
                daysHolder[week][counter].setForeground(Color.BLUE);
                counter++;
                i++;
            } else if (counter == 6 && i < lengthOfMonth && i != today) {
                daysHolder[week][counter].setText(
                        "<html>" + i + "<br>" + getEvents(LocalDate.of(cal.getYear(), cal.getMonth(), i)) + "</html>");
                counter = 0;
                i++;
                week++;
            } else if (counter == 6 && i < lengthOfMonth && !sameMonth) {
                daysHolder[week][counter].setText(
                        "<html>" + i + "<br>" + getEvents(LocalDate.of(cal.getYear(), cal.getMonth(), i)) + "</html>");
                counter = 0;
                i++;
                week++;
            } else if (counter == 6 && i < lengthOfMonth && !sameYear) {
                daysHolder[week][counter].setText(
                        "<html>" + i + "<br>" + getEvents(LocalDate.of(cal.getYear(), cal.getMonth(), i)) + "</html>");
                counter = 0;
                i++;
                week++;
            } else if (counter == 6 && i < lengthOfMonth) {
                daysHolder[week][counter].setText(
                        "<html>" + i + "<br>" + getEvents(LocalDate.of(cal.getYear(), cal.getMonth(), i)) + "</html>");
                // today
                daysHolder[week][counter].setForeground(Color.BLUE);
                counter = 0;
                i++;
                week++;
            } else if (counter <= 6 && i >= 10 && i == lengthOfMonth && i != today) {
                daysHolder[week][counter].setText(
                        "<html>" + i + "<br>" + getEvents(LocalDate.of(cal.getYear(), cal.getMonth(), i)) + "</html>");
                break;
            } else if (counter <= 6 && i >= 10 && i == lengthOfMonth && !sameMonth) {
                daysHolder[week][counter].setText(
                        "<html>" + i + "<br>" + getEvents(LocalDate.of(cal.getYear(), cal.getMonth(), i)) + "</html>");
                break;
            } else if (counter <= 6 && i >= 10 && i == lengthOfMonth && !sameYear) {
                daysHolder[week][counter].setText(
                        "<html>" + i + "<br>" + getEvents(LocalDate.of(cal.getYear(), cal.getMonth(), i)) + "</html>");
                break;
            } else if (counter <= 6 && i >= 10 && i == lengthOfMonth) {
                daysHolder[week][counter].setText(
                        "<html>" + i + "<br>" + getEvents(LocalDate.of(cal.getYear(), cal.getMonth(), i)) + "</html>");
                // today
                daysHolder[week][counter].setForeground(Color.BLUE);
                counter++;
                i++;
                break;
            } else {
                break;
            }
        }
    }

    public String getEvents(LocalDate date) {
        StringBuilder event = new StringBuilder();
        for (Event e : events) {
            if (e.occursOn(date)) {
                event.append(e.toShortString()).append("<br>");
            }
        }
        return event.toString();
    }

    @Override
    public void stateChanged(ChangeEvent arg0) {
        this.display();
    }

    public void next() {
        model.plusMonths(1);
        display();
    }

    public void previous() {
        model.plusMonths(-1);
        display();
    }
}