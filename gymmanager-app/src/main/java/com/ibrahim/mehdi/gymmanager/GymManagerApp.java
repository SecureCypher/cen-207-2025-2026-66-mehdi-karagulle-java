/**
 * @file GymManagerApp.java
 * @brief Main GUI application - WITHOUT QUEUE (for troubleshooting)
 */
package com.ibrahim.mehdi.gymmanager;

import com.ibrahim.mehdi.gymmanager.model.*;
import com.ibrahim.mehdi.gymmanager.service.GymService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Map;

/**
 * Main GUI Application - Simple Working Version
 */
public class GymManagerApp extends JFrame {
    private static final long serialVersionUID = 1L;
    
    private static final Color PRIMARY_COLOR = new Color(33, 150, 243);
    private static final Color SUCCESS_COLOR = new Color(76, 175, 80);
    private static final Color DANGER_COLOR = new Color(244, 67, 54);
    private static final Color WARNING_COLOR = new Color(255, 152, 0);
    private static final Color INFO_COLOR = new Color(0, 188, 212);
    
    private GymService gymService;
    private JTabbedPane tabbedPane;
    
    private JTextField memberNameField, memberSurnameField, memberPhoneField, memberEmailField;
    private JComboBox<Member.MembershipType> memberTypeCombo;
    private JLabel priceLabel;
    private JTable memberTable;
    private DefaultTableModel memberTableModel;
    
    private JTextField appointmentMemberIdField, appointmentServiceField;
    private JSpinner appointmentPrioritySpinner;
    private JTable appointmentTable;
    private DefaultTableModel appointmentTableModel;
    
    private JTextField equipNameField, equipTypeField, equipQtyField;
    private JSpinner equipXSpinner, equipYSpinner;
    private JTable equipmentTable;
    private DefaultTableModel equipmentTableModel;
    
    private JTextField queueMemberIdField;
    private JTextArea queueDisplayArea;
    
    private JTextArea historyDisplayArea;
    private JTextArea statsDisplayArea;
    
    public GymManagerApp() {
        gymService = new GymService();
        initializeGUI();
    }
    
    private void initializeGUI() {
        setTitle("Gym Manager - Data Structures Project");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 12));
        
        tabbedPane.addTab("Members", createMembersPanel());
        tabbedPane.addTab("Appointments", createAppointmentsPanel());
        tabbedPane.addTab("Equipment", createEquipmentPanel());
        tabbedPane.addTab("Queue", createQueuePanel());
        tabbedPane.addTab("History", createHistoryPanel());
        tabbedPane.addTab("Search", createSearchPanel());
        tabbedPane.addTab("Statistics", createStatisticsPanel());
        tabbedPane.addTab("About", createAboutPanel());
        
        add(tabbedPane);
        refreshAllDisplays();
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setForeground(Color.BLACK);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(150, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setBorderPainted(true);
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    private JPanel createMembersPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Add New Member"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        memberNameField = new JTextField(15);
        formPanel.add(memberNameField, gbc);
        
        gbc.gridx = 2;
        formPanel.add(new JLabel("Surname:"), gbc);
        gbc.gridx = 3;
        memberSurnameField = new JTextField(15);
        formPanel.add(memberSurnameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        memberPhoneField = new JTextField(15);
        formPanel.add(memberPhoneField, gbc);
        
        gbc.gridx = 2;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 3;
        memberEmailField = new JTextField(15);
        formPanel.add(memberEmailField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Membership:"), gbc);
        gbc.gridx = 1;
        memberTypeCombo = new JComboBox<Member.MembershipType>(Member.MembershipType.values());
        memberTypeCombo.setRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Member.MembershipType) {
                    Member.MembershipType type = (Member.MembershipType) value;
                    setText(type.getDescription());
                }
                return this;
            }
        });
        memberTypeCombo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updatePriceLabel();
            }
        });
        formPanel.add(memberTypeCombo, gbc);
        
        gbc.gridx = 2;
        formPanel.add(new JLabel("Price:"), gbc);
        gbc.gridx = 3;
        priceLabel = new JLabel("1,000 TL");
        priceLabel.setFont(new Font("Arial", Font.BOLD, 18));
        priceLabel.setForeground(SUCCESS_COLOR);
        formPanel.add(priceLabel, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton addButton = createStyledButton("Add Member", SUCCESS_COLOR);
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addMember();
            }
        });
        buttonPanel.add(addButton);
        
        JButton deleteButton = createStyledButton("Delete Selected", DANGER_COLOR);
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteMember();
            }
        });
        buttonPanel.add(deleteButton);
        
        JButton refreshButton = createStyledButton("Refresh", PRIMARY_COLOR);
        refreshButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                refreshMemberTable();
            }
        });
        buttonPanel.add(refreshButton);
        
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 4;
        formPanel.add(buttonPanel, gbc);
        
        panel.add(formPanel, BorderLayout.NORTH);
        
        String[] columns = {"ID", "Name", "Surname", "Phone", "Email", "Type", "Price", "Expires", "Active"};
        memberTableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) { 
                return false; 
            }
        };
        memberTable = new JTable(memberTableModel);
        memberTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        memberTable.setRowHeight(25);
        
        panel.add(new JScrollPane(memberTable), BorderLayout.CENTER);
        
        return panel;
    }
    
    private void updatePriceLabel() {
        Member.MembershipType selected = (Member.MembershipType) memberTypeCombo.getSelectedItem();
        if (selected != null) {
            priceLabel.setText(String.format("%,d TL", selected.getPrice()));
        }
    }
    
    private JPanel createAppointmentsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        formPanel.add(new JLabel("Member ID:"));
        appointmentMemberIdField = new JTextField();
        formPanel.add(appointmentMemberIdField);
        
        formPanel.add(new JLabel("Service:"));
        appointmentServiceField = new JTextField();
        formPanel.add(appointmentServiceField);
        
        formPanel.add(new JLabel("Priority (1-10):"));
        appointmentPrioritySpinner = new JSpinner(new SpinnerNumberModel(5, 1, 10, 1));
        formPanel.add(appointmentPrioritySpinner);
        
        JButton createButton = createStyledButton("Create", SUCCESS_COLOR);
        createButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createAppointment();
            }
        });
        formPanel.add(createButton);
        
        JButton processButton = createStyledButton("Process Next", PRIMARY_COLOR);
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
        appointmentTable.setRowHeight(25);
        panel.add(new JScrollPane(appointmentTable), BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createEquipmentPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
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
        
        JButton addButton = createStyledButton("Add Equipment", SUCCESS_COLOR);
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addEquipment();
            }
        });
        formPanel.add(addButton);
        
        JButton refreshButton = createStyledButton("Refresh", PRIMARY_COLOR);
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
        equipmentTable.setRowHeight(25);
        panel.add(new JScrollPane(equipmentTable), BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createQueuePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        formPanel.add(new JLabel("Member ID:"));
        queueMemberIdField = new JTextField(10);
        formPanel.add(queueMemberIdField);
        
        JButton addButton = createStyledButton("Add to Queue", SUCCESS_COLOR);
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addToQueue();
            }
        });
        formPanel.add(addButton);
        
        JButton processButton = createStyledButton("Process Next", PRIMARY_COLOR);
        processButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                processFromQueue();
            }
        });
        formPanel.add(processButton);
        
        JButton refreshButton = createStyledButton("Refresh", INFO_COLOR);
        refreshButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                refreshQueueDisplay();
            }
        });
        formPanel.add(refreshButton);
        
        panel.add(formPanel, BorderLayout.NORTH);
        
        queueDisplayArea = new JTextArea();
        queueDisplayArea.setEditable(false);
        queueDisplayArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        panel.add(new JScrollPane(queueDisplayArea), BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton backButton = createStyledButton("Back", PRIMARY_COLOR);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                navigateHistory(false);
            }
        });
        buttonPanel.add(backButton);
        
        JButton forwardButton = createStyledButton("Forward", PRIMARY_COLOR);
        forwardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                navigateHistory(true);
            }
        });
        buttonPanel.add(forwardButton);
        
        JButton undoButton = createStyledButton("Undo", WARNING_COLOR);
        undoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                performUndo();
            }
        });
        buttonPanel.add(undoButton);
        
        panel.add(buttonPanel, BorderLayout.NORTH);
        
        historyDisplayArea = new JTextArea();
        historyDisplayArea.setEditable(false);
        historyDisplayArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        panel.add(new JScrollPane(historyDisplayArea), BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        formPanel.add(new JLabel("Search Name:"));
        final JTextField searchField = new JTextField(25);
        formPanel.add(searchField);
        
        final JTextArea resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JButton searchButton = createStyledButton("Search (KMP)", PRIMARY_COLOR);
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String term = searchField.getText().trim();
                if (!term.isEmpty()) {
                    List<Member> results = gymService.searchMemberByName(term);
                    StringBuilder sb = new StringBuilder();
                    sb.append("KMP Search: \"").append(term).append("\"\n\n");
                    sb.append("Found ").append(results.size()).append(" member(s)\n\n");
                    for (Member m : results) {
                        sb.append(m.toString()).append("\n\n");
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
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JButton refreshButton = createStyledButton("Refresh Statistics", SUCCESS_COLOR);
        refreshButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                refreshStatistics();
            }
        });
        panel.add(refreshButton, BorderLayout.NORTH);
        
        statsDisplayArea = new JTextArea();
        statsDisplayArea.setEditable(false);
        statsDisplayArea.setFont(new Font("Monospaced", Font.BOLD, 13));
        panel.add(new JScrollPane(statsDisplayArea), BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createAboutPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JTextArea aboutText = new JTextArea();
        aboutText.setEditable(false);
        aboutText.setFont(new Font("SansSerif", Font.PLAIN, 13));
        aboutText.setText(
            "====================================================\n" +
            "     GYM MANAGER - DATA STRUCTURES PROJECT         \n" +
            "====================================================\n\n" +
            "Course: CEN207 / CE205 Data Structures\n" +
            "Term: Fall 2025-2026\n\n" +
            "COURSE INSTRUCTOR:\n" +
            "  Asst. Prof. Dr. Ugur CORUH\n\n" +
            "PROJECT TEAM:\n" +
            "  Ibrahim Demirci\n" +
            "  Muhammed Mehdi Karagulle\n\n" +
            "All 12 Data Structures Implemented\n"
        );
        
        panel.add(new JScrollPane(aboutText), BorderLayout.CENTER);
        return panel;
    }
    
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
            
            JOptionPane.showMessageDialog(this, 
                "Member Added!\n" + m.getFullName() + "\nPrice: " + m.getMembershipPrice() + " TL");
            
            memberNameField.setText("");
            memberSurnameField.setText("");
            memberPhoneField.setText("");
            memberEmailField.setText("");
            
            refreshMemberTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
    
    private void deleteMember() {
        int selectedRow = memberTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a member!");
            return;
        }
        
        int memberId = (Integer) memberTableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Delete member ID " + memberId + "?");
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (gymService.deleteMember(memberId)) {
                JOptionPane.showMessageDialog(this, "Member deleted!");
                refreshMemberTable();
            }
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
    
    private void refreshMemberTable() {
        memberTableModel.setRowCount(0);
        for (Member m : gymService.getAllMembers()) {
            memberTableModel.addRow(new Object[]{
                m.getId(), m.getName(), m.getSurname(), 
                m.getPhoneNumber(), m.getEmail(), m.getMembershipType(),
                m.getMembershipPrice() + " TL", m.getMembershipEndDate(),
                m.isActive() ? "Yes" : "No"
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
        queueDisplayArea.setText("Queue Size: " + gymService.getQueueSize());
    }
    
    private void refreshHistoryDisplay() {
        historyDisplayArea.setText("Current: " + gymService.getCurrentHistory());
    }
    
    private void refreshStatistics() {
        Map<String, Object> stats = gymService.getStatistics();
        StringBuilder sb = new StringBuilder("STATISTICS\n\n");
        for (Map.Entry<String, Object> entry : stats.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
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
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                GymManagerApp app = new GymManagerApp();
                app.setVisible(true);
            }
        });
    }
}