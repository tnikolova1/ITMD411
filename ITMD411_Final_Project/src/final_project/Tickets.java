//Final Project Teodora Nikolova
package final_project;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class Tickets extends JFrame implements ActionListener {

	// class level member objects
	Dao dao = new Dao(); // for CRUD operations
	Boolean chkIfAdmin = null;
	String curUser;

	// Main menu object items
	private JMenu mnuFile = new JMenu("File");
	private JMenu mnuAdmin = new JMenu("Admin");
	private JMenu mnuTickets = new JMenu("Tickets");

	// Sub menu item objects for all Main menu item objects
	JMenuItem mnuItemExit;
	JMenuItem mnuItemUpdate;
	JMenuItem mnuItemDelete;
	JMenuItem mnuItemOpenTicket;
	JMenuItem mnuItemViewTicket;

	public Tickets(Boolean isAdmin, String usid) { 

		chkIfAdmin = isAdmin;
		curUser = usid;
		createMenu();
		prepareGUI();
		
		try {

			
			JTable jt = new JTable(ticketsJTable.buildTableModel(dao.readTickets(curUser)));
				jt.setBounds(30, 40, 200, 400);
				jt.setBackground(Color.gray);
		        jt.setForeground(Color.white);
		        jt.getTableHeader().setBackground(Color.BLACK);
		        jt.getTableHeader().setForeground(Color.white);
				JScrollPane sp = new JScrollPane(jt);
				add(sp);
				setVisible(true); 
				System.out.println("Ticket view sucessfully created.");
				chkIfAdmin = true;
		} catch (SQLException e1) {
			System.out.println("Ticket view failed.");
			e1.printStackTrace();
		}
	}
	
	public void remakeJTable() {
		try {

			
			
			JTable jt = new JTable(ticketsJTable.buildTableModel(dao.readTickets(curUser)));
				jt.setBounds(30, 40, 200, 400);
				jt.setBackground(Color.gray);
		        jt.setForeground(Color.white);
		        jt.getTableHeader().setBackground(Color.BLACK);
		        jt.getTableHeader().setForeground(Color.white);
				JScrollPane sp = new JScrollPane(jt);
				add(sp);
				setVisible(true); 
				System.out.println("Ticket view sucessfully created.");
				chkIfAdmin = true;
		} catch (SQLException e1) {
			System.out.println("Ticket view failed.");
			e1.printStackTrace();
		}
	}

	private void createMenu() {

		/* Initialize sub menu items **************************************/

		// initialize sub menu item for File main menu
		mnuItemExit = new JMenuItem("Exit");
		// add to File main menu item
		mnuFile.add(mnuItemExit);
		
		// initialize first sub menu item for Tickets main menu
		mnuItemOpenTicket = new JMenuItem("Open Ticket");
		// add to Ticket Main menu item
		mnuTickets.add(mnuItemOpenTicket);

		// initialize second sub menu items for Tickets main menu
		mnuItemUpdate = new JMenuItem("Update Ticket");
		// add to Ticket main menu item
		mnuAdmin.add(mnuItemUpdate);

		// initialize third sub menu items for Tickets main menu
		mnuItemDelete = new JMenuItem("Delete Ticket");
		// add to Ticket main menu item
		mnuAdmin.add(mnuItemDelete);

		// initialize fourth sub menu item for Tickets main menu
		mnuItemViewTicket = new JMenuItem("View Tickets");
		// add to Ticket Main menu item
		mnuTickets.add(mnuItemViewTicket);

		// initialize any more desired sub menu items below

		/* Add action listeners for each desired menu item *************/
		mnuItemExit.addActionListener(this);
		mnuItemUpdate.addActionListener(this);
		mnuItemDelete.addActionListener(this);
		mnuItemOpenTicket.addActionListener(this);
		mnuItemViewTicket.addActionListener(this);

		// add any more listeners for any additional sub menu items if desired

	}

	private void prepareGUI() {

		
		JMenuBar bar = new JMenuBar();
		bar.add(mnuFile); 
		if(chkIfAdmin) {
			bar.add(mnuAdmin); 
		}
		bar.add(mnuTickets);
		setJMenuBar(bar);
		addWindowListener(new WindowAdapter() {
		
		public void windowClosing(WindowEvent wE) {
		    System.exit(0);
		}
		});
		// set frame options
		setSize(400, 400);
		getContentPane().setBackground(Color.LIGHT_GRAY);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		String act = "exit"; 
		
		if(e.getSource() == mnuItemOpenTicket){
			act = "openTicket";
		} else if (e.getSource() == mnuItemUpdate) {
			act = "updateTicket";
		} else if (e.getSource() == mnuItemDelete) {
			act = "deleteTicket";
		} else if (e.getSource() == mnuItemViewTicket) {
			act = "viewTicket";
		}
		
		switch(act) {
			case "openTicket":
				String ticketDesc = JOptionPane.showInputDialog(null, "Enter a description for your ticket");
				if(ticketDesc != null) {
					dao.insertTicket(curUser,ticketDesc); 
					remakeJTable();
				}
				break;
			case "updateTicket":
				String ticketID = JOptionPane.showInputDialog(null, "Enter the ID of the ticket you want to update");
				if(ticketID != null) {
					String newDesc = JOptionPane.showInputDialog(null, "Enter a new description for your ticket");
					if(newDesc != null) {
						dao.updateTicket(curUser, Integer.parseInt(ticketID), newDesc); 
						remakeJTable();
					}
				}
				break;
			case "deleteTicket":
				String tid2 = JOptionPane.showInputDialog(null, "Which ticket do you want to delete?");
				if(tid2 != null) {
					dao.deleteTicket(curUser, Integer.parseInt(tid2)); 
					remakeJTable();
				}
				break;
			case "viewTicket":
				String tid3 = JOptionPane.showInputDialog(null, "Which ticket do you want to view?");
				if(tid3 != null) {
					String viewT = dao.readTicket(curUser, Integer.parseInt(tid3)); //User Permission
					JOptionPane.showMessageDialog(null, viewT); //Use dialog to display the searched Ticket
				}
				break;
			default: 
				System.exit(0);
		}

	}

}
