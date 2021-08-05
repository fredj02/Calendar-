package calendar;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.time.LocalDate;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class CalendarTester {

    public static void main(String[] args) {
        final CalendarViewModel model = new CalendarViewModel();

        final SelectedMonthView selectedMonthView = new SelectedMonthView(model);
        final CreateView createView = new CreateView(model);
        final DayView dayView = new DayView(model);
        final WeekView weekView = new WeekView(model);
        final MonthView monthView = new MonthView(model);
        final AgendaView agendaView = new AgendaView(model);

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // Attach views/change listeners to model
        model.attachChangeListener(selectedMonthView);
        model.attachChangeListener(dayView);
        model.attachChangeListener(weekView);
        model.attachChangeListener(monthView);
        model.attachChangeListener(agendaView);

        final JFrame frame = new JFrame();
        frame.setTitle("Calendar");
        frame.setMinimumSize(new Dimension(1100, 500));

        // Split JFrame into two halves
        final JPanel leftControls = new JPanel();
        leftControls.setLayout(new BorderLayout());
        final JPanel rightControls = new JPanel();
        rightControls.setLayout(new BorderLayout());

        // Left half has a JPanel for the top buttons (top-left panel)
        JPanel currentViewNav = new JPanel();
        currentViewNav.setLayout(new GridBagLayout());

        final JButton todayButton = new JButton("Today");
        final JButton todayLeftButton = new JButton("<");
        final JButton todayRightButton = new JButton(">");
        final JButton createButton = new JButton("Create Event");

        // Disable todayLeftButton and todayRightButton by default
        todayLeftButton.setEnabled(false);
        todayRightButton.setEnabled(false);

        // Adding buttons to top-left panel
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        currentViewNav.add(todayButton, c);
        c.gridx++;
        currentViewNav.add(todayLeftButton, c);
        c.gridx++;
        currentViewNav.add(todayRightButton, c);
        c.gridx = 0;
        c.gridy++;
        c.gridwidth = 3;
        currentViewNav.add(createButton, c);
        
        //Buttons for dark mode
        JPanel toggle = new JPanel();
        final JButton darkButton = new JButton("Dark");
        final JButton lightButton = new JButton("Light");
        toggle.add(darkButton);
        toggle.add(lightButton);

        // Right half has a JPanel for the top buttons (top-right panel)
        JPanel changeViewNav = new JPanel();
        changeViewNav.setLayout(new GridLayout(1, 5));

        final JButton dayButton = new JButton("Day");
        final JButton weekButton = new JButton("Week");
        final JButton monthButton = new JButton("Month");
        final JButton agendaButton = new JButton("Agenda");
        final JButton fromFileButton = new JButton("From File");

        // Today action listener
        todayButton.addActionListener(e -> model.setSelectedDate(LocalDate.now()));

        // Left/Previous (<) action listener
        todayLeftButton.addActionListener(e -> {
            Component rightView = rightControls.getComponent(1);
            if (rightView instanceof CalendarView) {
                ((CalendarView) rightView).previous();
            }
        });

        // Right/Next (>) action listener
        todayRightButton.addActionListener(e -> {
            Component rightView = rightControls.getComponent(1);
            if (rightView instanceof CalendarView) {
                ((CalendarView) rightView).next();
            }
        });
        
        //Dark action listener
        darkButton.addActionListener(e -> {selectedMonthView.toggleColor(new DarkColor());});
        
      //Light action listener
        lightButton.addActionListener(e -> {selectedMonthView.toggleColor(new LightColor());});

        // Checks whether a CalendarView is in the right side to determine
        // whether to disable the left/right buttons
        rightControls.addContainerListener(new ContainerListener() {
            public void componentAdded(ContainerEvent arg0) {
                updateLeftRightButtonsAbility();
            }

            public void componentRemoved(ContainerEvent arg0) {
                updateLeftRightButtonsAbility();
            }

            public void updateLeftRightButtonsAbility() {
                if (rightControls.getComponentCount() > 1) {
                    Component rightView = rightControls.getComponent(1);
                    if (rightView instanceof CalendarView) {
                        todayLeftButton.setEnabled(true);
                        todayRightButton.setEnabled(true);
                    } else {
                        todayLeftButton.setEnabled(false);
                        todayRightButton.setEnabled(false);
                    }
                }
            }
        });

        // Create Event action listener
        createButton.addActionListener(e -> {
            rightControls.removeAll();
            rightControls.add(changeViewNav, BorderLayout.NORTH);
            rightControls.add(createView, BorderLayout.CENTER);
            rightControls.revalidate();
            rightControls.repaint();
        });

        // DayView Action Listener
        dayButton.addActionListener(arg0 -> {
            rightControls.removeAll();
            rightControls.add(changeViewNav, BorderLayout.NORTH);
            rightControls.add(dayView, BorderLayout.CENTER);
            rightControls.revalidate();
            rightControls.repaint();
        });

        // WeekView Action Listener
        weekButton.addActionListener(arg0 -> {
            rightControls.removeAll();
            rightControls.add(changeViewNav, BorderLayout.NORTH);
            rightControls.add(weekView, BorderLayout.CENTER);
            rightControls.revalidate();
            rightControls.repaint();
        });

        // MonthView Action Listener
        monthButton.addActionListener(arg0 -> {
            rightControls.removeAll();
            rightControls.add(changeViewNav, BorderLayout.NORTH);
            rightControls.add(monthView, BorderLayout.CENTER);
            rightControls.revalidate();
            rightControls.repaint();
        });

        // Agenda action listener
        agendaButton.addActionListener(e -> {
            rightControls.removeAll();
            agendaView.setNewInterval(true);
            rightControls.add(changeViewNav, BorderLayout.NORTH);
            rightControls.add(agendaView, BorderLayout.CENTER);
            rightControls.revalidate();
            rightControls.repaint();
        });

        // From File action listener
        fromFileButton.addActionListener(e -> {
            if (FileImporter.importFile(model))
                fromFileButton.setEnabled(false);
        });

        // Adding buttons to top-right panel
        changeViewNav.add(dayButton);
        changeViewNav.add(weekButton);
        changeViewNav.add(monthButton);
        changeViewNav.add(agendaButton);
        changeViewNav.add(fromFileButton);

        // Adding top-left panel to left half & top-right panel to right half
        leftControls.add(currentViewNav, BorderLayout.NORTH);
        rightControls.add(changeViewNav, BorderLayout.NORTH);
        rightControls.add(dayView, BorderLayout.CENTER);

        leftControls.add(selectedMonthView, BorderLayout.CENTER);
        leftControls.add(toggle, BorderLayout.SOUTH);

        // Adding left and right halves to the frame
        frame.add(leftControls, BorderLayout.WEST);
        frame.add(rightControls, BorderLayout.CENTER);

        // Set frame properties
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
