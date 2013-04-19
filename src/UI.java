/**
 * @author Raghav Ramesh A0091578R
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

// Creates the UserInterface for EZTask
public class UI extends JFrame {

	private final String COMMAND_ADD = "ADD ";
	private final String COMMAND_EDIT = "EDIT ";
	private final String COMMAND_GOTO = "GOTO ";
	private final String COMMAND_DELETE = "DELETE ";
	private final String COMMAND_UNDO = "UNDO ";
	private final String COMMAND_SEARCH = "SEARCH ";
	private final String COMMAND_COMPLETE = "COMPLETE ";
	private final String COMMAND_CLEAR = "CLEAR";
	private final String COMMAND_QUIT = "QUIT";

	private final String LOGO_URL = "EZTask logo.jpg";
	private final String ICON_URL = "EZTask Icon.jpg";
	private final String USER_MANUAL_URL = "rundll32 url.dll,FileProtocolHandler "
			+ "[t14-4j][V0.5].pdf";

	private final String DEFAULT_VIEW_CALENDAR = "View: Today's tasks and onwards";
	private final String DEFAULT_VIEW_PRIORITY = "View: All tasks";

	private final int LOCATION_X_AXIS = 250;
	private final int LOCATION_Y_AXIS = 50;

	private final int LOG_SIZE_INVALID = 0;
	private final int LOG_SIZE_MAX = 5;

	private final String ANTI_MERIDIAN = "am";
	private final String POST_MERIDIAN = "pm";

	private final String STATUS_LOW = "Low    ";
	private final String STATUS_MEDIUM = "Medium";
	private final String STATUS_HIGH = "High   ";

	private final String MESSAGE_USER_CONFIRMATION = "Are you sure you want to exit EZTask?";

	private final String TITLE_PRODUCT = "EZTask";
	private final String TITLE_USER_CONFIRMATION = "User Confirmation";
	private final String TITLE_CALENDAR_LIST = "Calendar List";
	private final String TITLE_PRIORITY_LIST = "Priority List";
	private final String TITLE_KEYWORDS = "Keywords";
	private final String TITLE_DESCRIPTION = "DESCRIPTION";
	private final String TITLE_DATE = "DATE";
	private final String TITLE_TIME = "TIME";
	private final String TITLE_STATUS = "STAT";
	private final String TITLE_PRIORITY = "PRIO";

	private final String NULL = "";
	private final String FULL_STOP = ". ";
	private final String HYPHEN = "-";
	private final String SPACE = " ";
	private final String COLON = ":";
	private final String NEW_LINE = "\n";

	private final String FONT_COMICSANS = "Comic Sans MS";
	private final String TASK_DONE = "Done";
	private final String TRUE = "true";

	private final String MONTH_JANUARY = "Jan";
	private final String MONTH_FEBRUARY = "Feb";
	private final String MONTH_MARCH = "Mar";
	private final String MONTH_APRIL = "Apr";
	private final String MONTH_MAY = "May";
	private final String MONTH_JUNE = "Jun";
	private final String MONTH_JULY = "Jul";
	private final String MONTH_AUGUST = "Aug";
	private final String MONTH_SEPTEMBER = "Sep";
	private final String MONTH_OCTOBER = "Oct";
	private final String MONTH_NOVEMBER = "Nov";
	private final String MONTH_DECEMBER = "Dec";

	private static UI instance = null;

	private UI() {

		initComponents();
		initFrame();
		setLogo();
		launchHelp();
		cmdLineOperations();
		taskListOperations();
		keyboardShortcuts();
		setViewDate(NULL);
		setViewPrio(NULL);
		menuBarActions();
		closingConfirmation();
	}
	
	//applying singleton
	public static UI getInstance() {

		if (instance == null) {
			instance = new UI();
		}
		return instance;
	}

	private void initFrame() {
		super.setTitle(TITLE_PRODUCT);
		super.setLocation(LOCATION_X_AXIS, LOCATION_Y_AXIS);
		super.setResizable(false);
		super.setIconImage(image.getImage());
		super.setVisible(true);
	}

	private void setLogo() {

		logo.setIcon(new ImageIcon(LOGO_URL));
	}
	
	//opens user manual
	private void launchHelp() {
		ActionListener alHelp = new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					Runtime.getRuntime().exec(USER_MANUAL_URL);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		};
		help.addActionListener(alHelp);
	}

	private void cmdLineOperations() {

		autoFillKeyword();
		enableDirectTypingOnCmdLine();
	}

	private void enableDirectTypingOnCmdLine() {

		cmdLine.requestFocus();
	}

	private void enableSimultaneousScrolling() {

		calListTaskPane.getVerticalScrollBar().setModel(
				calListInfoPane.getVerticalScrollBar().getModel());
		prioListTaskPane.getVerticalScrollBar().setModel(
				prioListInfoPane.getVerticalScrollBar().getModel());
	}

	private void taskListOperations() {

		selectTaskAndInfo();
		enableSimultaneousScrolling();
	}

	public String getCommandLine() {

		return cmdLine.getText();
	}

	protected String convertDateToUserFriendlyFormat(String date) {

		String convertedDate = NULL, month = NULL;

		assert (date.length() == 8);

		String yyyy = date.substring(0, 4);
		String mm = date.substring(4, 6);
		String dd = date.substring(6, 8);

		int mmInt = Integer.parseInt(mm);

		switch (mmInt) {

		case 1:
			month = MONTH_JANUARY;
			break;
		case 2:
			month = MONTH_FEBRUARY;
			break;
		case 3:
			month = MONTH_MARCH;
			break;
		case 4:
			month = MONTH_APRIL;
			break;
		case 5:
			month = MONTH_MAY;
			break;
		case 6:
			month = MONTH_JUNE;
			break;
		case 7:
			month = MONTH_JULY;
			break;
		case 8:
			month = MONTH_AUGUST;
			break;
		case 9:
			month = MONTH_SEPTEMBER;
			break;
		case 10:
			month = MONTH_OCTOBER;
			break;
		case 11:
			month = MONTH_NOVEMBER;
			break;
		case 12:
			month = MONTH_DECEMBER;
			break;
		default:
			break;
		}

		convertedDate = dd + HYPHEN + month + HYPHEN + yyyy;
		return convertedDate;
	}

	protected String convertTimeToUserFriendlyFormat(String time) {

		String convertedTime = NULL, hour = NULL, minutes = NULL;

		assert (time.length() == 4);

		hour = time.substring(0, 2);
		minutes = time.substring(2, 4);

		int hourInt = Integer.parseInt(hour);
		boolean isPastNoon;
		isPastNoon = (hourInt >= 12) ? true : false;
		String ampm = ANTI_MERIDIAN;

		if (isPastNoon) {

			if (hourInt == 12) {
				ampm = POST_MERIDIAN;
			} else {
				hourInt -= 12;
				ampm = POST_MERIDIAN;
			}
		}

		convertedTime = hourInt + COLON + minutes + SPACE + ampm;
		return convertedTime;
	}

	protected String convertTaskCompletionToUserFriendlyFormat(
			String isCompleted) {

		String taskStatus = NULL;
		taskStatus = isCompleted == TRUE ? TASK_DONE : NULL;

		return taskStatus;
	}

	private void resizeFrame() {

		super.setState(JFrame.ICONIFIED);
	}
	
	//to minimize EZTask when F2 is pressed
	private void keyboardShortcuts() {

		KeyListener kl = new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				if (e.getKeyCode() == KeyEvent.VK_F2) {

					resizeFrame();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

		};

		cmdLine.addKeyListener(kl);
	}

	public void setCommandLine(String text) {

		cmdLine.setText(text);
	}

	//highlights corresponding task info when task desc is selected and vice versa
	private void selectTaskAndInfo() {

		ListSelectionListener al = new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent ls) {

				if (ls.getSource() == calListTaskBox) {

					int index = calListTaskBox.getSelectedIndex();
					calListInfoBox.setSelectedIndex(index);
				}
				if (ls.getSource() == prioListTaskBox) {

					int index = prioListTaskBox.getSelectedIndex();
					prioListInfoBox.setSelectedIndex(index);
				}
				if (ls.getSource() == calListInfoBox) {

					int index = calListInfoBox.getSelectedIndex();
					calListTaskBox.setSelectedIndex(index);
				}
				if (ls.getSource() == prioListInfoBox) {

					int index = prioListInfoBox.getSelectedIndex();
					prioListTaskBox.setSelectedIndex(index);
				}
			}
		};

		calListTaskBox.addListSelectionListener(al);
		prioListTaskBox.addListSelectionListener(al);
		calListInfoBox.addListSelectionListener(al);
		prioListInfoBox.addListSelectionListener(al);

	}

	public void setViewDate(String startDate) {

		if (startDate == NULL) {
			dateDisplay.setText(DEFAULT_VIEW_CALENDAR);
		} else {
			String date = convertDateToUserFriendlyFormat(startDate);
			dateDisplay.setText("View: " + date + " and onwards");
		}
	}

	public void setViewPrio(String startPrio) {

		if (startPrio == NULL) {
			prioDisplay.setText(DEFAULT_VIEW_PRIORITY);
		} else {
			prioDisplay.setText("View: " + startPrio + " and onwards");
		}
	}

	public String getUserInput() {

		ActionListener alCmdLine = new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				command = cmdLine.getText();
				cmdLine.setText(NULL);
			}
		};
		cmdLine.addActionListener(alCmdLine);
		return command;
	}

	private void clearLog() {

		log.setText("");
	}

	// automatically enters the keyword when selected from the keywords list
	private void autoFillKeyword() {

		ListSelectionListener al_keyword = new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				int index = keywordsList.getSelectedIndex();
				switch (index) {
				case 0:
					cmdLine.setText(COMMAND_ADD);
					break;
				case 1:
					cmdLine.setText(COMMAND_EDIT);
					break;
				case 2:
					cmdLine.setText(COMMAND_DELETE);
					break;
				case 3:
					cmdLine.setText(COMMAND_UNDO);
					break;
				case 4:
					cmdLine.setText(COMMAND_GOTO);
					break;
				case 5:
					cmdLine.setText(COMMAND_SEARCH);
					break;
				case 6:
					cmdLine.setText(COMMAND_COMPLETE);
					break;
				case 7:
					cmdLine.setText(COMMAND_CLEAR);
					break;
				case 8:
					cmdLine.setText(COMMAND_QUIT);
					break;
				default:
					assert false : cmdLine;
					throw new AssertionError(cmdLine);

				}
			}
		};
		keywordsList.addListSelectionListener(al_keyword);
	}

	public void displayCalList(String[][] taskList) {

		calListTaskModel.clear();
		calListInfoModel.clear();

		assert (taskList.length == 6);

		System.out.println(taskList.length);
		for (int i = 0; i < taskList.length; i++) {

			String index = taskList[i][0];
			String desc = taskList[i][1];
			String date = taskList[i][2];
			String startTime = taskList[i][3];
			String endTime = taskList[i][4];
			String taskStatus = taskList[i][5];
			String leadingSpaces = SPACE + HYPHEN + SPACE;

			date = convertDateToUserFriendlyFormat(date);

			startTime = convertTimeToUserFriendlyFormat(startTime);
			endTime = convertTimeToUserFriendlyFormat(endTime);

			if (startTime.equals(endTime)) {
				endTime = "             ";
			} else {
				endTime = leadingSpaces.concat(endTime);
			}

			taskStatus = convertTaskCompletionToUserFriendlyFormat(taskStatus);

			String descLine = index + FULL_STOP + desc;
			String infoLine = date + SPACE + SPACE + startTime + endTime
					+ SPACE + SPACE + taskStatus + NEW_LINE;

			calListTaskModel.addElement(descLine);
			calListInfoModel.addElement(infoLine);

		}
	}

	public void displayPrioList(String[][] taskList) {

		prioListTaskModel.clear();
		prioListInfoModel.clear();

		assert (taskList.length == 4);

		for (int i = 0; i < taskList.length; i++) {

			String index = taskList[i][0];
			String desc = taskList[i][1];
			String prio = taskList[i][2];
			String taskStatus = taskList[i][3];

			int prioInt = Integer.parseInt(prio);

			taskStatus = convertTaskCompletionToUserFriendlyFormat(taskStatus);

			switch (prioInt) {
			case 1:
				prio = STATUS_LOW;
				break;
			case 2:
				prio = STATUS_MEDIUM;
				break;
			case 3:
				prio = STATUS_HIGH;
				break;
			default:
				prio = STATUS_LOW;
				assert false : prio;
				throw new AssertionError(prio);
			}

			String descLine = index + FULL_STOP + desc;
			String infoLine = prio + SPACE + taskStatus + NEW_LINE;

			prioListTaskModel.addElement(descLine);
			prioListInfoModel.addElement(infoLine);
		}

	}
	
	//@author Rajalakshmi Ramachandran A0088634U - this function alone
	public void displayLog(String infoToUser) {

		int count = 0;
		userCommandList.add(infoToUser);

		String logString = NULL;

		int sizeCommandList = userCommandList.size();

		if (sizeCommandList == LOG_SIZE_INVALID) {
			log.setText(NULL);
		} else if (sizeCommandList < LOG_SIZE_MAX) {
			for (count = 0; count < sizeCommandList; count++) {
				logString += userCommandList.get(count) + NEW_LINE;
				log.setText(logString);
			}
		} else {
			for (count = sizeCommandList - LOG_SIZE_MAX; count < sizeCommandList; count++) {
				logString += userCommandList.get(count) + NEW_LINE;
				log.setText(logString);
			}
		}
	}

	private void menuBarActions() {

		ActionListener menuActions = new ActionListener() {

			public void actionPerformed(ActionEvent event) {

				if (event.getSource() == calTimedTaskAdd) {

					cmdLine.setText("ADD <desc>; <date>; <start time> - <end time>");
				}
				if (event.getSource() == calDeadlineTaskAdd) {

					cmdLine.setText("ADD <desc>; <date>; <end time>");
				}
				if (event.getSource() == prioTaskAdd) {

					cmdLine.setText("ADD <desc>; <prio>");
				}
				if (event.getSource() == calTimedTaskEdit) {

					cmdLine.setText("EDIT <index>; <desc>; <date>; <start time> - <end time>");
				}
				if (event.getSource() == calDeadlineTaskEdit) {

					cmdLine.setText("EDIT <index>; <desc>; <date>; <end time>");
				}
				if (event.getSource() == prioTaskEdit) {

					cmdLine.setText("EDIT <index>; <desc>; <prio>");
				}
				if (event.getSource() == calTaskDelete) {

					cmdLine.setText("DELETE C <index>");
				}
				if (event.getSource() == prioTaskDelete) {

					cmdLine.setText("DELETE P <index>");
				}
				if (event.getSource() == calTaskGoTo) {

					cmdLine.setText("GOTO c <date>");
				}
				if (event.getSource() == prioTaskGoTo) {

					cmdLine.setText("GOTO p <priority>");
				}
				if (event.getSource() == todayGoTo) {

					cmdLine.setText("GOTO c today");
				}
				if (event.getSource() == helpMenuItem) {
					try {
						Runtime.getRuntime().exec(USER_MANUAL_URL);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				if (event.getSource() == completeCalTaskMenuItem) {

					cmdLine.setText("COMPLETE c <index>");
				}
				if (event.getSource() == completePrioTaskMenuItem) {

					cmdLine.setText("COMPLETE p <index>");
				}
				if (event.getSource() == searchMenuItem) {

					cmdLine.setText(COMMAND_SEARCH);
				}
				if (event.getSource() == clearLogMenuItem) {

					clearLog();
				}
				if (event.getSource() == clearCompTasksMenuItem) {

					cmdLine.setText(COMMAND_CLEAR);
				}
				if (event.getSource() == exitMenuItem) {

					System.exit(0);
				}
			}
		};

		calTimedTaskAdd.addActionListener(menuActions);
		calDeadlineTaskAdd.addActionListener(menuActions);
		prioTaskAdd.addActionListener(menuActions);
		calTimedTaskEdit.addActionListener(menuActions);
		calDeadlineTaskEdit.addActionListener(menuActions);
		prioTaskEdit.addActionListener(menuActions);
		calTaskDelete.addActionListener(menuActions);
		prioTaskDelete.addActionListener(menuActions);
		calTaskGoTo.addActionListener(menuActions);
		prioTaskGoTo.addActionListener(menuActions);
		todayGoTo.addActionListener(menuActions);
		exitMenuItem.addActionListener(menuActions);
		helpMenuItem.addActionListener(menuActions);
		completeCalTaskMenuItem.addActionListener(menuActions);
		completePrioTaskMenuItem.addActionListener(menuActions);
		clearCompTasksMenuItem.addActionListener(menuActions);
		clearLogMenuItem.addActionListener(menuActions);
		searchMenuItem.addActionListener(menuActions);
	}

	private void closingConfirmation() {

		super.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				int confirmed = JOptionPane.showConfirmDialog(null,
						MESSAGE_USER_CONFIRMATION, TITLE_USER_CONFIRMATION,
						JOptionPane.YES_NO_OPTION);
				if (confirmed == JOptionPane.YES_OPTION) {
					System.exit(0);
				} else {
					assert confirmed == JOptionPane.NO_OPTION : confirmed;
				}
				assert false : confirmed;
			}
		});
	}

	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initComponents() {

		panel = new javax.swing.JPanel();
		cmdLine = new javax.swing.JTextField();
		logScrollPane = new javax.swing.JScrollPane();
		log = new javax.swing.JTextArea();
		logo = new javax.swing.JLabel();
		keywords = new javax.swing.JTextField();
		calListTextField = new javax.swing.JTextField();
		prioListTextField = new javax.swing.JTextField();
		help = new javax.swing.JButton();
		keywordsPane = new javax.swing.JScrollPane();
		keywordsList = new javax.swing.JList();

		calListTaskPane = new javax.swing.JScrollPane();
		calListTaskModel = new DefaultListModel();
		calListTaskBox = new javax.swing.JList(calListTaskModel);

		calListInfoPane = new javax.swing.JScrollPane();
		calListInfoModel = new DefaultListModel();
		calListInfoBox = new javax.swing.JList(calListInfoModel);

		prioListTaskPane = new javax.swing.JScrollPane();
		prioListTaskModel = new DefaultListModel();
		prioListTaskBox = new javax.swing.JList(prioListTaskModel);

		prioListInfoPane = new javax.swing.JScrollPane();
		prioListInfoModel = new DefaultListModel();
		prioListInfoBox = new javax.swing.JList(prioListInfoModel);

		calDescLabel = new javax.swing.JLabel();
		calDateLabel = new javax.swing.JLabel();
		calTimeLabel = new javax.swing.JLabel();
		calStatLabel = new javax.swing.JLabel();
		prioDescLabel = new javax.swing.JLabel();
		prioPrioLabel = new javax.swing.JLabel();
		prioStatLabel = new javax.swing.JLabel();
		prioDisplay = new javax.swing.JLabel();
		dateDisplay = new javax.swing.JLabel();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		panel.setBackground(new java.awt.Color(50, 50, 50));

		cmdLine.setBackground(new java.awt.Color(204, 204, 204));
		cmdLine.setFont(new java.awt.Font(FONT_COMICSANS, 1, 15)); // NOI18N
		cmdLine.setForeground(new java.awt.Color(50, 50, 50));

		logScrollPane.setBackground(new java.awt.Color(75, 9, 9));
		logScrollPane.setForeground(new java.awt.Color(218, 192, 192));
		logScrollPane
				.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		logScrollPane.setCursor(new java.awt.Cursor(
				java.awt.Cursor.DEFAULT_CURSOR));
		logScrollPane
				.setDebugGraphicsOptions(javax.swing.DebugGraphics.LOG_OPTION);

		log.setBackground(new java.awt.Color(204, 204, 204));
		log.setColumns(20);
		log.setFont(new java.awt.Font(FONT_COMICSANS, 1, 11)); // NOI18N
		log.setForeground(new java.awt.Color(50, 50, 50));
		log.setRows(5);
		logScrollPane.setViewportView(log);

		logo.setBackground(new java.awt.Color(204, 204, 204));
		logo.setBorder(javax.swing.BorderFactory
				.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
		logo.setCursor(new java.awt.Cursor(java.awt.Cursor.CROSSHAIR_CURSOR));

		keywords.setEditable(false);
		keywords.setBackground(new java.awt.Color(0, 0, 0));
		keywords.setFont(new java.awt.Font(FONT_COMICSANS, 1, 14)); // NOI18N
		keywords.setForeground(new java.awt.Color(240, 240, 240));
		keywords.setHorizontalAlignment(javax.swing.JTextField.CENTER);
		keywords.setText(TITLE_KEYWORDS);

		calListTextField.setEditable(false);
		calListTextField.setBackground(new java.awt.Color(0, 0, 0));
		calListTextField.setFont(new java.awt.Font(FONT_COMICSANS, 1, 14)); // NOI18N
		calListTextField.setForeground(new java.awt.Color(255, 255, 255));
		calListTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
		calListTextField.setText(TITLE_CALENDAR_LIST);

		prioListTextField.setEditable(false);
		prioListTextField.setBackground(new java.awt.Color(0, 0, 0));
		prioListTextField.setFont(new java.awt.Font(FONT_COMICSANS, 1, 14)); // NOI18N
		prioListTextField.setForeground(new java.awt.Color(255, 255, 255));
		prioListTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
		prioListTextField.setText(TITLE_PRIORITY_LIST);

		help.setBackground(new java.awt.Color(0, 0, 0));
		help.setFont(new java.awt.Font(FONT_COMICSANS, 1, 12)); // NOI18N
		help.setForeground(new java.awt.Color(255, 255, 255));
		help.setText("Help");

		keywordsList.setBackground(new java.awt.Color(50, 50, 50));
		keywordsList.setFont(new java.awt.Font(FONT_COMICSANS, 1, 13)); // NOI18N
		keywordsList.setForeground(new java.awt.Color(204, 204, 204));
		keywordsList.setModel(new javax.swing.AbstractListModel() {
			String[] strings = { "ADD", "EDIT", "DELETE", "UNDO", "GOTO",
					"SEARCH", "COMPLETE", "CLEAR", "QUIT" };

			public int getSize() {
				return strings.length;
			}

			public Object getElementAt(int i) {
				return strings[i];
			}
		});
		keywordsPane.setViewportView(keywordsList);

		calListTaskBox.setBackground(new java.awt.Color(204, 204, 204));
		calListTaskBox.setFont(new java.awt.Font(FONT_COMICSANS, 1, 12)); // NOI18N
		calListTaskBox.setForeground(new java.awt.Color(50, 50, 50));
		calListTaskPane.setViewportView(calListTaskBox);

		prioListTaskBox.setBackground(new java.awt.Color(204, 204, 204));
		prioListTaskBox.setFont(new java.awt.Font(FONT_COMICSANS, 1, 12)); // NOI18N
		prioListTaskBox.setForeground(new java.awt.Color(50, 50, 50));
		prioListTaskPane.setViewportView(prioListTaskBox);

		prioListInfoBox.setBackground(new java.awt.Color(204, 204, 204));
		prioListInfoBox.setFont(new java.awt.Font(FONT_COMICSANS, 1, 12)); // NOI18N
		prioListInfoBox.setForeground(new java.awt.Color(50, 50, 50));
		prioListInfoPane.setViewportView(prioListInfoBox);

		calListInfoBox.setBackground(new java.awt.Color(204, 204, 204));
		calListInfoBox.setFont(new java.awt.Font(FONT_COMICSANS, 1, 12)); // NOI18N
		calListInfoBox.setForeground(new java.awt.Color(50, 50, 50));
		calListInfoPane.setViewportView(calListInfoBox);

		calDescLabel.setFont(new java.awt.Font(FONT_COMICSANS, 1, 12)); // NOI18N
		calDescLabel.setForeground(new java.awt.Color(255, 255, 255));
		calDescLabel.setBackground(new java.awt.Color(0, 0, 0));
		calDescLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		calDescLabel.setText(TITLE_DESCRIPTION);

		calDateLabel.setFont(new java.awt.Font(FONT_COMICSANS, 1, 12)); // NOI18N
		calDateLabel.setForeground(new java.awt.Color(255, 255, 255));
		calDateLabel.setBackground(new java.awt.Color(0, 0, 0));
		calDateLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		calDateLabel.setText(TITLE_DATE);

		calTimeLabel.setFont(new java.awt.Font(FONT_COMICSANS, 1, 12)); // NOI18N
		calTimeLabel.setForeground(new java.awt.Color(255, 255, 255));
		calTimeLabel.setBackground(new java.awt.Color(0, 0, 0));
		calTimeLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		calTimeLabel.setText(TITLE_TIME);

		calStatLabel.setFont(new java.awt.Font(FONT_COMICSANS, 1, 12)); // NOI18N
		calStatLabel.setForeground(new java.awt.Color(255, 255, 255));
		calStatLabel.setBackground(new java.awt.Color(0, 0, 0));
		calStatLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		calStatLabel.setText(TITLE_STATUS);

		prioDescLabel.setFont(new java.awt.Font(FONT_COMICSANS, 1, 12)); // NOI18N
		prioDescLabel.setForeground(new java.awt.Color(255, 255, 255));
		prioDescLabel.setBackground(new java.awt.Color(0, 0, 0));
		prioDescLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		prioDescLabel.setText(TITLE_DESCRIPTION);

		prioPrioLabel.setFont(new java.awt.Font(FONT_COMICSANS, 1, 12)); // NOI18N
		prioPrioLabel.setForeground(new java.awt.Color(255, 255, 255));
		prioPrioLabel.setBackground(new java.awt.Color(0, 0, 0));
		prioPrioLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		prioPrioLabel.setText(TITLE_PRIORITY);

		prioStatLabel.setFont(new java.awt.Font(FONT_COMICSANS, 1, 12)); // NOI18N
		prioStatLabel.setForeground(new java.awt.Color(255, 255, 255));
		prioStatLabel.setBackground(new java.awt.Color(0, 0, 0));
		prioStatLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		prioStatLabel.setText(TITLE_STATUS);

		prioDisplay.setFont(new java.awt.Font(FONT_COMICSANS, 1, 12)); // NOI18N
		prioDisplay.setForeground(new java.awt.Color(255, 255, 255));

		dateDisplay.setFont(new java.awt.Font(FONT_COMICSANS, 1, 12)); // NOI18N
		dateDisplay.setForeground(new java.awt.Color(255, 255, 255));
		
		// setting the location of various swing items.
		javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
		panel.setLayout(panelLayout);
		panelLayout
				.setHorizontalGroup(panelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addComponent(cmdLine)
						.addGroup(
								panelLayout
										.createSequentialGroup()
										.addGroup(
												panelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																calListTextField)
														.addComponent(
																dateDisplay,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
														.addGroup(
																javax.swing.GroupLayout.Alignment.TRAILING,
																panelLayout
																		.createSequentialGroup()
																		.addGroup(
																				panelLayout
																						.createParallelGroup(
																								javax.swing.GroupLayout.Alignment.LEADING)
																						.addComponent(
																								calDescLabel,
																								javax.swing.GroupLayout.PREFERRED_SIZE,
																								301,
																								javax.swing.GroupLayout.PREFERRED_SIZE)
																						.addComponent(
																								calListTaskPane,
																								javax.swing.GroupLayout.PREFERRED_SIZE,
																								318,
																								javax.swing.GroupLayout.PREFERRED_SIZE))
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addGroup(
																				panelLayout
																						.createParallelGroup(
																								javax.swing.GroupLayout.Alignment.LEADING)
																						.addGroup(
																								panelLayout
																										.createSequentialGroup()
																										.addComponent(
																												calListInfoPane,
																												javax.swing.GroupLayout.PREFERRED_SIZE,
																												271,
																												javax.swing.GroupLayout.PREFERRED_SIZE)
																										.addGap(0,
																												0,
																												Short.MAX_VALUE))
																						.addGroup(
																								panelLayout
																										.createSequentialGroup()
																										.addComponent(
																												calDateLabel,
																												javax.swing.GroupLayout.PREFERRED_SIZE,
																												76,
																												javax.swing.GroupLayout.PREFERRED_SIZE)
																										.addPreferredGap(
																												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																										.addComponent(
																												calTimeLabel,
																												javax.swing.GroupLayout.PREFERRED_SIZE,
																												46,
																												javax.swing.GroupLayout.PREFERRED_SIZE)
																										.addPreferredGap(
																												javax.swing.LayoutStyle.ComponentPlacement.RELATED,
																												javax.swing.GroupLayout.DEFAULT_SIZE,
																												Short.MAX_VALUE)
																										.addComponent(
																												calStatLabel,
																												javax.swing.GroupLayout.PREFERRED_SIZE,
																												49,
																												javax.swing.GroupLayout.PREFERRED_SIZE)
																										.addGap(15,
																												15,
																												15)))))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												panelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																prioListTextField)
														.addComponent(
																prioDisplay,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
														.addGroup(
																javax.swing.GroupLayout.Alignment.TRAILING,
																panelLayout
																		.createSequentialGroup()
																		.addGroup(
																				panelLayout
																						.createParallelGroup(
																								javax.swing.GroupLayout.Alignment.LEADING)
																						.addComponent(
																								prioDescLabel,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								Short.MAX_VALUE)
																						.addComponent(
																								prioListTaskPane,
																								javax.swing.GroupLayout.PREFERRED_SIZE,
																								153,
																								javax.swing.GroupLayout.PREFERRED_SIZE))
																		.addGroup(
																				panelLayout
																						.createParallelGroup(
																								javax.swing.GroupLayout.Alignment.LEADING)
																						.addGroup(
																								panelLayout
																										.createSequentialGroup()
																										.addGap(14,
																												14,
																												14)
																										.addComponent(
																												prioPrioLabel,
																												javax.swing.GroupLayout.PREFERRED_SIZE,
																												31,
																												javax.swing.GroupLayout.PREFERRED_SIZE)
																										.addGap(18,
																												18,
																												18)
																										.addComponent(
																												prioStatLabel)
																										.addGap(0,
																												0,
																												Short.MAX_VALUE))
																						.addGroup(
																								javax.swing.GroupLayout.Alignment.TRAILING,
																								panelLayout
																										.createSequentialGroup()
																										.addPreferredGap(
																												javax.swing.LayoutStyle.ComponentPlacement.RELATED,
																												javax.swing.GroupLayout.DEFAULT_SIZE,
																												Short.MAX_VALUE)
																										.addComponent(
																												prioListInfoPane,
																												javax.swing.GroupLayout.PREFERRED_SIZE,
																												102,
																												javax.swing.GroupLayout.PREFERRED_SIZE)))))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												panelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING,
																false)
														.addComponent(
																help,
																javax.swing.GroupLayout.Alignment.TRAILING,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
														.addComponent(
																keywords,
																javax.swing.GroupLayout.Alignment.TRAILING)
														.addComponent(
																keywordsPane,
																javax.swing.GroupLayout.Alignment.TRAILING,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																103,
																Short.MAX_VALUE)
														.addComponent(
																logo,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)))
						.addComponent(logScrollPane,
								javax.swing.GroupLayout.Alignment.TRAILING));
		panelLayout
				.setVerticalGroup(panelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								javax.swing.GroupLayout.Alignment.TRAILING,
								panelLayout
										.createSequentialGroup()
										.addGroup(
												panelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.TRAILING,
																false)
														.addComponent(
																calListTextField,
																javax.swing.GroupLayout.Alignment.LEADING)
														.addGroup(
																panelLayout
																		.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.BASELINE)
																		.addComponent(
																				keywords)
																		.addComponent(
																				prioListTextField)))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												panelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addGroup(
																panelLayout
																		.createSequentialGroup()
																		.addComponent(
																				keywordsPane,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				207,
																				javax.swing.GroupLayout.PREFERRED_SIZE)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				help)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				logo,
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				135,
																				Short.MAX_VALUE))
														.addGroup(
																panelLayout
																		.createSequentialGroup()
																		.addGroup(
																				panelLayout
																						.createParallelGroup(
																								javax.swing.GroupLayout.Alignment.LEADING,
																								false)
																						.addComponent(
																								prioDisplay,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								23,
																								Short.MAX_VALUE)
																						.addComponent(
																								dateDisplay,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								Short.MAX_VALUE))
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addGroup(
																				panelLayout
																						.createParallelGroup(
																								javax.swing.GroupLayout.Alignment.BASELINE)
																						.addComponent(
																								prioStatLabel)
																						.addComponent(
																								prioPrioLabel,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								Short.MAX_VALUE)
																						.addComponent(
																								prioDescLabel)
																						.addComponent(
																								calStatLabel)
																						.addComponent(
																								calTimeLabel,
																								javax.swing.GroupLayout.PREFERRED_SIZE,
																								18,
																								javax.swing.GroupLayout.PREFERRED_SIZE)
																						.addComponent(
																								calDateLabel)
																						.addComponent(
																								calDescLabel,
																								javax.swing.GroupLayout.PREFERRED_SIZE,
																								19,
																								javax.swing.GroupLayout.PREFERRED_SIZE))
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addGroup(
																				panelLayout
																						.createParallelGroup(
																								javax.swing.GroupLayout.Alignment.LEADING)
																						.addComponent(
																								prioListInfoPane,
																								javax.swing.GroupLayout.Alignment.TRAILING)
																						.addComponent(
																								prioListTaskPane,
																								javax.swing.GroupLayout.Alignment.TRAILING)
																						.addComponent(
																								calListTaskPane)
																						.addComponent(
																								calListInfoPane))))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												logScrollPane,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												92,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(
												cmdLine,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												52,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addContainerGap()));

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addComponent(panel,
				javax.swing.GroupLayout.DEFAULT_SIZE,
				javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
		layout.setVerticalGroup(layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addComponent(panel,
				javax.swing.GroupLayout.DEFAULT_SIZE,
				javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

		jmb = new JMenuBar();
		super.setJMenuBar(jmb);
		super.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		fileMenu = new JMenu("File");
		fileMenu.setMnemonic('f');
		helpMenu = new JMenu("Help");
		helpMenu.setMnemonic('h');

		jmb.add(fileMenu);
		jmb.add(helpMenu);

		addMenu = new JMenu("Add");
		editMenu = new JMenu("Edit");
		deleteMenu = new JMenu("Delete");
		goToMenu = new JMenu("Goto");
		completeMenu = new JMenu("Complete");
		clearMenu = new JMenu("Clear");
		searchMenuItem = new JMenuItem("Search");
		clearLogMenuItem = new JMenuItem("Log");
		clearCompTasksMenuItem = new JMenuItem("Completed Tasks");
		exitMenuItem = new JMenuItem("Exit", 'X');

		fileMenu.add(addMenu);
		fileMenu.add(editMenu);
		fileMenu.add(deleteMenu);
		fileMenu.add(goToMenu);
		fileMenu.addSeparator();
		fileMenu.add(searchMenuItem);
		fileMenu.add(completeMenu);
		fileMenu.addSeparator();
		fileMenu.add(clearMenu);
		fileMenu.addSeparator();
		fileMenu.add(exitMenuItem);

		calTimedTaskAdd = new JMenuItem("Calendar Task - Timed");
		calDeadlineTaskAdd = new JMenuItem("Calendar Task - Deadline");
		prioTaskAdd = new JMenuItem("Priority Task");
		calTaskDelete = new JMenuItem("Calendar Task");
		prioTaskDelete = new JMenuItem("Priority Task");
		calTimedTaskEdit = new JMenuItem("Calendar Task - Timed");
		calDeadlineTaskEdit = new JMenuItem("Calendar Task - Deadline");
		prioTaskEdit = new JMenuItem("Priority Task");
		calTaskGoTo = new JMenuItem("Calendar Task");
		prioTaskGoTo = new JMenuItem("Priority Task");
		todayGoTo = new JMenuItem("Today's tasks");
		completePrioTaskMenuItem = new JMenuItem("Priority Task");
		completeCalTaskMenuItem = new JMenuItem("Calendar Task");

		addMenu.add(calTimedTaskAdd);
		addMenu.add(calDeadlineTaskAdd);
		addMenu.addSeparator();
		addMenu.add(prioTaskAdd);
		editMenu.add(calTimedTaskEdit);
		editMenu.add(calDeadlineTaskEdit);
		editMenu.addSeparator();
		editMenu.add(prioTaskEdit);
		deleteMenu.add(calTaskDelete);
		deleteMenu.add(prioTaskDelete);
		goToMenu.add(calTaskGoTo);
		goToMenu.add(prioTaskGoTo);
		goToMenu.addSeparator();
		goToMenu.add(todayGoTo);
		completeMenu.add(completeCalTaskMenuItem);
		completeMenu.add(completePrioTaskMenuItem);
		clearMenu.add(clearCompTasksMenuItem);
		clearMenu.addSeparator();
		clearMenu.add(clearLogMenuItem);

		exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_Q, java.awt.Event.CTRL_MASK));

		helpMenuItem = new JMenuItem("User guide manual", 'u');
		helpMenu.add(helpMenuItem);

		helpMenuItem.setAccelerator(KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_F1, java.awt.Event.CTRL_MASK));

		pack();
	}

	// Variables declaration 
	private String command = NULL;
	private JMenuBar jmb;
	private JMenu fileMenu, helpMenu, addMenu, editMenu, completeMenu,
			deleteMenu, goToMenu, clearMenu;
	private JMenuItem clearLogMenuItem, clearCompTasksMenuItem, searchMenuItem,
			calTimedTaskAdd, calDeadlineTaskAdd, prioTaskAdd, calTaskDelete,
			prioTaskDelete, calTimedTaskEdit, calDeadlineTaskEdit,
			prioTaskEdit, calTaskGoTo, todayGoTo, prioTaskGoTo,
			completeCalTaskMenuItem, completePrioTaskMenuItem, helpMenuItem,
			exitMenuItem;

	private DefaultListModel calListTaskModel, calListInfoModel,
			prioListTaskModel, prioListInfoModel;
	private javax.swing.JTextField calListTextField;
	private javax.swing.JLabel calDateLabel;
	private javax.swing.JLabel calDescLabel;
	private javax.swing.JList calListInfoBox;
	private javax.swing.JScrollPane calListInfoPane;
	private javax.swing.JList calListTaskBox;
	private javax.swing.JScrollPane calListTaskPane;
	private javax.swing.JLabel calStatLabel;
	private javax.swing.JLabel calTimeLabel;
	private javax.swing.JTextField cmdLine;
	private javax.swing.JButton help;
	private javax.swing.JTextField keywords;
	private javax.swing.JList keywordsList;
	private javax.swing.JScrollPane keywordsPane;
	private javax.swing.JTextArea log;
	private javax.swing.JScrollPane logScrollPane;
	private javax.swing.JLabel logo;
	private javax.swing.JPanel panel;
	private javax.swing.JLabel prioDescLabel;
	private javax.swing.JScrollPane prioListTaskPane;
	private javax.swing.JList prioListTaskBox;
	private javax.swing.JList prioListInfoBox;
	private javax.swing.JScrollPane prioListInfoPane;
	private javax.swing.JTextField prioListTextField;
	private javax.swing.JLabel prioPrioLabel;
	private javax.swing.JLabel prioStatLabel;
	private javax.swing.JLabel prioDisplay;
	private javax.swing.JLabel dateDisplay;
	private ImageIcon image = new ImageIcon(ICON_URL);
	private ArrayList<String> userCommandList = new ArrayList<String>();
	// End of variables declaration
}
