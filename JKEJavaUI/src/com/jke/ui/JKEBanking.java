/*******************************************************************************
 * Licensed Materials - Property of IBM
 * (c) Copyright IBM Corporation 2010, 2011. All Rights Reserved. 
 * 
 * Note to U.S. Government Users Restricted Rights:  Use, 
 * duplication or disclosure restricted by GSA ADP Schedule 
 * Contract with IBM Corp.
 *******************************************************************************/

package com.jke.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.ibm.team.json.JSONArray;
import com.ibm.team.json.JSONObject;
import com.jke.beans.AccountBean;
import com.jke.beans.ContributionBean;
import com.jke.beans.TransactionBean;
import com.jke.beans.UserBean;
import com.jke.server.JKEServer;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JKEBanking {
	private static final String JKE_TITLE= "JKE Banking";
	private UserBean fLoggedInUser;
	private List<String> fOrganizations;
	private List<AccountBean> fAccounts;
	private Map<String, List<TransactionBean>> fTransactions;

	// Widely used controls
	private Display fDisplay;
	private Shell fShell;
	private MenuItem fLogoutItem;
	private MenuItem fTransactionHistoryItem;
	private MenuItem fAccountHomeItem;
	private MenuItem fDividendDepositItem;
	private Image fImage;
	private Composite fActionComposite;
	private Label fHeader;
	private Label fUsernameLabel;
	private Text fUsernameField;
	private Label fPasswordLabel;
	private Text fPasswordField;

	private static Thread t;
	public static SimpleHttpClient client= new SimpleHttpClient();
	private Canvas fImageCanvas;

	public JKEBanking(Display display) {
		fLoggedInUser= null;
		fDisplay= display;
	}

	public static void main(String[] args) {
		if (args.length > 0 && (args[0].equals("-server") || args[0].equals("-s"))) {
			t= new Thread("Embedded Server") {
				public void run() {
					JKEServer.main(new String[0]);
				};
			};
			t.start();
		}
		Display.setAppName("JKE");
		Display display= new Display();
		JKEBanking application= new JKEBanking(display);
		Shell shell= application.open();
		shell.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				if (t != null)
					JKEServer.stop();
			}
		});
		shell.setBackground(Utils.WHITE);
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	public List<AccountBean> getLoggedInUserAccounts() {
		return fAccounts;
	}

	public Map<String, List<TransactionBean>> getLoggedInUserTransactions() {
		return fTransactions;
	}

	public String[] getOrganizations() {
		return (String[]) fOrganizations.toArray(new String[fOrganizations.size()]);
	}

	public TransactionBean getTransactionPreview(ContributionBean contribution) {
		return client.getTransactionPreview(contribution);
	}

	public TransactionBean postTransaction(ContributionBean contribution) {
		TransactionBean t= client.postTransaction(contribution);
		try {
			fetchDataFromServer(fLoggedInUser);
		} catch (IOException e) {
			showError(fShell, "Cannot Update Data From Server", e);
		}
		return t;
	}

	/**
	 * Create the controls to select a squawker, show its image and its text.
	 * 
	 * @param display
	 * @return
	 */
	public Shell open() {
		Utils.init(fDisplay);

		/* Extra call to a library for Rational Functional Tester */
		try {
			// Self-enable this application. The object passed to 
			// enableSwtUi must be an object or class whose classLoader 
			//has access to the SWT classes. 
			//enableSwtUi() must be called from the SWT UI Thread. 
			//If the JRE in which the application is running is enabled 
			//by IBM Rational Functional Tester it will then be testable. 
			//If the JRE is not enabled, a ClassNotFoundException will 
			//be caught here and the application will load as usual but 
			//it will not be testable. 
			Class<?> bootstrap= Class.forName("com.rational.test.ft.bootstrap.Bootstrap");
			if (bootstrap != null) {
				Method method= bootstrap.getMethod("enableSwtUi", Object.class);
				if (method != null)
					method.invoke(this, this);
			}
		} catch (Throwable e) {
		}

		fShell= new Shell(fDisplay, SWT.SHELL_TRIM | SWT.LEFT_TO_RIGHT);
		fShell.setText(JKE_TITLE);
		fShell.setImage(new Image(fDisplay, getClass().getResourceAsStream("icon.jpg")));
		GridLayout shellLayout= new GridLayout();
		shellLayout.marginHeight= 0;
		fShell.setLayout(shellLayout);

		createMenuBar();
		createHeading();
		buildLogin();

		fShell.setSize(600, 480);
		fShell.open();

		return fShell;
	}

	private void createEmptyActionComposite() {
		if (fActionComposite != null) {
			fActionComposite.dispose();
			fActionComposite= null;
		}

		fActionComposite= new Composite(fShell, SWT.NONE);
		GridData actionGridData= new GridData(SWT.CENTER, SWT.CENTER, false, false);
		fActionComposite.setLayoutData(actionGridData);
		GridLayout layout= new GridLayout();
		layout.numColumns= 1;
		fActionComposite.setLayout(layout);
	}

	protected void buildLogin() {
		createEmptyActionComposite();
		GridLayout layout= new GridLayout();

		GridData data= new GridData();
		data.widthHint= 100;
		data.verticalAlignment= GridData.FILL;
		data.grabExcessHorizontalSpace= false;
		data.grabExcessVerticalSpace= false;

		layout.numColumns= 2;
		layout.marginTop= 20;
		fActionComposite.setLayout(layout);
		fActionComposite.setBackground(Utils.WHITE);
		fUsernameLabel= new Label(fActionComposite, SWT.CENTER);
		fUsernameLabel.setText("Username:");
		fUsernameLabel.setBackground(Utils.WHITE);
		fUsernameField= new Text(fActionComposite, SWT.SINGLE | SWT.BORDER);
		fUsernameField.setLayoutData(data);

		fPasswordLabel= new Label(fActionComposite, SWT.LEFT);
		fPasswordLabel.setText("Password:");
		fPasswordLabel.setBackground(Utils.WHITE);
		fPasswordField= new Text(fActionComposite, SWT.SINGLE | SWT.BORDER);
		fPasswordField.setEchoChar('*');
		fPasswordField.setLayoutData(data);

		new Label(fActionComposite, SWT.LEFT);
		Button okButton= new Button(fActionComposite, SWT.PUSH);
		okButton.setText("Login");
		okButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				try {
					String userObj= client.request(client.getUserResource(fUsernameField.getText()));
					UserBean user= new UserBean();
					user.deserializeFromJson(new StringReader(userObj));
					fetchDataFromServer(user);
					login(user);
				} catch (IOException ex) {
					showError(fShell, "Cannot Login", ex);
				}
			}
		});
		data.verticalAlignment= GridData.VERTICAL_ALIGN_END;
		okButton.setLayoutData(data);
		fShell.setDefaultButton(okButton);
		fShell.layout();
	}

	public static void showError(Shell parent, String message, Exception e) {
		MessageBox messageBox= new MessageBox(parent, SWT.OK | SWT.ICON_ERROR);
		if (e != null) {
			message+= "\n\n" + e.getLocalizedMessage();
		}
		messageBox.setMessage(message);
		messageBox.open();
	}

	protected void login(UserBean u) {
		if (u != null) {
			fLoggedInUser= u;
			fImageCanvas.redraw();
			System.out.println("User: " + u.getUserName() + " logged in.");
			enableMenuItems();
			selectAccountWidget();
		} else
			showError(fShell, "Incorrect Login, please try again.", null);
	}

	private void logout() {
		fLoggedInUser= null;
		fImageCanvas.redraw();
		fHeader.setText("Account Access");
		enableMenuItems();
		buildLogin();
	}

	private void refreshDataFromServer() {
		if (fLoggedInUser != null) {
			try {
				fetchDataFromServer(fLoggedInUser);
			} catch (IOException e) {
				showError(fShell, "Cannot refresh data from server.", e);
			}
		}
	}

	private AccountBean refresh(AccountBean b) {
		if (b != null) {
			for (AccountBean account : fAccounts) {
				if (account.getAccountNumber() == b.getAccountNumber())
					return account;
			}
		}

		return b;
	}

	private void fetchDataFromServer(UserBean user) throws IOException {
		String orgsObj= client.request(client.getOrganizations());
		JSONArray orgsJson= JSONArray.parse(new StringReader(orgsObj));
		fOrganizations= new ArrayList<String>(orgsJson.size());
		for (Object object : orgsJson) {
			if (object instanceof JSONObject) {
				fOrganizations.add((String) ((JSONObject) object).get("name"));
			}
		}
		String accountsObj= client.request(client.getUserAccounts(user.getUserName()));
		JSONArray accountsJson= JSONArray.parse(new StringReader(accountsObj));
		fTransactions= new HashMap<String, List<TransactionBean>>();
		fAccounts= new ArrayList<AccountBean>();
		for (Object object : accountsJson) {
			if (object instanceof JSONObject) {
				AccountBean account= new AccountBean();
				account.fromJson((JSONObject) object);
				fAccounts.add(account);
				List<TransactionBean> history= new ArrayList<TransactionBean>();
				String accountTransObj= client.request(client.getTransactionsForAccount(user.getUserName(), account.getType()));
				JSONArray accountTransJson= JSONArray.parse(new StringReader(accountTransObj));
				for (Object object2 : accountTransJson) {
					if (object instanceof JSONObject) {
						TransactionBean t= new TransactionBean();
						t.fromJson((JSONObject) object2);
						history.add(t);
					}
				}
				fTransactions.put(account.getType(), history);
			}
		}
	}

	private void createHeading() {
		Composite headingComposite= new Composite(fShell, SWT.NONE);
		headingComposite.setBackground(Utils.WHITE);
		GridData headingGridData= new GridData(SWT.CENTER, SWT.BEGINNING, true, false);
		headingComposite.setLayoutData(headingGridData);

		GridLayout layout= new GridLayout();
		layout.marginHeight= 0;
		headingComposite.setLayout(layout);

		fImageCanvas= new Canvas(headingComposite, SWT.NONE);
		GridData imageCanvasGridData= new GridData(SWT.CENTER, SWT.BEGINNING, true, false);
		imageCanvasGridData.widthHint= 500;
		imageCanvasGridData.heightHint= 58;
		fImageCanvas.setBackground(Utils.WHITE);
		fImageCanvas.setLayoutData(imageCanvasGridData);
		fImageCanvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent event) {
				if ((fImage != null) && (!fImage.isDisposed())) {
					event.gc.drawImage(fImage, 0, 0);
					if (fLoggedInUser != null) {
						String welcomeStr= "Welcome, " + fLoggedInUser.getFirstName();

						event.gc.setFont(Utils.BOLD_FONT);
						event.gc.setForeground(Utils.GRAY);
						Point textSize= event.gc.textExtent(welcomeStr, SWT.DRAW_TRANSPARENT);

						event.gc.drawText(welcomeStr, 500 - textSize.x - 20, 5, true);
					}
				}
			}
		});
		fImage= new Image(fShell.getDisplay(), getClass().getResourceAsStream("jke_banner.png"));
		fImageCanvas.redraw();

		GridData headerData= new GridData(SWT.CENTER, SWT.CENTER, true, false);
		headerData.widthHint= 300;
		fHeader= new Label(headingComposite, SWT.CENTER);
		fHeader.setLayoutData(headerData);
		fHeader.setBackground(Utils.WHITE);
		fHeader.setText("Account Access");
		fHeader.setFont(Utils.WELCOME_FONT);
	}

	private void createMenuBar() {
		Menu menuBar= new Menu(fShell, SWT.BAR);
		fShell.setMenuBar(menuBar);

		MenuItem jkeMenuItem= new MenuItem(menuBar, SWT.CASCADE);
		jkeMenuItem.setText("&JKE Banking");
		Menu jkeMenu= new Menu(jkeMenuItem);
		jkeMenuItem.setMenu(jkeMenu);

		fLogoutItem= new MenuItem(jkeMenu, SWT.NONE);
		fLogoutItem.setText("&Logout");
		fLogoutItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				logout();
			}
		});

		MenuItem accountMenuItem= new MenuItem(menuBar, SWT.CASCADE);
		accountMenuItem.setText("&Account Services");
		Menu accountMenu= new Menu(accountMenuItem);
		accountMenuItem.setMenu(accountMenu);

		MenuItem accountInfoItem= new MenuItem(accountMenu, SWT.CASCADE);
		accountInfoItem.setText("&Account Information");
		Menu accountInfoMenu= new Menu(accountInfoItem);
		accountInfoItem.setMenu(accountInfoMenu);

		fAccountHomeItem= new MenuItem(accountInfoMenu, SWT.NONE);
		fAccountHomeItem.setText("Account &Summary");
		fAccountHomeItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectAccountWidget();
			}
		});

		fTransactionHistoryItem= new MenuItem(accountInfoMenu, SWT.NONE);
		fTransactionHistoryItem.setText("&Transaction History");
		fTransactionHistoryItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectTransactionHistory(null);
			}
		});

		createDisabledMenuItem(accountInfoMenu, "Account &Profile");

		MenuItem servicesItem= new MenuItem(accountMenu, SWT.CASCADE);
		servicesItem.setText("&Services");
		Menu servicesMenu= new Menu(servicesItem);
		servicesItem.setMenu(servicesMenu);

		createDisabledMenuItem(servicesMenu, "&Certificate of Deposit");
		createDisabledMenuItem(servicesMenu, "&Open New Account");
		createDisabledMenuItem(servicesMenu, "Close &Account");
		createDisabledMenuItem(servicesMenu, "&Policy Change Request");
		createDisabledMenuItem(servicesMenu, "Policy Change &Status");

		MenuItem mtmMenuItem= new MenuItem(menuBar, SWT.CASCADE);
		mtmMenuItem.setText("&Money That Matters");
		Menu mtmMenu= new Menu(mtmMenuItem);
		mtmMenuItem.setMenu(mtmMenu);

		fDividendDepositItem= new MenuItem(mtmMenu, SWT.NONE);
		fDividendDepositItem.setText("&Dividend Contribution");
		fDividendDepositItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				makeDividendDeposit(null);
			}
		});

		MenuItem financialItem= new MenuItem(menuBar, SWT.CASCADE);
		financialItem.setText("&Financial Services");
		Menu finMenu= new Menu(financialItem);
		financialItem.setMenu(finMenu);

		createDisabledMenuItem(finMenu, "&Auto Loans");
		createDisabledMenuItem(finMenu, "&Vacation Homw Finance");
		createDisabledMenuItem(finMenu, "&Today's Rates");
		createDisabledMenuItem(finMenu, "&Refinancing");
		createDisabledMenuItem(finMenu, "&Mortgage Details");
		createDisabledMenuItem(finMenu, "&Home Equity Lines");
		createDisabledMenuItem(finMenu, "Amorit&ization");

		enableMenuItems();
	}
	
	private void createDisabledMenuItem(Menu parent, String text) {
		MenuItem item= new MenuItem(parent, SWT.NONE);
		item.setText(text);
		item.setEnabled(false);
	}

	private void enableMenuItems() {
		boolean loggedIn= (fLoggedInUser != null);
		fLogoutItem.setEnabled(loggedIn);
		fTransactionHistoryItem.setEnabled(loggedIn);
		fAccountHomeItem.setEnabled(loggedIn);
		fDividendDepositItem.setEnabled(loggedIn);
	}

	protected void selectTransactionHistory(AccountBean b) {
		fHeader.setText("Transactions");
		createEmptyActionComposite();
		refreshDataFromServer();
		TransactionHistoryWidget widget= new TransactionHistoryWidget(fActionComposite, this, refresh(b));
		widget.createControls();
		fShell.layout();
	}

	protected void selectAccountInfo(AccountBean b) {
		fHeader.setText("Accounts Details");
		createEmptyActionComposite();
		refreshDataFromServer();
		AccountInfoWidget widget= new AccountInfoWidget(fActionComposite, refresh(b), this);
		widget.createControls();
		fShell.layout();
	}

	protected void selectAccountWidget() {
		fHeader.setText("Accounts Overview");
		createEmptyActionComposite();
		refreshDataFromServer();
		ViewAccountsWidget widget= new ViewAccountsWidget(fActionComposite, this);
		widget.createControls();
		fShell.layout();
	}

	protected void makeDividendDeposit(AccountBean acct) {
		fHeader.setText("Money That Matters");
		createEmptyActionComposite();
		DividendDepositWidget widget= new DividendDepositWidget(fActionComposite, this, acct);
		widget.createControls();
		fShell.layout();
	}

	protected void dividendDepositConfirmWidget(ContributionBean contribute) {
		fHeader.setText("Money That Matters");
		createEmptyActionComposite();
		DonationConfirmationWidget widget= new DonationConfirmationWidget(fActionComposite, this, contribute);
		widget.createControls();
		fShell.layout();
	}

	protected void donationReceiptWigdet(TransactionBean tran) {
		createEmptyActionComposite();
		DonationReceiptWidget widget= new DonationReceiptWidget(fActionComposite, this, tran);
		widget.createControls();
		fShell.layout();
	}
}