import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

class LibraryItem implements Serializable {
    String title;
    String author;
    String category;
    boolean isAvailable;

    public LibraryItem(String title, String author, String category) {
        this.title = title;
        this.author = author;
        this.category = category;
        this.isAvailable = true;
    }

    public void checkOut() {
        if (isAvailable) {
            isAvailable = false;
            System.out.println(title + " checked out successfully.");
        } else {
            System.out.println(title + " is not available.");
        }
    }

    public void returnItem() {
        isAvailable = true;
        System.out.println(title + " returned successfully.");
    }

    @Override
    public String toString() {
        return "Title: " + title + ", Author: " + author + ", Category: " + category + ", Available: " + isAvailable;
    }
}

class LibraryManager {
    java.util.List<LibraryItem> items;  // Specify the fully qualified name
    String fileName;

    public LibraryManager(String fileName) {
        this.fileName = fileName;
        items = new ArrayList<>();
        loadItems();
    }

    public void addItem(LibraryItem item) {
        items.add(item);
        saveItems();
    }

    public java.util.List<LibraryItem> searchItem(String query) {  // Specify the fully qualified name
        java.util.List<LibraryItem> results = new ArrayList<>();  // Specify the fully qualified name
        for (LibraryItem item : items) {
            if (item.title.contains(query) || item.author.contains(query) || item.category.contains(query)) {
                results.add(item);
            }
        }
        return results;
    }

    public void checkOutItem(String title) {
        for (LibraryItem item : items) {
            if (item.title.equals(title)) {
                item.checkOut();
                saveItems();
                return;
            }
        }
        System.out.println("Item not found.");
    }

    public void returnItem(String title) {
        for (LibraryItem item : items) {
            if (item.title.equals(title)) {
                item.returnItem();
                saveItems();
                return;
            }
        }
        System.out.println("Item not found.");
    }

    @SuppressWarnings("unchecked")
    public void loadItems() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            items = (java.util.List<LibraryItem>) ois.readObject();  // Specify the fully qualified name
        } catch (FileNotFoundException e) {
            System.out.println("No existing library data found. Starting with an empty library.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveItems() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(items);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

public class LibrarySystem {
    static LibraryManager libraryManager = new LibraryManager("library_data.ser");

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    public static void createAndShowGUI() {
        JFrame frame = new JFrame("Library System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel addPanel = new JPanel(new GridLayout(4, 2));
        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField categoryField = new JTextField();
        JButton addButton = new JButton("Add Item");

        addPanel.add(new JLabel("Title:"));
        addPanel.add(titleField);
        addPanel.add(new JLabel("Author:"));
        addPanel.add(authorField);
        addPanel.add(new JLabel("Category:"));
        addPanel.add(categoryField);
        addPanel.add(addButton);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = titleField.getText();
                String author = authorField.getText();
                String category = categoryField.getText();
                LibraryItem item = new LibraryItem(title, author, category);
                libraryManager.addItem(item);
                JOptionPane.showMessageDialog(frame, "Item added successfully.");
                titleField.setText("");
                authorField.setText("");
                categoryField.setText("");
            }
        });

        JPanel searchPanel = new JPanel(new GridLayout(2, 1));
        JTextField searchField = new JTextField();
        JButton searchButton = new JButton("Search");
        JTextArea searchResults = new JTextArea();
        searchResults.setEditable(false);

        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(new JScrollPane(searchResults));

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String query = searchField.getText();
                java.util.List<LibraryItem> results = libraryManager.searchItem(query);  // Specify the fully qualified name
                searchResults.setText("");
                for (LibraryItem item : results) {
                    searchResults.append(item.toString() + "\n");
                }
            }
        });

        JPanel checkOutPanel = new JPanel(new GridLayout(2, 1));
        JTextField checkOutField = new JTextField();
        JButton checkOutButton = new JButton("Check Out");

        checkOutPanel.add(checkOutField);
        checkOutPanel.add(checkOutButton);

        checkOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = checkOutField.getText();
                libraryManager.checkOutItem(title);
                JOptionPane.showMessageDialog(frame, "Checked out item: " + title);
                checkOutField.setText("");
            }
        });

        JPanel returnPanel = new JPanel(new GridLayout(2, 1));
        JTextField returnField = new JTextField();
        JButton returnButton = new JButton("Return");

        returnPanel.add(returnField);
        returnPanel.add(returnButton);

        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = returnField.getText();
                libraryManager.returnItem(title);
                JOptionPane.showMessageDialog(frame, "Returned item: " + title);
                returnField.setText("");
            }
        });

        tabbedPane.addTab("Add Item", addPanel);
        tabbedPane.addTab("Search Item", searchPanel);
        tabbedPane.addTab("Check Out Item", checkOutPanel);
        tabbedPane.addTab("Return Item", returnPanel);

        frame.add(tabbedPane);
        frame.setVisible(true);
    }
}
