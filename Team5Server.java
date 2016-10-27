import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Team5Server extends JFrame implements ActionListener {
	/**
	 * Team 5 - Server
	 */
	private static final long serialVersionUID = 1L;
	public static final int PORT = 2222;
	JFrame sFrame;
	JPanel sPanel,
	btPanel,
	bottombtnPnl;
	JButton allBt,
	seatBt,
	userBt;
	JComboBox < String > seatList,
	userList;
	JLabel seatLb,
	userLb;
	JTable reportTable;
	JScrollPane sPane;
	DefaultTableModel modelTable;
	String[]totalUsers = {
		""
	};
	ArrayList < User > listOfUsers = new ArrayList < User > ();
	ArrayList < Seat > seats = new ArrayList <  > ();
	ArrayList < String > output = new ArrayList <  > ();
	ArrayList < String > usersRegistered = new ArrayList <  > ();
	ArrayList < String > totalSeatsSelected = new ArrayList <  > ();

	public static void main(String[]args) {
		new Team5Server().run(); // initialize run method every time server
		// class starts
	}

	// create server socket to connect to client
	public void run() {
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(PORT);
			System.out.println("Server connected");
			serverSideView(); // Run server side validation
			while (true) {
				Socket clientSocket = serverSocket.accept(); // accept
				// connection
				// from client
				new ServerThread(clientSocket).start(); // start a thread for
				// each client
				// connection
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// multi threading
	class ServerThread extends Thread {

		Socket socket;

		ServerThread(Socket socket) {
			this.socket = socket;
		}

		// method to validate input from client and return desired output
		public void run() {
			try {
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				StringBuffer strbuf = new StringBuffer();
				BufferedInputStream is = new BufferedInputStream(socket.getInputStream());
				int input = 0;
				InputStreamReader reader = new InputStreamReader(is, "US-ASCII");
				while ((input = reader.read()) != 13)
					strbuf.append((char)input);
				String[]userInfo = strbuf.toString().split(",");
				// validation to register a user
				if (userInfo[0].equals("Register")) {
					boolean flag = true;
					for (User str : listOfUsers) {
						if (str.getUserID().equals(userInfo[3])) {
							flag = false;
						}
					}
					if (flag == true) {
						listOfUsers.add(new User(listOfUsers.size() + 1, userInfo[1], userInfo[2], userInfo[3],
								userInfo[4], userInfo[5], userInfo[6]));
						seats.add(new Seat("", "", userInfo[3]));
						System.out.println("User added successfully");
						System.out.println(listOfUsers.toString());
						out.println("Success");
						out.println("");
					} else {
						out.println("Failure");
					}
				}
				// validation to login a user
				if (userInfo[0].equals("Login")) {
					String out2 = "";
					boolean isUser = false;
					for (User userobj : listOfUsers) {
						if (userobj.getUserID().equals(userInfo[1]) && userobj.getPassword().equals(userInfo[2])) {
							if (userobj.getFlag().equals("0")) {
								out.println("Success");
								System.out.println(userobj.getUserID() + " is logged in");
								userobj.setFlag("1");
								isUser = true;
								// send selected seats information
								if (!totalSeatsSelected.isEmpty()) {
									for (String str : totalSeatsSelected) {
										out2 = String.join(",", str);
									}
									out.println(out2);
									System.out.println(out2);
								}
							} else {
								out.println("Failure");
								out.println("activeUser");
							}
						}
					}
					if (!isUser) {
						out.println("Failure");
						out.println("");
					}
				}
				// request to logout
				if (userInfo[0].equals("logout")) {
					for (User userobj : listOfUsers) {
						if (userobj.getUserID().equals(userInfo[1])) {
							userobj.setFlag("0");
							System.out.println(userInfo[1] + " has logged out ");
							out.println("Success");
							out.println("");
						}
					}
				}
				// user side -view seats
				if (userInfo[0].equals("viewSeats")) {
					String outputUserSeats = "";
					for (Seat obj : seats) {
						if (obj.getUserID().equals(userInfo[1])) {
							outputUserSeats = obj.getUserID() + "   " + obj.getSeatsSelected() + "    "
								 + obj.getseatCount();
						}
					}
					out.println("Success");
					out.println(outputUserSeats);
				}
				// seat reservation request
				if (userInfo[0].equals("seats")) {
					String seatInfo = null;
					String status = null;
					boolean seatFlag = true;
					boolean statusFlag = true;
					// check if seat already booked
					boolean seatConflict = false;
					ArrayList < String > temp = new ArrayList <  > ();

					System.out.println("into Seats");
					for (int i = 3; i < userInfo.length; i++) {
						temp.add(userInfo[i]);
						seatInfo = String.join(",", temp);
					}
					for (User o1 : listOfUsers) {
						if (o1.getUserID().equals(userInfo[1])) {
							status = o1.getStatus();
						}
					}
					if (status.equals("Standard")) {
						for (String str : temp) {
							if (str.equalsIgnoreCase("a1") || str.equalsIgnoreCase("a2") || str.equalsIgnoreCase("a3")
								 || str.equalsIgnoreCase("b1") || str.equalsIgnoreCase("b2")
								 || str.equalsIgnoreCase("b3") || str.equalsIgnoreCase("c1")
								 || str.equalsIgnoreCase("c2") || str.equalsIgnoreCase("c3")
								 || str.equalsIgnoreCase("d1") || str.equalsIgnoreCase("d2")
								 || str.equalsIgnoreCase("d3")) {
								statusFlag = false;
							}
						}
					}
					if (statusFlag) {
						for (Seat obj : seats) {
							// get existing seats and add them to
							// current selection
							String[]getUserSeats = null;
							if (!obj.getSeatsSelected().isEmpty()) {
								getUserSeats = obj.getSeatsSelected().split(",");
								// parse seats
								for (String seat : temp) {
									for (int i = 0; i < getUserSeats.length; i++) {
										if (getUserSeats[i].equals(seat)) {
											seatConflict = true;
											statusFlag = false;
										}
									}
								}
							}
							if (obj.getUserID().equals(userInfo[1])) {
								String count = obj.getseatCount();
								String str = userInfo[2];
								if (count.isEmpty()) {
									count = "0";
								} else
									System.out.println((Integer.parseInt(count) + Integer.parseInt(str)));
								if ((Integer.parseInt(count) + Integer.parseInt(str)) > 4) {
									statusFlag = false;
									seatFlag = false;
								} else {
									String updatedSeats = null;
									if (!seatConflict) {
										// Merge seats curently booked with
										// existing seats for a user
										if (obj.getSeatsSelected().equalsIgnoreCase(",")
											 || obj.getSeatsSelected().equalsIgnoreCase("")) {
											updatedSeats = seatInfo;
										} else {
											updatedSeats = obj.getSeatsSelected().concat("," + seatInfo);
										}
										System.out.println(updatedSeats);

										obj.setSeatsSelected(updatedSeats);
										obj.setseatCount(
											String.valueOf(Integer.parseInt(count) + Integer.parseInt(str)));
										System.out.println(
											obj.getUserID() + userInfo[1] + obj.getSeatsSelected() + userInfo[2]);
										output.add(userInfo[1]);
										output.add(obj.getseatCount());
										output.add(obj.getSeatsSelected());
										totalSeatsSelected.add(obj.getSeatsSelected());
									}
								}
							}
						}
					}
					System.out.println(seatInfo);
					if (seatInfo != null && statusFlag) {
						out.println("Success");
						String tempOut = null;
						tempOut = String.join(",", output);
						System.out.println(tempOut);

						out.println(tempOut);
					} else {
						out.println("Failure");
						if (seatConflict) {
							out.println("seatConflict");
						} else if (!seatFlag) {
							out.println("seats");
						} else if (!statusFlag) {
							out.println("statusFlag");
						} else
							out.println("");
					}
					output.clear();
				}
				socket.close(); // close the connection
			} catch (IOException e) {
				System.out.println(
					"Exception caught when trying to listen on port " + PORT + " or listening for a connection");
				System.out.println(e.getMessage());
			}
		}
	}

	// set the frame for server side views
	public void serverSideView() {
		// array for total seats
		String[]totalSeats = {
			"a1",
			"a2",
			"a3",
			"a4",
			"a5",
			"a6",
			"a7",
			"a8",
			"a9",
			"a10",
			"a11",
			"a12",
			"a13",
			"a14",
			"a15",
			"a16",
			"a17",
			"a18",
			"a19",
			"a20",
			"b1",
			"b2",
			"b3",
			"b4",
			"b5",
			"b6",
			"b7",
			"b8",
			"b9",
			"b10",
			"b11",
			"b12",
			"b13",
			"b14",
			"b15",
			"b16",
			"b17",
			"b18",
			"b19",
			"b20",
			"c1",
			"c2",
			"c3",
			"c4",
			"c5",
			"c6",
			"c7",
			"c8",
			"c9",
			"c10",
			"c11",
			"c12",
			"c13",
			"c14",
			"c15",
			"c16",
			"c17",
			"c18",
			"c19",
			"c20",
			"d1",
			"d2",
			"d3",
			"d4",
			"d5",
			"d6",
			"d7",
			"d8",
			"d9",
			"d10",
			"d11",
			"d12",
			"d13",
			"d14",
			"d15",
			"d16",
			"d17",
			"d18",
			"d19",
			"d20"
		};
		try {
			sFrame = new JFrame("Server Side View");
			sFrame.setLayout(null);
			sFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			btPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
			bottombtnPnl = new JPanel(new FlowLayout(FlowLayout.CENTER));

			allBt = new JButton("All User Reservations");
			allBt.addActionListener(this);

			seatBt = new JButton("View User");
			seatBt.addActionListener(this);

			userBt = new JButton("View Seat(s)");
			userBt.addActionListener(this);

			seatLb = new JLabel("Select Seat");
			seatLb.setForeground(Color.BLUE);
			seatLb.setFont(new Font("Calibri", Font.BOLD, 20));

			userLb = new JLabel("Select User");
			userLb.setForeground(Color.BLUE);
			userLb.setFont(new Font("Calibri", Font.BOLD, 20));

			seatList = new JComboBox <  > (totalSeats);
			seatList.setSelectedIndex(0);

			userList = new JComboBox <  > (totalUsers);

			allBt.setBounds(70, 100, 200, 30);
			seatLb.setBounds(100, 150, 200, 30);
			seatList.setBounds(300, 150, 200, 30);
			seatBt.setBounds(500, 150, 200, 30);
			userLb.setBounds(100, 200, 200, 30);
			userList.setBounds(300, 200, 200, 30);
			userBt.setBounds(500, 200, 200, 30);

			sFrame.add(allBt);
			sFrame.add(seatLb);
			sFrame.add(seatList);
			sFrame.add(seatBt);
			sFrame.add(userLb);
			sFrame.add(userList);
			sFrame.add(userBt);

			sFrame.setSize(800, 400);
			sFrame.setVisible(true);
		} catch (HeadlessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// method to fetch the list of users registered
	public void fetchUserList() {
		String userDropdown = "";
		try {
			if (!listOfUsers.isEmpty()) {
				for (User userobj : listOfUsers) {
					userDropdown = userDropdown + userobj.getUserID() + ",";
					System.out.println("userdropdown:  " + userDropdown);
				}
				totalUsers = userDropdown.split(",");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// seat class to load seats selected, seat count and user id values from
	// client
	class Seat {
		String seatsSelected;
		String seatCount;
		String userID;

		// set the values every instance
		public Seat(String seatsSelected, String seatCount, String userID) {
			this.seatsSelected = seatsSelected;
			this.seatCount = seatCount;
			this.userID = userID;
		}

		// getters and setters
		public String getSeatsSelected() {
			return seatsSelected;
		}

		public void setSeatsSelected(String seatsSelected) {
			this.seatsSelected = seatsSelected;
		}

		public String getseatCount() {
			return seatCount;
		}

		public void setseatCount(String seatCount) {
			this.seatCount = seatCount;
		}

		public String getUserID() {
			return userID;
		}

		public void setUserID(String userID) {
			this.userID = userID;
		}

	}

	// user class to store all user information
	class User {
		private int AcctID;
		private String fname;
		private String lname;
		private String userID;
		private String password;
		private String status;
		private String flag;

		// set the values
		public User(int AcctID, String fname, String lname, String userID, String password, String status,
			String flag) {
			this.AcctID = AcctID;
			this.fname = fname;
			this.lname = lname;
			this.userID = userID;
			this.password = password;
			this.status = status;
			this.flag = flag;
			usersRegistered.add(userID);
		}

		// getters and setters
		public int getAcctID() {
			return AcctID;
		}

		public void setAcctID(int acctID) {
			AcctID = acctID;
		}

		public String getFname() {
			return fname;
		}

		public void setFname(String fname) {
			this.fname = fname;
		}

		public String getLname() {
			return lname;
		}

		public void setLname(String lname) {
			this.lname = lname;
		}

		public String getUserID() {
			return userID;
		}

		public void setUserID(String userID) {
			this.userID = userID;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getFlag() {
			return flag;
		}

		public void setFlag(String flag) {
			this.flag = flag;
		}
	}

	 @ Override
	// perform process based on actions received
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		ArrayList < String > allUsers = new ArrayList <  > ();
		ArrayList < String > allSeats = new ArrayList <  > ();
		ArrayList < String > seatCount = new ArrayList <  > ();
		String outputAll = "";
		String dispUser = "";
		String dispSeats = "";
		String[]seatObjchoices = {
			""
		};
		try {
			// view all seats reserved
			if (e.getSource() == allBt) {
				if (sFrame.isVisible()) {
					sFrame.setVisible(false);
					sFrame.dispose();
					fetchUserList();
					serverSideView();
				}
				if (listOfUsers.isEmpty()) {
					JOptionPane.showMessageDialog(allBt, "No Data to Display");
				} else {
					for (User userobj : listOfUsers) {
						allUsers.add(userobj.getUserID());
					}
					for (Seat seatobj : seats) {
						if (!seatobj.getSeatsSelected().isEmpty())
							allSeats.add(seatobj.getSeatsSelected());
						seatCount.add(seatobj.getseatCount());
						System.out.println("allSeats: " + allSeats);
						System.out.println("seatCount: " + seatCount);
					}
					for (int i = 0; i < allUsers.size(); i++) {
						outputAll = outputAll + allUsers.get(i) + "   " + allSeats.get(i) + "   " + seatCount.get(i)
							 + "\n";
					}
					JOptionPane.showMessageDialog(allBt,
						"UserId" + "  " + "Seats Booked " + "#Seats " + "\n" + outputAll);
				}
			}
			// view users based on seat booking
			if (e.getSource() == seatBt) {
				String seatChoice = (String)seatList.getSelectedItem();
				System.out.println(seatChoice);
				for (Seat seatobj : seats) {
					seatObjchoices = seatobj.getSeatsSelected().split(",");
					for (int i = 0; i < seatObjchoices.length; i++) {
						if (seatObjchoices[i].equals(seatChoice)) {
							dispUser = seatobj.getUserID();
							System.out.println("dispUser: " + dispUser);
						}
					}
				}
				if (dispUser.equals("")) {
					JOptionPane.showMessageDialog(seatBt, "Seat Available");
				} else {
					JOptionPane.showMessageDialog(seatBt, "Seat Booked by " + dispUser);
				}
			}
			// view seats booked by users
			if (e.getSource() == userBt) {
				for (Seat seatobj : seats) {
					if (seatobj.getUserID().equals(userList.getSelectedItem())) {
						dispSeats = dispSeats + seatobj.getSeatsSelected();
					}
				}
				JOptionPane.showMessageDialog(userBt, "Seats Booked " + dispSeats);
			}
		} catch (IndexOutOfBoundsException ex) {
			// ex.printStackTrace();
		}
		catch (Exception e2) {
			e2.printStackTrace();
		}
	}
}