package calendar;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

class FileImporter {

    public static boolean importFile(CalendarViewModel model) {
        final JFileChooser chooser = new JFileChooser(new File("src"));
        chooser.setFileFilter(new FileNameExtensionFilter("Text Documents (*.txt)", "txt"));

        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            if (addEventsToCalendar(chooser.getSelectedFile(), model)) {
                JOptionPane.showMessageDialog(
                        null,
                        "Events imported successfully.",
                        "Import",
                        JOptionPane.INFORMATION_MESSAGE
                );
                return true;
            } else {
                JOptionPane.showMessageDialog(
                        null,
                        "Error importing",
                        "Import",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
        return false;
    }

    private static boolean addEventsToCalendar(File file, CalendarViewModel model) {
        try {
            final Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                final String eventString = scanner.nextLine();
                final String[] eventArray = eventString.split(";");

                final LocalDate startDate = LocalDate.of(
                        Integer.parseInt(eventArray[1].trim()),
                        Integer.parseInt(eventArray[2].trim()), 1
                );

                final LocalDate endDate = LocalDate.of(
                        Integer.parseInt(eventArray[1].trim()),
                        Integer.parseInt(eventArray[3].trim()), 1
                );

                final LocalTime startTime = LocalTime.of(Integer.parseInt(eventArray[5].trim()), 0);
                final LocalTime endTime = LocalTime.of(Integer.parseInt(eventArray[6].trim()), 0);
                model.addEvent(
                        new Event(
                                eventArray[0].trim(),
                                eventArray[4].trim(),
                                startTime,
                                endTime,
                                startDate,
                                endDate
                        )
                );
            }
            scanner.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
