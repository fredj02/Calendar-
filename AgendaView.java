package calendar;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.util.TreeSet;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;

public class AgendaView extends JPanel implements CalendarView {

    private final CalendarViewModel model;
    private TreeSet<Event> events;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean newInterval;

    public AgendaView(CalendarViewModel m) {
        this.setLayout(new BorderLayout());
        this.model = m;
        events = m.getEvents();
        newInterval = true;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (newInterval) buildView();
    }

    public void setNewInterval(boolean n) {
        newInterval = n;
    }

    public void buildView() {
        this.removeAll();

        final JLabel titleLabel = new JLabel("What time frame do you want to view? (MM/DD/YYYY)");
        final JLabel startLabel = new JLabel("Start Date: ");
        final JTextField startInput = new JTextField();
        final JLabel endLabel = new JLabel("End Date: ");
        final JTextField endInput = new JTextField();

        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        startLabel.setHorizontalAlignment(JLabel.RIGHT);
        endLabel.setHorizontalAlignment(JLabel.RIGHT);

        startInput.setPreferredSize(new Dimension(300, 20));
        endInput.setPreferredSize(new Dimension(300, 20));

        final JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            try {
                final String[] startArray = startInput.getText().split("/");
                final String[] endArray = endInput.getText().split("/");
                startDate = LocalDate.of(
                        Integer.parseInt(startArray[2]),
                        Integer.parseInt(startArray[0]),
                        Integer.parseInt(startArray[1])
                );
                endDate = LocalDate.of(
                        Integer.parseInt(endArray[2]),
                        Integer.parseInt(endArray[0]),
                        Integer.parseInt(endArray[1])
                );

                setNewInterval(false);
                displayAgenda();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        null,
                        "Invalid date.",
                        "Agenda View",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        final JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());

        final GridBagConstraints constraints = new GridBagConstraints();
        Insets insets = new Insets(5, 5, 5, 5);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = insets;
        constraints.gridwidth = 2;
        inputPanel.add(titleLabel, constraints);
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        inputPanel.add(startLabel, constraints);
        constraints.gridx = 1;
        inputPanel.add(startInput, constraints);
        constraints.gridx = 0;
        constraints.gridy = 2;
        inputPanel.add(endLabel, constraints);
        constraints.gridx = 1;
        inputPanel.add(endInput, constraints);
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 2;
        inputPanel.add(submitButton, constraints);

        this.add(inputPanel, BorderLayout.CENTER);
        this.revalidate();
        this.repaint();
    }

    public void displayAgenda() {
        this.removeAll();

        if (endDate.isBefore(startDate)) {
            LocalDate toBefore = endDate;
            endDate = startDate;
            startDate = toBefore;
        }

        JLabel dateIntervalLabel = new JLabel();
        dateIntervalLabel.setText("Events: " + startDate + " --> " + endDate);

        JTextArea eventsLabel = new JTextArea();

        JScrollPane scroll = new JScrollPane(eventsLabel);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        TreeSet<LocalDate> dates = new TreeSet<>();
        LocalDate current = startDate;

        while (!current.equals(endDate)) {
            dates.add(current);
            current = current.plusDays(1);
        }

        String eventDisplay = "";

        for (LocalDate day : dates) {
            boolean noEvents = true;
            for (Event e : events) {
                if (e.occursOn(day)) {
                    if (noEvents) {
                        eventDisplay = eventDisplay + "\n" + day + "\n";
                    }
                    noEvents = false;
                    eventDisplay = eventDisplay + "  " + e.toShortString() + "\n";
                }
            }
        }

        eventsLabel.setText(eventDisplay);

        this.add(dateIntervalLabel, BorderLayout.NORTH);
        this.add(scroll);
        this.revalidate();
        this.repaint();
    }

    @Override
    public void next() {
    }

    @Override
    public void previous() {
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        events = model.getEvents();
        if (newInterval) {
            repaint();
        } else {
            displayAgenda();
        }
    }
}
