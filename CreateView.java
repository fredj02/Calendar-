package calendar;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

class CreateView extends JPanel {

    private final CalendarViewModel model;

    public CreateView(CalendarViewModel m) {
        model = m;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        final JTextField nameTextField = new JTextField();
        final JTextField dateTextField = new JTextField();
        final JTextField startTimeTextField = new JTextField();
        final JTextField endTimeTextField = new JTextField();
        final JButton saveButton = new JButton("Save");

        final Dimension TextFieldDimension = new Dimension(800, 20);
        nameTextField.setMaximumSize(TextFieldDimension);
        startTimeTextField.setMaximumSize(TextFieldDimension);
        endTimeTextField.setMaximumSize(TextFieldDimension);
        dateTextField.setMaximumSize(TextFieldDimension);

        saveButton.addActionListener(e -> {
            String message = "";
            try {
                final String nameStr = nameTextField.getText();
                if (nameStr.length() == 0) {
                    throw new NullPointerException();
                }

                final String dateStr = dateTextField.getText();
                final String[] dateSplitted = dateStr.split("/"); // 0: MM; 1: DD; 2: YYYY
                final LocalDate date = LocalDate.of(
                        Integer.parseInt(dateSplitted[2].trim()),
                        Integer.parseInt(dateSplitted[0].trim()),
                        Integer.parseInt(dateSplitted[1].trim())
                );

                final String startTimeStr = startTimeTextField.getText();
                final LocalTime startTime = LocalTime.of(Integer.parseInt(startTimeStr), 0);
                final String endTimeStr = endTimeTextField.getText();
                final LocalTime endTime = LocalTime.of(Integer.parseInt(endTimeStr), 0);

                final Event event = new Event(nameStr, date, startTime, endTime);
                if (model.addEvent(event)) {
                    message = "Saved the event: " + event;
                } else {
                    message = "Not saved. There is a conflict with an existing event.";
                }
            } catch (ArrayIndexOutOfBoundsException exception) {
                message = "Check the date format";
            } catch (NumberFormatException | DateTimeException exception) {
                message = "Check the date and times";
            } catch (NullPointerException exception) {
                message = "Provide a name";
            } finally {
                JOptionPane.showMessageDialog(null, message, "Create Event", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        this.add(new JLabel("Create New Event"));
        this.add(new JLabel("Name"));
        this.add(nameTextField);
        this.add(new JLabel("Date (MM/DD/YYYY)"));
        this.add(dateTextField);
        this.add(new JLabel("Start time (HH)"));
        this.add(startTimeTextField);
        this.add(new JLabel("End time (HH)"));
        this.add(endTimeTextField);
        this.add(saveButton);
    }
}
