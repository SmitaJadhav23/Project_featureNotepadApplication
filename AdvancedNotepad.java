import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.SimpleAttributeSet;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class AdvancedNotepad extends JFrame implements ActionListener {
    JTextPane textPane;  // JTextPane for text formatting
    File currentFile;    // To keep track of the current open file
    JFileChooser fileChooser;

    public AdvancedNotepad() {
        // Frame Settings
        setTitle("Advanced Notepad");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Text Area
        textPane = new JTextPane();
        JScrollPane scrollPane = new JScrollPane(textPane);
        add(scrollPane);

        // Menu Bar
        JMenuBar menuBar = new JMenuBar();

        // File Menu
        JMenu fileMenu = new JMenu("File");
        addMenuItem(fileMenu, "New", "New");
        addMenuItem(fileMenu, "Open", "Open");
        addMenuItem(fileMenu, "Save", "Save");
        addMenuItem(fileMenu, "Save As", "SaveAs");
        addMenuItem(fileMenu, "Merge Files", "MergeFiles");
        addMenuItem(fileMenu, "Exit", "Exit");

        // Edit Menu
        JMenu editMenu = new JMenu("Edit");
        addMenuItem(editMenu, "Cut", "Cut");
        addMenuItem(editMenu, "Copy", "Copy");
        addMenuItem(editMenu, "Paste", "Paste");

        // Format Menu
        JMenu formatMenu = new JMenu("Format");
        addMenuItem(formatMenu, "Change Text Color", "ChangeColor");

        // Help Menu
        JMenu helpMenu = new JMenu("Help");
        addMenuItem(helpMenu, "About", "About");

        // Adding Menus to the MenuBar
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(formatMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        // File Chooser
        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));

        setVisible(true);
    }

    // Helper method to add MenuItems
    private void addMenuItem(JMenu menu, String name, String command) {
        JMenuItem item = new JMenuItem(name);
        item.setActionCommand(command);
        item.addActionListener(this);
        menu.add(item);
    }

    // Event Handling
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch (command) {
            case "New":
                newFile();
                break;
            case "Open":
                openFile();
                break;
            case "Save":
                saveFile();
                break;
            case "SaveAs":
                saveFileAs();
                break;
            case "MergeFiles":
                mergeFiles();
                break;
            case "Cut":
                textPane.cut();
                break;
            case "Copy":
                textPane.copy();
                break;
            case "Paste":
                textPane.paste();
                break;
            case "ChangeColor":
                changeTextColor();
                break;
            case "About":
                showAbout();
                break;
            case "Exit":
                System.exit(0);
                break;
        }
    }

    // Create a new file
    private void newFile() {
        textPane.setText("");
        currentFile = null;
        setTitle("Advanced Notepad");
    }

    // Open an existing file
    private void openFile() {
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try (BufferedReader reader = new BufferedReader(new FileReader(fileChooser.getSelectedFile()))) {
                textPane.setText("");
                String line;
                while ((line = reader.readLine()) != null) {
                    textPane.getDocument().insertString(textPane.getDocument().getLength(), line + "\n", null);
                }
                currentFile = fileChooser.getSelectedFile();
                setTitle("Advanced Notepad - " + currentFile.getName());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error opening file!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Save the current file
    private void saveFile() {
        if (currentFile == null) {
            saveFileAs();
        } else {
            writeToFile(currentFile);
        }
    }

    // Save the file with a new name
    private void saveFileAs() {
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            currentFile = fileChooser.getSelectedFile();
            writeToFile(currentFile);
        }
    }

    // Helper to write to a file
    private void writeToFile(File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(textPane.getText());
            setTitle("Advanced Notepad - " + file.getName());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving file!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Merge two files into one
    private void mergeFiles() {
        int result1 = fileChooser.showOpenDialog(this);
        if (result1 == JFileChooser.APPROVE_OPTION) {
            File file1 = fileChooser.getSelectedFile();
            int result2 = fileChooser.showOpenDialog(this);
            if (result2 == JFileChooser.APPROVE_OPTION) {
                File file2 = fileChooser.getSelectedFile();
                try (BufferedReader br1 = new BufferedReader(new FileReader(file1));
                     BufferedReader br2 = new BufferedReader(new FileReader(file2))) {
                    textPane.setText("");
                    String line;
                    while ((line = br1.readLine()) != null) textPane.getDocument().insertString(textPane.getDocument().getLength(), line + "\n", null);
                    while ((line = br2.readLine()) != null) textPane.getDocument().insertString(textPane.getDocument().getLength(), line + "\n", null);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error merging files!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    // Change the text color
    private void changeTextColor() {
        Color color = JColorChooser.showDialog(this, "Choose Text Color", Color.BLACK);
        if (color != null) {
            StyledDocument doc = textPane.getStyledDocument();
            SimpleAttributeSet set = new SimpleAttributeSet();
            StyleConstants.setForeground(set, color);
            doc.setCharacterAttributes(0, textPane.getDocument().getLength(), set, false);
        }
    }

    // Show About Dialog
    private void showAbout() {
        JOptionPane.showMessageDialog(this, "Advanced Notepad\nVersion 1.0\nDeveloped in Java Swing", "About", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdvancedNotepad::new);
    }
}
