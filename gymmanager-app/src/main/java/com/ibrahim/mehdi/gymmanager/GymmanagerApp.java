/**
 * @file GymManagerApp.java
 * @brief Main GUI application for Gym Management System
 */
package com.ibrahim.mehdi.gymmanager;

import com.ibrahim.mehdi.gymmanager.model.*;
import com.ibrahim.mehdi.gymmanager.service.GymService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Map;

/**
 * Main GUI Application
 */
public class GymManagerApp extends JFrame {
    private static final long serialVersionUID = 1L;
    
    private GymService gymService;
    private JTabbedPane tabbedPane;
    
    // Member tab
    private JTextField memberNameField, memberSurnameField, memberPhoneField, memberEmailField;
    private JComboBox<Member.MembershipType> memberTypeCombo;
    private JTable memberTable;
    private DefaultTableModel memberTableModel;
    
    // Appointment tab
    private JTextField appointmentMemberIdField, appointmentServiceField;
    private JSpinner appointmentPrioritySpinner;
    private JTable appointmentTable;
    private DefaultTableModel appointmentTableModel;
    
    // Equipment tab
    private JTextField equipNameField, equipTypeField, equipQtyField;
    private JSpinner equipXSpinner, equipYSpinner;
    private JTable equipmentTable;
    private DefaultTableModel equipmentTableModel;
    
    // Queue tab
    private JTextField queueMemberIdField;
    private JTextArea queueDisplayArea;
    
    // History tab
    private JTextArea historyDisplayArea;
    
    // Statistics tab
    private JTextArea statsDisplayArea;
    
    public GymManagerApp() {
        gymService = new GymService();
        initializeGUI();
    }
    
    private void initializeGUI() {
        setTitle("Gym Manager - Data Structures Project");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        tabbedPane = new JTabbedPane();
        
        tabbedPane.addTab("Members", createMembersPanel());
        tabbedPane.addTab("Appointments", createAppointmentsPanel());
        tabbedPane.addTab("Equipment", createEquipmentPanel());
        tabbedPane.addTab("Waiting Queue", createQueuePanel());
        tabbedPane.addTab("History & Undo", createHistoryPanel());
        tabbedPane.addTab("Search (KMP)", createSearchPanel());
        tabbedPane.addTab("Statistics", createStatisticsPanel());
        tabbedPane.addTab("About", createAboutPanel());
        
        add(tabbedPane);
        refreshAllDisplays();
    }
    
    private JPanel createMembersPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        formPanel.add(new JLabel("Name:"));
        memberNameField = new JTextField();
        formPanel.add(memberNameField);
        
        formPanel.add(new JLabel("Surname:"));
        memberSurnameField = new JTextField();
        formPanel.add(memberSurnameField);
        
        formPanel.add(new JLabel("Phone:"));
        memberPhoneField = new JTextField();
        formPanel.add(memberPhoneField);
        
        formPanel.add(new JLabel("Email:"));
        memberEmailField = new JTextField();
        formPanel.add(memberEmailField);
        
        formPanel.add(new JLabel("Type:"));
        memberTypeCombo = new JComboBox<Member.MembershipType>(Member.MembershipType.values());
        formPanel.add(memberTypeCombo);
        
        JButton addButton = new JButton("Add Member");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addMember();
            }
        });
        formPanel.add(addButton);
        
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                refreshMemberTable();
            }
        });
        formPanel.add(refreshButton);
        
        panel.add(formPanel, BorderLayout.NORTH);
        
        String[] columns = {"ID", "Name", "Surname", "Phone", "Email", "Type", "Active"};
        memberTableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) { 
                return false; 
            }
        };
        memberTable = new JTable(memberTableModel);
        panel.add(new JScrollPane(memberTable), BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createAppointmentsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        formPanel.add(new JLabel("Member ID:"));
        appointmentMemberIdField = new JTextField();
        formPanel.add(appointmentMemberIdField);
        
        formPanel.add(new JLabel("Service:"));
        appointmentServiceField = new JTextField();
        formPanel.add(appointmentServiceField);
        
        formPanel.add(new JLabel("Priority:"));
        appointmentPrioritySpinner = new JSpinner(new SpinnerNumberModel(5, 1, 10, 1));
        formPanel.add(appointmentPrioritySpinner);
        
        JButton createButton = new JButton("Create");
        createButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createAppointment();
            }
        });
        formPanel.add(createButton);
        
        JButton processButton = new JButton("Process Next");
        processButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                processNextAppointment();
            }
        });
        formPanel.add(processButton);
        
        panel.add(formPanel, BorderLayout.NORTH);
        
        String[] columns = {"ID", "Member", "Service", "Priority", "Time", "Status"};
        appointmentTableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) { 
                return false; 
            }
        };
        appointmentTable = new JTable(appointmentTableModel);
        panel.add(new JScrollPane(appointmentTable), BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createEquipmentPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        formPanel.add(new JLabel("Name:"));
        equipNameField = new JTextField();
        formPanel.add(equipNameField);
        
        formPanel.add(new JLabel("Type:"));
        equipTypeField = new JTextField();
        formPanel.add(equipTypeField);
        
        formPanel.add(new JLabel("Quantity:"));
        equipQtyField = new JTextField();
        formPanel.add(equipQtyField);
        
        formPanel.add(new JLabel("X (0-19):"));
        equipXSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 19, 1));
        formPanel.add(equipXSpinner);
        
        formPanel.add(new JLabel("Y (0-19):"));
        equipYSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 19, 1));
        formPanel.add(equipYSpinner);
        
        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addEquipment();
            }
        });
        formPanel.add(addButton);
        
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                refreshEquipmentTable();
            }
        });
        formPanel.add(refreshButton);
        
        panel.add(formPanel, BorderLayout.NORTH);
        
        String[] columns = {"ID", "Name", "Type", "Qty", "X", "Y"};
        equipmentTableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) { 
                return false; 
            }
        };
        equipmentTable = new JTable(equipmentTableModel);
        panel.add(new JScrollPane(equipmentTable), BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createQueuePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        formPanel.add(new JLabel("Member ID:"));
        queueMemberIdField = new JTextField(10);
        formPanel.add(queueMemberIdField);
        
        JButton addButton = new JButton("Add to Queue");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addToQueue();
            }
        });
        formPanel.add(addButton);
        
        JButton processButton = new JButton("Process Next");
        processButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                processFromQueue();
            }
        });
        formPanel.add(processButton);
        
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                refreshQueueDisplay();
            }
        });
        formPanel.add(refreshButton);
        
        panel.add(formPanel, BorderLayout.NORTH);
        
        queueDisplayArea = new JTextArea();
        queueDisplayArea.setEditable(false);
        queueDisplayArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        panel.add(new JScrollPane(queueDisplayArea), BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton backButton = new JButton("← Back");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                navigateHistory(false);
            }
        });
        buttonPanel.add(backButton);
        
        JButton forwardButton = new JButton("Forward →");
        forwardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                navigateHistory(true);
            }
        });
        buttonPanel.add(forwardButton);
        
        JButton undoButton = new JButton("Undo");
        undoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                performUndo();
            }
        });
        buttonPanel.add(undoButton);
        
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                refreshHistoryDisplay();
            }
        });
        buttonPanel.add(refreshButton);
        
        panel.add(buttonPanel, BorderLayout.NORTH);
        
        historyDisplayArea = new JTextArea();
        historyDisplayArea.setEditable(false);
        historyDisplayArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        panel.add(new JScrollPane(historyDisplayArea), BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        formPanel.add(new JLabel("Search Name:"));
        final JTextField searchField = new JTextField(20);
        formPanel.add(searchField);
        
        final JTextArea resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JButton searchButton = new JButton("Search (KMP)");
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String term = searchField.getText().trim();
                if (!term.isEmpty()) {
                    List<Member> results = gymService.searchMemberByName(term);
                    StringBuilder sb = new StringBuilder();
                    sb.append("KMP Search: \"").append(term).append("\"\n");
                    sb.append("Found ").append(results.size()).append(" members\n\n");
                    for (Member m : results) {
                        sb.append(m.toString()).append("\n");
                    }
                    resultArea.setText(sb.toString());
                }
            }
        });
        formPanel.add(searchButton);
        
        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(resultArea), BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createStatisticsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton refreshButton = new JButton("Refresh Statistics");
        refreshButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                refreshStatistics();
            }
        });
        panel.add(refreshButton, BorderLayout.NORTH);
        
        statsDisplayArea = new JTextArea();
        statsDisplayArea.setEditable(false);
        statsDisplayArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        panel.add(new JScrollPane(statsDisplayArea), BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createAboutPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JTextArea aboutText = new JTextArea();
        aboutText.setEditable(false);
        aboutText.setFont(new Font("SansSerif", Font.PLAIN, 12));
        aboutText.setText(
            "GYM MANAGER - DATA STRUCTURES PROJECT\n\n" +
            "All 12 Data Structures:\n\n" +
            "1. Double Linked List - Member history\n" +
            "2. XOR Linked List - Workout history\n" +
            "3. Sparse Matrix - Equipment map\n" +
            "4. Stack - Undo operations\n" +
            "5. Queue - Waiting queue\n" +
            "6. Min Heap - Appointments\n" +
            "7. Hash Table - Member lookup\n" +
            "8. BFS/DFS - Graph traversal\n" +
            "9. SCC - Components\n" +
            "10. KMP - Name search\n" +
            "11. Huffman - Compression\n" +
            "12. B+ Tree - Indexing\n" +
            "13. Linear Probing - File ops\n\n" +
            "CEN207/CE205 - Fall 2025-2026\n"
        );
        
        panel.add(new JScrollPane(aboutText), BorderLayout.CENTER);
        return panel;
    }
    
    // Action methods
    private void addMember() {
        try {
            String name = memberNameField.getText().trim();
            String surname = memberSurnameField.getText().trim();
            if (name.isEmpty() || surname.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name and Surname required!");
                return;
            }
            
            Member.MembershipType type = (Member.MembershipType) memberTypeCombo.getSelectedItem();
            Member m = gymService.addMember(name, surname, 
                memberPhoneField.getText().trim(),
                memberEmailField.getText().trim(), type);
            
            JOptionPane.showMessageDialog(this, "Added: " + m.getFullName());
            memberNameField.setText("");
            memberSurnameField.setText("");
            memberPhoneField.setText("");
            memberEmailField.setText("");
            refreshMemberTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
    
    private void createAppointment() {
        try {
            int memberId = Integer.parseInt(appointmentMemberIdField.getText().trim());
            String service = appointmentServiceField.getText().trim();
            int priority = ((Number) appointmentPrioritySpinner.getValue()).intValue();
            
            Appointment apt = gymService.createAppointment(memberId, service, priority);
            if (apt != null) {
                JOptionPane.showMessageDialog(this, "Appointment created!");
                appointmentMemberIdField.setText("");
                appointmentServiceField.setText("");
                refreshAppointmentDisplay();
            } else {
                JOptionPane.showMessageDialog(this, "Member not found!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid input!");
        }
    }
    
    private void processNextAppointment() {
        Appointment apt = gymService.processNextAppointment();
        if (apt != null) {
            JOptionPane.showMessageDialog(this, "Processed: " + apt.getMemberName());
            refreshAppointmentDisplay();
        } else {
            JOptionPane.showMessageDialog(this, "No appointments!");
        }
    }
    
    private void addEquipment() {
        try {
            String name = equipNameField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name required!");
                return;
            }
            
            int qty = Integer.parseInt(equipQtyField.getText().trim());
            int x = ((Number) equipXSpinner.getValue()).intValue();
            int y = ((Number) equipYSpinner.getValue()).intValue();
            
            gymService.addEquipment(name, equipTypeField.getText().trim(), qty, x, y);
            JOptionPane.showMessageDialog(this, "Equipment added!");
            equipNameField.setText("");
            equipTypeField.setText("");
            equipQtyField.setText("");
            refreshEquipmentTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
    
    private void addToQueue() {
        try {
            int memberId = Integer.parseInt(queueMemberIdField.getText().trim());
            Member m = gymService.searchMember(memberId);
            if (m != null) {
                gymService.addToWaitingQueue(m);
                JOptionPane.showMessageDialog(this, "Added: " + m.getFullName());
                queueMemberIdField.setText("");
                refreshQueueDisplay();
            } else {
                JOptionPane.showMessageDialog(this, "Member not found!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid ID!");
        }
    }
    
    private void processFromQueue() {
        Member m = gymService.processNextInQueue();
        if (m != null) {
            JOptionPane.showMessageDialog(this, "Processed: " + m.getFullName());
            refreshQueueDisplay();
        } else {
            JOptionPane.showMessageDialog(this, "Queue empty!");
        }
    }
    
    private void navigateHistory(boolean forward) {
        String record = forward ? gymService.navigateHistoryForward() : 
                                 gymService.navigateHistoryBackward();
        if (record != null) {
            refreshHistoryDisplay();
        }
    }
    
    private void performUndo() {
        String action = gymService.undo();
        if (action != null) {
            JOptionPane.showMessageDialog(this, "Undone: " + action);
            refreshAllDisplays();
        } else {
            JOptionPane.showMessageDialog(this, "Nothing to undo!");
        }
    }
    
    // Refresh methods
    private void refreshMemberTable() {
        memberTableModel.setRowCount(0);
        for (Member m : gymService.getAllMembers()) {
            memberTableModel.addRow(new Object[]{
                m.getId(), m.getName(), m.getSurname(), 
                m.getPhoneNumber(), m.getEmail(), m.getMembershipType(), m.isActive()
            });
        }
    }
    
    private void refreshAppointmentDisplay() {
        appointmentTableModel.setRowCount(0);
        Appointment next = gymService.getNextAppointment();
        if (next != null) {
            appointmentTableModel.addRow(new Object[]{
                next.getId(), next.getMemberName(), next.getService(),
                next.getPriority(), next.getAppointmentTime(), next.getStatus()
            });
        }
    }
    
    private void refreshEquipmentTable() {
        equipmentTableModel.setRowCount(0);
        for (Equipment e : gymService.getAllEquipment()) {
            equipmentTableModel.addRow(new Object[]{
                e.getId(), e.getName(), e.getType(), e.getQuantity(),
                e.getLocationX(), e.getLocationY()
            });
        }
    }
    
    private void refreshQueueDisplay() {
        queueDisplayArea.setText(String.format(
            "WAITING QUEUE (FIFO)\n===================\n\nSize: %d\n",
            gymService.getQueueSize()
        ));
    }
    
    private void refreshHistoryDisplay() {
        historyDisplayArea.setText(String.format(
            "HISTORY (Double Linked List)\n===========================\n\nCurrent: %s\n",
            gymService.getCurrentHistory()
        ));
    }
    
    private void refreshStatistics() {
        Map<String, Object> stats = gymService.getStatistics();
        StringBuilder sb = new StringBuilder("STATISTICS\n==========\n\n");
        for (Map.Entry<String, Object> entry : stats.entrySet()) {
            sb.append(String.format("%-20s: %s\n", entry.getKey(), entry.getValue()));
        }
        statsDisplayArea.setText(sb.toString());
    }
    
    private void refreshAllDisplays() {
        refreshMemberTable();
        refreshAppointmentDisplay();
        refreshEquipmentTable();
        refreshQueueDisplay();
        refreshHistoryDisplay();
        refreshStatistics();
    }
    
    /**
     * Main method - Application entry point
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    // Use default look and feel
                }
                GymManagerApp app = new GymManagerApp();
                app.setVisible(true);
            }
        });
    }
}