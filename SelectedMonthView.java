package calendar;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.Arrays;

public class SelectedMonthView extends JPanel implements CalendarView {

    private LocalDate visibleDate;
    private JButton[][] daysHolder;
    private final CalendarViewModel model;

    public SelectedMonthView(CalendarViewModel m) {
        this.model = m;
        visibleDate = model.getSelectedDate();
        display();
    }

    private void display() {
        this.removeAll();
        this.revalidate();

        final JPanel header = new JPanel();
        header.setLayout(new BorderLayout());

        final JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        final JPanel body = new JPanel();
        body.setLayout(new GridLayout(8, 7));

        final JButton left = new JButton(" < ");
        left.setBackground(Color.LIGHT_GRAY); // configure left button

        final JButton right = new JButton(" > ");
        right.setBackground(Color.LIGHT_GRAY); // configure right button

        buttonPanel.add(left);
        buttonPanel.add(right); // add buttons to buttonPanel

        // title
        header.add(
                new JLabel(visibleDate.getMonth().name() + " " + visibleDate.getYear()),
                BorderLayout.WEST
        );
        header.add(buttonPanel, BorderLayout.EAST);

        left.addActionListener(e -> previous());
        right.addActionListener(e -> next());

        Arrays.asList("S", "M", "T", "W", "T", "F", "S")
                .forEach(l -> {
                    JLabel daysOfWeek = new JLabel(l, SwingConstants.CENTER);
                    daysOfWeek.setBackground(Color.WHITE);
                    daysOfWeek.setOpaque(true);
                    body.add(daysOfWeek);
                });

        // Creating and populating the calendar with each date as a button
       this.daysHolder = new JButton[6][7];
        LocalDate firstDayOfWeek = visibleDate.withDayOfMonth(1);
        int day = 1;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                JButton dateButton = new JButton();
                dateButton.setBackground(new Color(230, 230, 230));
                dateButton.setOpaque(true);
                dateButton.setEnabled(false);

                if (i != 0 || j >= firstDayOfWeek.getDayOfWeek().getValue()) {
                    if (day < visibleDate.lengthOfMonth() + 1) {
                        dateButton.setText("" + day);
                        dateButton.setEnabled(true);

                        final LocalDate buttonRepresentedDate = visibleDate
                                .withDayOfMonth(Integer.parseInt(dateButton.getText()));

                        // set color of date button
                        if (buttonRepresentedDate.equals(LocalDate.now())) { // today
                            dateButton.setBackground(new Color(122, 138, 153));
                            dateButton.setForeground(Color.WHITE);
                        } else if (buttonRepresentedDate.equals(model.getSelectedDate())) // selected date
                            dateButton.setBackground(new Color(198, 217, 234));
                        else
                            dateButton.setBackground(Color.WHITE);

                        // date button action listener
                        dateButton.addActionListener(e -> model.setSelectedDate(buttonRepresentedDate));
                        day++;
                    }
                }
                // else if we haven't reached the end of the month


                daysHolder[i][j] = dateButton;
                body.add(daysHolder[i][j]);
            }
        }

        this.setLayout(new BorderLayout());
        this.add(header, BorderLayout.NORTH);
        this.add(body, BorderLayout.CENTER);
    }

    public void stateChanged(ChangeEvent arg0) {
        visibleDate = model.getSelectedDate();
        this.display();
    }

    public void next() {
        visibleDate = visibleDate.plusMonths(1);
        this.display();
    }

    public void previous() {
        visibleDate = visibleDate.plusMonths(-1);
        this.display();
    }
    
    public void toggleColorDark()
    {
    	for (int i = 0; i < 6; i++) 
    	{
            for (int j = 0; j < 7; j++) 
            {
            	JButton dateButton = this.daysHolder[i][j];
            	if (dateButton.isEnabled())
            	{
            		LocalDate buttonRepresentedDate = visibleDate.withDayOfMonth(Integer.parseInt(dateButton.getText()));
            		if (buttonRepresentedDate.equals(LocalDate.now())) { // today
                        dateButton.setBackground(new Color(122, 138, 153));
                        dateButton.setForeground(Color.WHITE);
                    } else if (buttonRepresentedDate.equals(model.getSelectedDate())) // selected date
                        dateButton.setBackground(new Color(198, 217, 234));
                    else
                        dateButton.setBackground(Color.BLACK);
            			dateButton.setForeground(Color.WHITE);
            		
            	
            	}
            	
            }
    	}
    }
    
    public void toggleColorLight()
    {
    	for (int i = 0; i < 6; i++) 
    	{
            for (int j = 0; j < 7; j++) 
            {
            	JButton dateButton = this.daysHolder[i][j];
            	if (dateButton.isEnabled())
            	{
            		LocalDate buttonRepresentedDate = visibleDate.withDayOfMonth(Integer.parseInt(dateButton.getText()));
            		if (buttonRepresentedDate.equals(LocalDate.now())) { // today
                        dateButton.setBackground(new Color(122, 138, 153));
                        dateButton.setForeground(Color.WHITE);
                    } else if (buttonRepresentedDate.equals(model.getSelectedDate())) // selected date
                        dateButton.setBackground(new Color(198, 217, 234));
                    else
                        dateButton.setBackground(Color.WHITE);
            			dateButton.setForeground(Color.BLACK);
            		
            	
            	}
            	
            }
    	}

    }
    
    public void toggleColor(ButtonColor bc)
    {
    	for (int i = 0; i < 6; i++) 
    	{
            for (int j = 0; j < 7; j++) 
            {
            	JButton dateButton = this.daysHolder[i][j];
            	if (dateButton.isEnabled())
            	{
            		LocalDate buttonRepresentedDate = visibleDate.withDayOfMonth(Integer.parseInt(dateButton.getText()));
            		if (buttonRepresentedDate.equals(LocalDate.now())) { // today
                        dateButton.setBackground(new Color(122, 138, 153));
                        dateButton.setForeground(Color.WHITE);
                    } 
            		else if (buttonRepresentedDate.equals(model.getSelectedDate())) // selected date
                        dateButton.setBackground(new Color(198, 217, 234));
                    else
                        bc.changeColor(dateButton);
            		
            	
            	}
            	
            }
    	}
    }
}
