import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

public class Team5Client extends JFrame implements ActionListener {
	/**
	 * Author - Team 5
	 */
	private static final long serialVersionUID = 1L;

	JPanel panel1;
	JToggleButton a[], b[], c[], d[];
	JFrame home;
	JFrame register;
	JFrame login;
	JFrame flight;
	JLabel lb1, lb2, lb3, lb4, lb5, lb6, lb7, lb8, lb9, lb10, lb11, lb12, lb13, user, pwd;
	JButton btLogin, btReg, bt1, bt2, bt3, bt4, bt5, submit, viewReservations, logout, viewSeats;
	JTextField fd1, fd2, fd3, fd5, fd6, fdLogin;
	JComboBox<String> cb1;
	JPasswordField pfd1, pfd2, pfdLogin;
	ArrayList<String> seatsAvailable = new ArrayList<String>();
	ArrayList<String> seatsSelected = new ArrayList<String>();
	String clientFlag = null;
	String input = "";
	int seatCount = 0;
	String activeUser = null;

	Team5Client() {
		home = new JFrame("Home"); // Frame for home page
		home.setLayout(null);
		home.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		lb3 = new JLabel("Welcome to UIC Airlines"); // Label for UIC Airlines
		lb3.setForeground(Color.BLUE);
		lb3.setFont(new Font("Calibri", Font.BOLD, 30));

		lb4 = new JLabel("Flight 919 !");// Label for Flight name
		lb4.setForeground(Color.BLUE);
		lb4.setFont(new Font("Calibri", Font.BOLD, 30));

		btLogin = new JButton("Login to book"); // Login button
		btLogin.setForeground(Color.BLACK);
		btLogin.setFont(new Font("Calibri", Font.ITALIC, 20));
		btLogin.addActionListener(this);

		btReg = new JButton("Register as new user"); // Register button
		btReg.setForeground(Color.BLACK);
		btReg.setFont(new Font("Calibri", Font.ITALIC, 20));
		btReg.addActionListener(this);

		// Setting bounds for labels
		lb3.setBounds(150, 60, 400, 30);
		lb4.setBounds(200, 100, 400, 40);
		btLogin.setBounds(50, 250, 200, 30);
		btReg.setBounds(320, 250, 250, 30);

		// Adding components to the home frame
		home.add(lb3);
		home.add(lb4);
		home.add(btLogin);
		home.add(btReg);
		home.setSize(600, 600); // setting width and height of frame
		home.setVisible(true); // home frame is set to visible

	}

	// method to reserve seats
	public void seatSelection() {
		flight = new JFrame("flight");// label for flight
		flight.setLayout(null);
		flight.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		lb11 = new JLabel("Reserve your airline seat !");// label
		lb11.setForeground(Color.BLUE); // setting label bg color
		lb11.setFont(new Font("Calibri", Font.BOLD, 30)); // setting font size,
															// font style
															// -properties for
															// label
		lb11.setBounds(150, 60, 400, 30);
		submit = new JButton("Submit"); // submit button
		submit.setForeground(Color.BLACK);
		submit.setFont(new Font("Calibri", Font.ITALIC, 20));
		logout = new JButton("Logout"); // logout button
		logout.setForeground(Color.BLACK);
		logout.setFont(new Font("Calibri", Font.ITALIC, 20));
		viewSeats = new JButton("View Seats"); // logout button
		viewSeats.setForeground(Color.BLACK);
		viewSeats.setFont(new Font("Calibri", Font.ITALIC, 20));

		submit.setBounds(200, 140, 200, 30); // bounds for submit button
		logout.setBounds(800, 60, 200, 30); // bounds for logout button
		viewSeats.setBounds(400, 140, 200, 30);// bounds for view Seats button
		submit.addActionListener(this); // adding action listeners for buttons
		logout.addActionListener(this);
		viewSeats.addActionListener(this);

		a = new JToggleButton[20]; // Allocating size of grid
		b = new JToggleButton[20];
		c = new JToggleButton[20];
		d = new JToggleButton[20];

		// panel for adding seats
		panel1 = new JPanel(new GridLayout(4, 0));
		panel1.setBounds(40, 190, 1100, 300);

		// Creating seats for the airline
		for (int j = 0; j < 20; j++) {
			a[j] = new JToggleButton("a" + (j + 1)); // creates new
														// button
			a[j].addActionListener(this);
			a[j].setBackground(Color.CYAN);
			seatsAvailable.add("a" + (j + 1));
			panel1.add(a[j]);
		}

		for (int j = 0; j < 20; j++) {
			b[j] = new JToggleButton("b" + (j + 1)); // creates new
														// button
			b[j].addActionListener(this);
			b[j].setBackground(Color.CYAN);
			seatsAvailable.add("b" + (j + 1));
			panel1.add(b[j]);
		}

		for (int j = 0; j < 20; j++) {
			c[j] = new JToggleButton("c" + (j + 1)); // creates new
														// button
			c[j].addActionListener(this);
			c[j].setBackground(Color.CYAN);
			seatsAvailable.add("c" + (j + 1));
			panel1.add(c[j]);
		}

		for (int j = 0; j < 20; j++) {
			d[j] = new JToggleButton("d" + (j + 1)); // creates new
														// button
			d[j].addActionListener(this);
			d[j].setBackground(Color.CYAN);
			seatsAvailable.add("d" + (j + 1));
			panel1.add(d[j]);
		}
		// compare and remove selected seats
		for (String seats1 : seatsSelected) {
			// set the toggle state for selected seats
			for (int i = 0; i < 20; i++) {
				if (a[i].getText().equals(seats1)) {
					a[i].setEnabled(false);
				}
				if (b[i].getText().equals(seats1)) {
					b[i].setEnabled(false);
				}
				if (c[i].getText().equals(seats1)) {
					c[i].setEnabled(false);
				}
				if (d[i].getText().equals(seats1)) {
					d[i].setEnabled(false);
				}
				if (!a[i].isEnabled()) {
					a[i].setBackground(Color.RED);
				}
				if (!b[i].isEnabled()) {
					b[i].setBackground(Color.RED);
				}
				if (!c[i].isEnabled()) {
					c[i].setBackground(Color.RED);
				}
				if (!d[i].isEnabled()) {
					d[i].setBackground(Color.RED);
				}
			}
		}

		// adding components to frame
		flight.add(lb11);
		flight.add(panel1);
		flight.add(submit);
		flight.add(logout);
		flight.add(viewSeats);
		flight.setSize(1300, 1300);
		flight.setVisible(true);
	}

	// method to login users
	public void login() {
		login = new JFrame("Login"); // Frame for login
		login.setLayout(null);
		login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		lb10 = new JLabel("Login Page"); // Label for login
		lb10.setForeground(Color.BLUE);
		lb10.setFont(new Font("Calibri", Font.BOLD, 20));

		user = new JLabel("User ID:"); // Label for userID
		user.setForeground(Color.BLUE);
		user.setFont(new Font("Calibri", Font.BOLD, 20));

		pwd = new JLabel("Password:");// Label for password
		pwd.setForeground(Color.BLUE);
		pwd.setFont(new Font("Calibri", Font.BOLD, 20));

		lb13 = new JLabel("New User? Register Here");
		lb13.setForeground(Color.BLUE);
		lb13.setFont(new Font("Calibri", Font.BOLD, 20));

		fdLogin = new JTextField(); // User name Text field
		pfdLogin = new JPasswordField();// Password Text field
		bt3 = new JButton("Login"); // Login Button
		bt3.addActionListener(this);
		bt4 = new JButton("Reset");// Reset Button
		bt4.addActionListener(this);
		bt5 = new JButton("Register"); // Register Button
		bt5.addActionListener(this);

		// Setting bounds for labels
		lb10.setBounds(100, 70, 400, 30);
		user.setBounds(70, 120, 200, 30);
		pwd.setBounds(70, 160, 200, 30);
		fdLogin.setBounds(180, 120, 200, 30);
		pfdLogin.setBounds(180, 160, 200, 30);
		bt3.setBounds(70, 230, 150, 30);
		bt4.setBounds(200, 230, 150, 30);
		lb13.setBounds(80, 280, 400, 30);
		bt5.setBounds(80, 330, 200, 30);

		// Adding components to the login frame
		login.add(lb10);
		login.add(user);
		login.add(pwd);
		login.add(fdLogin);
		login.add(pfdLogin);
		login.add(lb10);
		login.add(bt3);
		login.add(bt4);
		login.add(lb13);
		login.add(bt5);

		login.setSize(600, 600);
		login.setVisible(true);

	}

	// method to register users
	public void registration() {
		register = new JFrame("Registration");
		register.setLayout(null);
		register.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		lb1 = new JLabel("Registration Form");
		lb1.setForeground(Color.BLUE);
		lb1.setFont(new Font("Calibri", Font.BOLD, 20));

		lb2 = new JLabel("FirstName:"); // Label for first name
		lb5 = new JLabel("LastName:");// Label for last name
		lb6 = new JLabel("UserID:"); // Label for UserID
		lb6.setToolTipText("User ID must be between 6 to 12 characters");
		lb7 = new JLabel("Create Password:"); // Label for Password
		lb7.setToolTipText("Password must be between 6 to 12 characters");
		lb8 = new JLabel("Confirm Password:");// Label for Confirm Password
		lb8.setToolTipText("Passwords must match");
		lb9 = new JLabel("Status:"); // Label for Status
		fd1 = new JTextField(); // First name Text field
		fd2 = new JTextField(); // Last name Text field
		fd3 = new JTextField(); // UserID Text field
		pfd1 = new JPasswordField(); // Password field
		pfd2 = new JPasswordField(); // Confirm Password field

		String[] status = { "Standard", "Luxury" }; // setting values for status
		cb1 = new JComboBox<String>(status); // drop down list for status
		cb1.setSelectedIndex(0); // setting default value
		cb1.addActionListener(this);

		bt1 = new JButton("Register"); // Register Button
		bt1.addActionListener(this);
		bt2 = new JButton("Cancel");// Cancel Button
		bt2.addActionListener(this);

		// Setting bounds for labels
		lb1.setBounds(100, 70, 400, 30);
		lb2.setBounds(70, 120, 200, 30);
		lb5.setBounds(70, 160, 200, 30);
		lb6.setBounds(70, 200, 200, 30);
		lb7.setBounds(70, 240, 200, 30);
		lb8.setBounds(70, 280, 200, 30);
		lb9.setBounds(70, 320, 200, 30);
		fd1.setBounds(200, 120, 200, 30);
		fd2.setBounds(200, 160, 200, 30);
		fd3.setBounds(200, 200, 200, 30);
		pfd1.setBounds(200, 240, 200, 30);
		pfd2.setBounds(200, 280, 200, 30);
		cb1.setBounds(200, 320, 200, 30);
		bt1.setBounds(200, 420, 200, 30);
		bt2.setBounds(380, 420, 200, 30);

		// Adding components to the register frame
		register.add(lb1);
		register.add(lb2);
		register.add(lb5);
		register.add(lb6);
		register.add(lb7);
		register.add(lb8);
		register.add(lb9);
		register.add(fd1);
		register.add(fd2);
		register.add(fd3);
		register.add(pfd1);
		register.add(pfd2);
		register.add(cb1);
		register.add(bt1);
		register.add(bt2);

		register.setSize(600, 600);
		register.setVisible(true);
	}

	public static void main(String[] args) throws IOException {
		new Team5Client(); // open home frame on start
	}

	@SuppressWarnings("resource")
	// client method to create connection to server to transmit and receive data
	public String client(String details) {
		String hostName = "localhost";
		String flag = null;
		int portNumber = 2222;
		String data;
		try {
			Socket echoSocket = new Socket(hostName, portNumber);
			BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
			System.out.println(details);
			String userDetails = details + (char) 13;
			BufferedOutputStream os = new BufferedOutputStream(echoSocket.getOutputStream());
			OutputStreamWriter osw = new OutputStreamWriter(os, "US-ASCII");
			osw.write(userDetails); // write output to server
			osw.flush();
			if (in.readLine().equals("Success")) { // check result from server
													// -validate
				System.out.println("Client-server interaction in progress !");
				data = in.readLine();
				if (data != null && !data.isEmpty()) {
					flag = "val";
					input = data;
				} else
					flag = "0";
			} else {
				flag = "1";
				data = in.readLine();
				input = data;
			}
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host " + hostName);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to " + hostName);
			e.printStackTrace();
			System.exit(1);
		} catch (NullPointerException nl) {
			System.err.println("No data in the server.Please create a new user");
			// pop up message to be displayed to user
			JOptionPane.showMessageDialog(bt3, "User info does not exist. Please Register first ");
		}
		return flag;
	}

	@Override
	// perform process based on action events
	public void actionPerformed(ActionEvent e) {
		try {
			// home page , login button action
			if (e.getSource() == btLogin) {
				home.setVisible(false);
				login();
			}
			// home page, register button action
			if (e.getSource() == btReg) {
				home.setVisible(false);
				registration();
			}
			// register page ,submit button action
			if (e.getSource() == bt1) {
				String fname = fd1.getText();
				String lname = fd2.getText();
				String userID = fd3.getText();
				char[] ch1 = pfd1.getPassword();
				char[] ch2 = pfd2.getPassword();
				String pwd1 = new String(ch1);
				String pwd2 = new String(ch2);
				String status = (String) cb1.getSelectedItem();

				// Checking password length condition (min 6 characters and max
				// 12
				// characters)
				if (pwd1.length() > 5 && pwd2.length() > 5 && pwd1.length() < 13 && pwd2.length() < 13) {
					if (userID.length() > 5 && userID.length() < 13) {
						// Checking if passwords match
						if (pwd1.equals(pwd2)) {
							try {
								clientFlag = client("Register," + fname + "," + lname + "," + userID + "," + pwd1 + ","
										+ status + ",0");
								if (clientFlag.equals("0")) {
									JOptionPane.showMessageDialog(bt1, "Registered Successfully");
									register.setVisible(false);
									fd1.setText("");
									fd2.setText("");
									fd3.setText("");
									pfd1.setText("");
									pfd2.setText("");
									cb1.setSelectedIndex(0);
									login();
								} else {
									JOptionPane.showMessageDialog(bt1, "User already exists");
								}
							} catch (Exception exp) {
								System.out.println(exp);
							}
						} else {
							JOptionPane.showMessageDialog(bt1, "Passwords does not match");
						}
					} else {
						JOptionPane.showMessageDialog(bt1,
								"userID length should be min 6 characters and max 12 characters");
					}
				} else {
					JOptionPane.showMessageDialog(bt1,
							"Password length should be min 6 characters and max 12 characters");
				}
			}
			// register page, cancel button action
			if (e.getSource() == bt2) {
				if (!home.isActive()) {
					home.setVisible(true);
					fd1.setText("");
					fd2.setText("");
					fd3.setText("");
					pfd1.setText("");
					pfd2.setText("");
					cb1.setSelectedIndex(0);
					register.setVisible(false);
				} else {
					register.setVisible(false);
				}
			}
			// login page, login button action
			if (e.getSource() == bt3) {
				String userID = fdLogin.getText();
				char[] ch1 = pfdLogin.getPassword();
				String pwd1 = new String(ch1);
				String seats = "";
				try {
					fdLogin.setText("");
					pfdLogin.setText("");
					if (!userID.isEmpty() && !pwd1.isEmpty()) {
						clientFlag = client("Login," + userID + "," + pwd1);
						System.out.println(clientFlag);
						if (!clientFlag.equals("1")) {
							activeUser = userID;
							login.setVisible(false);
							seats = input;
							parseSeatInput(seats);
							seatSelection();
						} else {
							if (input.equals("activeUser"))
								JOptionPane.showMessageDialog(bt3, "User already logged in");
							else
								JOptionPane.showMessageDialog(bt3, "Invalid credentails");
						}
					} else {
						JOptionPane.showMessageDialog(bt3, "Please enter valid credentials");
					}
				} catch (NullPointerException nl) {

				} catch (Exception exp) {
					exp.printStackTrace();
				}
			}
			// login page, reset button
			if (e.getSource() == bt4)

			{
				fdLogin.setText("");
				pfdLogin.setText("");
			}
			// login page, register button action
			if (e.getSource() == bt5) {
				home.setVisible(false);
				login.setVisible(false);
				registration();
			}
			// flight page, logout button action
			if (e.getSource() == logout) {
				clientFlag = client("logout" + "," + activeUser);
				if (flight.isActive()) {
					flight.dispose();
				}
				input = ""; // clear the input from server
				login();
			}
			// flight page, submit button action
			if (e.getSource() == submit) {
				seatCount = 0;
				ArrayList<String> tempUserSeats = new ArrayList<>();
				// get seats selected
				for (int i = 0; i < 20; i++) {
					System.out.println(a[i].isSelected() + " -" + a[i].getText());
					if (a[i].isSelected() && a[i].isEnabled()) {
						tempUserSeats.add(a[i].getText());
						seatCount++;
					}
					if (b[i].isSelected() && b[i].isEnabled()) {
						tempUserSeats.add(b[i].getText());
						seatCount++;
					}
					if (c[i].isSelected() && c[i].isEnabled()) {
						tempUserSeats.add(c[i].getText());
						seatCount++;
					}
					if (d[i].isSelected() && d[i].isEnabled()) {
						tempUserSeats.add(d[i].getText());
						seatCount++;
					}
				}
				// if (seatCount <= 4) {
				String seats = String.join(",", tempUserSeats);

				// send seats selected information to server to store and
				// validate
				clientFlag = client("seats," + activeUser + "," + seatCount + "," + seats);
				if (!clientFlag.equals("1")) {
					processSeats();
					tempUserSeats.clear();
				} else {
					if (input.equals("seats")) {
						JOptionPane.showMessageDialog(submit, "Max 4 seats only for a user");
					} else if (input.equals("seatConflict")) {
						JOptionPane.showMessageDialog(submit,
								"Seat already reserved , try selecting a different seat \n Please unselect the seat(s) already booked");
					} else if (input.equals("statusFlag")) {
						JOptionPane.showMessageDialog(submit, "Sorry, first 3 rows available for luxury users only !");
					} else {
						JOptionPane.showMessageDialog(submit, "No seats selected");
					}
				}
			}
			// user side view- seats selected
			if (e.getSource() == viewSeats) {
				clientFlag = client("viewSeats," + activeUser);
				if (!clientFlag.equals("1")) {
					JOptionPane.showMessageDialog(viewSeats,
							"UserId" + "  " + "Seats Booked " + "#Seats " + "\n" + input);
				} else {
					JOptionPane.showMessageDialog(viewSeats, "No seats selected");
				}
			}
		} catch (

		Exception ex)

		{
			ex.printStackTrace();
		}

	}

	public void parseSeatInput(String seats) {
		String seatsBooked = "";
		ArrayList<String> bookedSeats = new ArrayList<>();
		try {
			String[] inputSplit = seats.split(","); // split the data to read
			System.out.println(inputSplit);
			for (int i = 0; i < inputSplit.length; i++) {
				bookedSeats.add(inputSplit[i]);
				seatsBooked = String.join(",", bookedSeats);
				seatsSelected.add(inputSplit[i]);
			}
			System.out.println("Booked seats : " + seatsBooked);
		} catch (Exception e) {
		}
	}

	public void processSeats() throws NumberFormatException, HeadlessException {
		String seatsBooked = null;
		String Count = null;
		String userID;

		try {
			String[] inputSplit = input.split(","); // split the data to read

			ArrayList<String> bookedSeats = new ArrayList<>(); // get seats
																// booked

			System.out.println(inputSplit);
			userID = inputSplit[0];
			Count = inputSplit[1];
			seatCount = Integer.parseInt(Count);
			for (int i = 2; i < inputSplit.length; i++) {
				bookedSeats.add(inputSplit[i]);
				seatsBooked = String.join(",", bookedSeats);
				seatsSelected.add(inputSplit[i]);
			}

			JOptionPane.showMessageDialog(submit,
					"UserID : " + userID + "\n Seats Booked : " + seatsBooked + "\n No. of Seats: " + seatCount);
		} catch (Exception e) {
		}
	}
}