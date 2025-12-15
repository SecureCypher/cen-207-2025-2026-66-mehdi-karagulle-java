/**
 * @file GymManagerApp.java
 * @brief Main GUI application for Gym Management System - FINAL VERSION
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
 * Main GUI Application - FINAL VERSION
 */
public class GymManagerApp extends JFrame {
    private static final long serialVersionUID = 1L;
    
    // Modern color scheme
    private static final Color PRIMARY_COLOR = new Color(33, 150, 243);      // Blue
    private static final Color SUCCESS_COLOR = new Color(76, 175, 80);       // Green
    private static final Color DANGER_COLOR = new Color(244, 67, 54);        // Red
    private static final Color WARNING_COLOR = new Color(255, 152, 0);       // Orange
    private static final Color INFO_COLOR = new Color(0, 188, 212);          // Cyan
    private static final Color DARK_COLOR = new Color(66, 66, 66);           // Dark Gray
    
    private GymService gymService;
    private JTabbedPane tabbedPane;
    
    // Member tab
    private JTextField memberNameField, memberSurnameField, memberPhoneField, memberEmailField;
    private JComboBox<Member.MembershipType> memberTypeCombo;
    private JLabel priceLabel;
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
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 12));
        
        tabbedPane.addTab("👥 Members", createMembersPanel());
        tabbedPane.addTab("📅 Appointments", createAppointmentsPanel());
        tabbedPane.addTab("🏋️ Equipment", createEquipmentPanel());
        tabbedPane.addTab("⏳ Queue", createQueuePanel());
        tabbedPane.addTab("📜 History", createHistoryPanel());
        tabbedPane.addTab("🔍 Search", createSearchPanel());
        tabbedPane.addTab("📊 Statistics", createStatisticsPanel());
        tabbedPane.addTab("ℹ️ About", createAboutPanel());
        
        add(tabbedPane);
        refreshAllDisplays();
    }
    
    /**
     * Create styled button
     */
    private JButton createStyledButton(String text, Color bgColor, String icon) {
        JButton button = new JButton(icon + " " + text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
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
        panel.setBackground(Color.WHITE);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
            "Add New Member",
            0, 0,
            new Font("Arial", Font.BOLD, 14),
            PRIMARY_COLOR
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Row 0: Name & Surname
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
        
        // Row 1: Phone & Email
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
        
        // Row 2: Membership & Price
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
        priceLabel = new JLabel("1,000₺");
        priceLabel.setFont(new Font("Arial", Font.BOLD, 18));
        priceLabel.setForeground(SUCCESS_COLOR);
        formPanel.add(priceLabel, gbc);
        
        // Row 3: Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton addButton = createStyledButton("Add Member", SUCCESS_COLOR, "➕");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addMember();
            }
        });
        buttonPanel.add(addButton);
        
        JButton deleteButton = createStyledButton("Delete Selected", DANGER_COLOR, "🗑️");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteMember();
            }
        });
        buttonPanel.add(deleteButton);
        
        JButton refreshButton = createStyledButton("Refresh", PRIMARY_COLOR, "🔄");
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
        
        // Table
        String[] columns = {"ID", "Name", "Surname", "Phone", "Email", "Type", "Price", "Expires", "Active"};
        memberTableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) { 
                return false; 
            }
        };
        memberTable = new JTable(memberTableModel);
        memberTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        memberTable.setRowHeight(25);
        memberTable.setFont(new Font("Arial", Font.PLAIN, 12));
        memberTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        memberTable.getTableHeader().setBackground(PRIMARY_COLOR);
        memberTable.getTableHeader().setForeground(Color.WHITE);
        
        // Center align numeric columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        memberTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        memberTable.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);
        memberTable.getColumnModel().getColumn(8).setCellRenderer(centerRenderer);
        
        JScrollPane scrollPane = new JScrollPane(memberTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Info panel
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.setBackground(new Color(255, 248, 225));
        JLabel infoLabel = new JLabel("💡 Select a member row and click 'Delete Selected' to remove");
        infoLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        infoPanel.add(infoLabel);
        panel.add(infoPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void updatePriceLabel() {
        Member.MembershipType selected = (Member.MembershipType) memberTypeCombo.getSelectedItem();
        if (selected != null) {
            String price = String.format("%,d₺", selected.getPrice());
            priceLabel.setText(price);
        }
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
        
        formPanel.add(new JLabel("Priority (1-10):"));
        appointmentPrioritySpinner = new JSpinner(new SpinnerNumberModel(5, 1, 10, 1));
        formPanel.add(appointmentPrioritySpinner);
        
        JButton createButton = createStyledButton("Create", SUCCESS_COLOR, "➕");
        createButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createAppointment();
            }
        });
        formPanel.add(createButton);
        
        JButton processButton = createStyledButton("Process Next", PRIMARY_COLOR, "▶️");
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
        
        JButton addButton = createStyledButton("Add Equipment", SUCCESS_COLOR, "➕");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addEquipment();
            }
        });
        formPanel.add(addButton);
        
        JButton refreshButton = createStyledButton("Refresh", PRIMARY_COLOR, "🔄");
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
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        formPanel.add(new JLabel("Member ID:"));
        queueMemberIdField = new JTextField(10);
        formPanel.add(queueMemberIdField);
        
        JButton addButton = createStyledButton("Add to Queue", SUCCESS_COLOR, "➕");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addToQueue();
            }
        });
        formPanel.add(addButton);
        
        JButton processButton = createStyledButton("Process Next", PRIMARY_COLOR, "▶️");
        processButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                processFromQueue();
            }
        });
        formPanel.add(processButton);
        
        JButton refreshButton = createStyledButton("Refresh", INFO_COLOR, "🔄");
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
        queueDisplayArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(new JScrollPane(queueDisplayArea), BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton backButton = createStyledButton("Back", PRIMARY_COLOR, "◀");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                navigateHistory(false);
            }
        });
        buttonPanel.add(backButton);
        
        JButton forwardButton = createStyledButton("Forward", PRIMARY_COLOR, "▶");
        forwardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                navigateHistory(true);
            }
        });
        buttonPanel.add(forwardButton);
        
        JButton undoButton = createStyledButton("Undo", WARNING_COLOR, "↶");
        undoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                performUndo();
            }
        });
        buttonPanel.add(undoButton);
        
        JButton refreshButton = createStyledButton("Refresh", INFO_COLOR, "🔄");
        refreshButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                refreshHistoryDisplay();
            }
        });
        buttonPanel.add(refreshButton);
        
        panel.add(buttonPanel, BorderLayout.NORTH);
        
        historyDisplayArea = new JTextArea();
        historyDisplayArea.setEditable(false);
        historyDisplayArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        historyDisplayArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(new JScrollPane(historyDisplayArea), BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        formPanel.add(new JLabel("Search Name:"));
        final JTextField searchField = new JTextField(25);
        formPanel.add(searchField);
        
        final JTextArea resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        resultArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton searchButton = createStyledButton("Search (KMP)", PRIMARY_COLOR, "🔍");
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String term = searchField.getText().trim();
                if (!term.isEmpty()) {
                    List<Member> results = gymService.searchMemberByName(term);
                    StringBuilder sb = new StringBuilder();
                    sb.append("🔍 KMP Search: \"").append(term).append("\"\n");
                    sb.append("═══════════════════════════════════\n\n");
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
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton refreshButton = createStyledButton("Refresh Statistics", SUCCESS_COLOR, "📊");
        refreshButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                refreshStatistics();
            }
        });
        panel.add(refreshButton, BorderLayout.NORTH);
        
        statsDisplayArea = new JTextArea();
        statsDisplayArea.setEditable(false);
        statsDisplayArea.setFont(new Font("Monospaced", Font.BOLD, 13));
        statsDisplayArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
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
            "╔═══════════════════════════════════════════════════════╗\n" +
            "║     GYM MANAGER - DATA STRUCTURES PROJECT            ║\n" +
            "╚═══════════════════════════════════════════════════════╝\n\n" +
            "📚 All 12 Data Structures:\n\n" +
            "  1. ⛓️  Double Linked List - Member history navigation\n" +
            "  2. 🔗 XOR Linked List - Workout history\n" +
            "  3. 🗺️  Sparse Matrix - Equipment location map\n" +
            "  4. 📚 Stack - Undo operations\n" +
            "  5. 📋 Queue - Waiting member queue\n" +
            "  6. 🏔️  Min Heap - Priority appointments\n" +
            "  7. #️⃣  Hash Table - Fast member lookup\n" +
            "  8. 🌐 BFS/DFS - Graph traversal\n" +
            "  9. 🔄 SCC - Strongly Connected Components\n" +
            "  10. 🔍 KMP - Pattern matching search\n" +
            "  11. 🗜️  Huffman - Data compression\n" +
            "  12. 🌳 B+ Tree - Member indexing\n" +
            "  13. ⚡ Linear Probing - File operations\n\n" +
            "💰 MEMBERSHIP PRICES:\n\n" +
            "  • MONTHLY   : 1,000₺  (1 month)\n" +
            "  • QUARTERLY : 2,700₺  (3 months - 10% discount)\n" +
            "  • YEARLY    : 9,600₺  (12 months - 20% discount)\n" +
            "  • VIP       : 15,000₺ (12 months + extras)\n\n" +
            "📅 CEN207/CE205 - Fall 2025-2026\n" +
            "👨‍💻 Developers: İbrahim Demirci & Muhammed Mehdi Karagülle\n" +
            "🏫 Institution: Recep Tayyip Erdoğan University\n"
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
                JOptionPane.showMessageDialog(this, 
                    "❌ Name and Surname are required!",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Member.MembershipType type = (Member.MembershipType) memberTypeCombo.getSelectedItem();
            Member m = gymService.addMember(name, surname, 
                memberPhoneField.getText().trim(),
                memberEmailField.getText().trim(), type);
            
            JOptionPane.showMessageDialog(this, 
                "✅ Member Added Successfully!\n\n" +
                "Name: " + m.getFullName() + "\n" +
                "Type: " + m.getMembershipType() + "\n" +
                "Price: " + String.format("%,d₺", m.getMembershipPrice()),
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            
            // Clear form
            memberNameField.setText("");
            memberSurnameField.setText("");
            memberPhoneField.setText("");
            memberEmailField.setText("");
            memberTypeCombo.setSelectedIndex(0);
            
            refreshMemberTable();
            refreshAllDisplays();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "❌ Error: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteMember() {
        int selectedRow = memberTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "⚠️ Please select a member from the table first!",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            int memberId = (Integer) memberTableModel.getValueAt(selectedRow, 0);
            String memberName = memberTableModel.getValueAt(selectedRow, 1) + " " + 
                               memberTableModel.getValueAt(selectedRow, 2);
            
            int confirm = JOptionPane.showConfirmDialog(this,
                "🗑️ Are you sure you want to delete this member?\n\n" +
                "Name: " + memberName + "\n" +
                "ID: " + memberId + "\n\n" +
                "This action cannot be undone!",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
            
            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = gymService.deleteMember(memberId);
                if (success) {
                    JOptionPane.showMessageDialog(this, 
                        "✅ Member deleted successfully!\n\n" +
                        "Deleted: " + memberName,
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                    refreshMemberTable();
                    refreshAllDisplays();
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "❌ Failed to delete member!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "❌ Error during deletion: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void createAppointment() {
        try {
            int memberId = Integer.parseInt(appointmentMemberIdField.getText().trim());
            String service = appointmentServiceField.getText().trim();
            int priority = ((Number) appointmentPrioritySpinner.getValue()).intValue();
            
            Appointment apt = gymService.createAppointment(memberId, service, priority);
            if (apt != null) {
                JOptionPane.showMessageDialog(this, "✅ Appointment created!");
                appointmentMemberIdField.setText("");
                appointmentServiceField.setText("");
                refreshAppointmentDisplay();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Member not found!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Invalid input!");
        }
    }
    
    private void processNextAppointment() {
        Appointment apt = gymService.processNextAppointment();
        if (apt != null) {
            JOptionPane.showMessageDialog(this, "✅ Processed: " + apt.getMemberName());
            refreshAppointmentDisplay();
        } else {
            JOptionPane.showMessageDialog(this, "ℹ️ No pending appointments!");
        }
    }
    
    private void addEquipment() {
        try {
            String name = equipNameField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "❌ Equipment name required!");
                return;
            }
            
            int qty = Integer.parseInt(equipQtyField.getText().trim());
            int x = ((Number) equipXSpinner.getValue()).intValue();
            int y = ((Number) equipYSpinner.getValue()).intValue();
            
            gymService.addEquipment(name, equipTypeField.getText().trim(), qty, x, y);
            JOptionPane.showMessageDialog(this, "✅ Equipment added!");
            equipNameField.setText("");
            equipTypeField.setText("");
            equipQtyField.setText("");
            refreshEquipmentTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Error: " + ex.getMessage());
        }
    }
    
    private void addToQueue() {
        try {
            int memberId = Integer.parseInt(queueMemberIdField.getText().trim());
            Member m = gymService.searchMember(memberId);
            if (m != null) {
                gymService.addToWaitingQueue(m);
                JOptionPane.showMessageDialog(this, "✅ Added: " + m.getFullName());
                queueMemberIdField.setText("");
                refreshQueueDisplay();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Member not found!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Invalid Member ID!");
        }
    }
    
    private void processFromQueue() {
        Member m = gymService.processNextInQueue();
        if (m != null) {
            JOptionPane.showMessageDialog(this, "✅ Processed: " + m.getFullName());
            refreshQueueDisplay();
        } else {
            JOptionPane.showMessageDialog(this, "ℹ️ Queue is empty!");
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
            JOptionPane.showMessageDialog(this, "↶ Undone: " + action);
            refreshAllDisplays();
        } else {
            JOptionPane.showMessageDialog(this, "ℹ️ Nothing to undo!");
        }
    }
    
    // Refresh methods
    private void refreshMemberTable() {
        memberTableModel.setRowCount(0);
        for (Member m : gymService.getAllMembers()) {
            memberTableModel.addRow(new Object[]{
                m.getId(), 
                m.getName(), 
                m.getSurname(), 
                m.getPhoneNumber(), 
                m.getEmail(), 
                m.getMembershipType(),
                String.format("%,d₺", m.getMembershipPrice()),
                m.getMembershipEndDate(),
                m.isActive() ? "✓" : "✗"
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
            "╔════════════════════════════════╗\n" +
            "║   WAITING QUEUE (FIFO)         ║\n" +
            "╚════════════════════════════════╝\n\n" +
            "Queue Size: %d member(s)\n",
            gymService.getQueueSize()
        ));
    }
    
    private void refreshHistoryDisplay() {
        historyDisplayArea.setText(String.format(
            "╔════════════════════════════════╗\n" +
            "║   HISTORY (Double Linked List) ║\n" +
            "╚════════════════════════════════╝\n\n" +
            "Current: %s\n",
            gymService.getCurrentHistory()
        ));
    }
    
    private void refreshStatistics() {
        Map<String, Object> stats = gymService.getStatistics();
        StringBuilder sb = new StringBuilder();
        sb.append("╔══════════════════════════════════════╗\n");
        sb.append("║          SYSTEM STATISTICS            ║\n");
        sb.append("╚══════════════════════════════════════╝\n\n");
        for (Map.Entry<String, Object> entry : stats.entrySet()) {
            sb.append(String.format("%-25s: %s\n", entry.getKey(), entry.getValue()));
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