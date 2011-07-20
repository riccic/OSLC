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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.jke.beans.AccountBean;
import com.jke.beans.ContributionBean;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DividendDepositWidget extends Composite {
	private static final String SELECT_ACCOUNT_MSG= "Select an account type.";
	private static final String POSITIVE_PERCENTAGE_MSG= "Enter a positive percentage.";
	private static final String BUTTON_NEXT= "Next";
	private static final String BUTTON_CANCEL= "Cancel";

	private JKEBanking jke;
	private AccountBean acct;
	private Combo fAccountTypeField;
	private Combo fOrganizationCombo;
	private Text fPercentageField;
	private Map<String, AccountBean> nameToAccount;

	/**
	 * @param jkeBanking
	 * @param acct
	 */
	public DividendDepositWidget(Composite parent, JKEBanking jkeBanking, AccountBean acct) {
		super(parent, SWT.NONE);
		this.getParent().setBackground(Utils.WHITE);
		this.setBackground(Utils.WHITE);
		setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
		GridLayout layout= new GridLayout();
		layout.numColumns= 2;
		setLayout(layout);
		jke= jkeBanking;
		this.acct= acct;
	}

	public void createControls() {
		createEntryFields();
		createControlButtons();
	}

	private void createEntryFields() {
		if (acct != null) {
			Label l1= new Label(this, SWT.LEFT);
			l1.setBackground(Utils.WHITE);
			l1.setText(Utils.getAccountTypeName(acct.getTypeBean()));
			l1.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 2, 1));
			l1.setFont(Utils.BOLD_FONT);

			Composite c= new Composite(this, SWT.BORDER);
			c.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 2, 1));
			c.setBackground(Utils.WHITE);

			GridLayout layout= new GridLayout(2, false);
			layout.marginWidth= 10;
			layout.horizontalSpacing= 30;
			layout.marginHeight= 10;

			c.setLayout(layout);

			Label l2= new Label(c, SWT.LEFT);
			l2.setText("Balance: ");
			l2.setBackground(Utils.WHITE);

			Label l3= new Label(c, SWT.RIGHT);
			l3.setText(NumberFormat.getCurrencyInstance(Locale.US).format(acct.getBalance()));
			l3.setLayoutData(new GridData(SWT.END, SWT.CENTER, true, false));
			l3.setBackground(Utils.WHITE);

			Label l4= new Label(c, SWT.LEFT);
			l4.setText("Dividends earned: ");
			l4.setBackground(Utils.WHITE);

			Label l5= new Label(c, SWT.RIGHT);
			l5.setText(NumberFormat.getCurrencyInstance(Locale.US).format(acct.getDividends()));
			l5.setLayoutData(new GridData(SWT.END, SWT.CENTER, true, false));
			l5.setBackground(Utils.WHITE);

			Label l6= new Label(c, SWT.LEFT);
			l6.setText("Dividends earned to date: ");
			l6.setBackground(Utils.WHITE);

			Label l7= new Label(c, SWT.RIGHT);
			l7.setText(NumberFormat.getCurrencyInstance(Locale.US).format(acct.getDividendsETD()));
			l7.setLayoutData(new GridData(SWT.END, SWT.CENTER, true, false));
			l7.setBackground(Utils.WHITE);
		}

		new Label(this, SWT.NONE).setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 2, 1));

		if (acct == null) {
			Label accountTypeLabel= new Label(this, SWT.LEFT);
			accountTypeLabel.setText("Account Type:");
			accountTypeLabel.setBackground(Utils.WHITE);
			fAccountTypeField= new Combo(this, SWT.DROP_DOWN | SWT.READ_ONLY);
			List<AccountBean> accounts= jke.getLoggedInUserAccounts();
			List<String> names= new ArrayList<String>(accounts.size());
			nameToAccount= new HashMap<String, AccountBean>();
			for (AccountBean account : accounts) {
				names.add(Utils.getAccountTypeName(account.getTypeBean()));
				nameToAccount.put(Utils.getAccountTypeName(account.getTypeBean()), account);
			}
			fAccountTypeField.setItems((String[]) names.toArray(new String[names.size()]));
		}

		Label organizationLabel= new Label(this, SWT.LEFT);
		organizationLabel.setText("Organization:");
		organizationLabel.setBackground(Utils.WHITE);
		fOrganizationCombo= new Combo(this, SWT.DROP_DOWN | SWT.READ_ONLY);
		fOrganizationCombo.setItems(jke.getOrganizations());
		fOrganizationCombo.select(0);

		Label percentageLabel= new Label(this, SWT.LEFT);
		percentageLabel.setText("Percentage:");
		percentageLabel.setBackground(Utils.WHITE);
		fPercentageField= new Text(this, SWT.SINGLE | SWT.BORDER);
		GridData data= new GridData();
		data.widthHint= 50;
		fPercentageField.setLayoutData(data);
		new Label(this, SWT.RIGHT);

		if (acct == null)
			fAccountTypeField.setFocus();
		else
			fPercentageField.setFocus();
	}

	private void createControlButtons() {
		Composite composite= new Composite(this, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.END, SWT.CENTER, true, false));

		GridLayout layout= new GridLayout();
		layout.numColumns= 2;
		composite.setLayout(layout);
		composite.setBackground(Utils.WHITE);

		final Button nextButton= new Button(composite, SWT.PUSH);
		nextButton.setText(BUTTON_NEXT);
		composite.getShell().setDefaultButton(nextButton);
		nextButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (fAccountTypeField != null && fAccountTypeField.getText().length() == 0) {
					JKEBanking.showError(nextButton.getShell(), SELECT_ACCOUNT_MSG, null);
					return;
				}

				double percentage= 0.0;
				try {
					percentage= Double.parseDouble(fPercentageField.getText());
					if (percentage <= 0.0) {
						JKEBanking.showError(nextButton.getShell(), POSITIVE_PERCENTAGE_MSG, null);
						return;
					}
				} catch (NumberFormatException nfe) {
					JKEBanking.showError(nextButton.getShell(), POSITIVE_PERCENTAGE_MSG, nfe);
					return;
				}

				ContributionBean contribute;
				if (acct == null) {
					AccountBean account= nameToAccount.get(fAccountTypeField.getText());
					contribute= new ContributionBean(account.getAccountNumber(), fOrganizationCombo.getText(), percentage, new SimpleDateFormat("MM/dd/yyyy").format(new Date()));
					jke.dividendDepositConfirmWidget(contribute);
				} else {
					contribute= new ContributionBean(acct.getAccountNumber(), fOrganizationCombo.getText(), percentage, new SimpleDateFormat("MM/dd/yyyy").format(new Date()));
					jke.dividendDepositConfirmWidget(contribute);
				}
			}
		});

		final Button cancelButton= new Button(composite, SWT.PUSH);
		cancelButton.setText(BUTTON_CANCEL);
		cancelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				jke.selectAccountWidget();
			}
		});
	}
}