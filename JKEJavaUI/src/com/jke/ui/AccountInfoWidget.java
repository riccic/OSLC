/*******************************************************************************
 * Licensed Materials - Property of IBM
 * (c) Copyright IBM Corporation 2010. All Rights Reserved. 
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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.jke.beans.AccountBean;

import java.text.NumberFormat;
import java.util.Locale;

public class AccountInfoWidget extends Composite {
	private JKEBanking jke;
	private AccountBean acct;

	public AccountInfoWidget(Composite parent, AccountBean a, JKEBanking j) {
		super(parent, SWT.NONE);
		this.getParent().setBackground(Utils.WHITE);
		this.setBackground(Utils.WHITE);
		jke= j;
		setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
		GridLayout layout= new GridLayout();
		layout.numColumns= 2;
		setLayout(layout);

		acct= a;
	}

	public void createControls() {
		createAccountInfo();
		createControlButtons();
	}

	private void createAccountInfo() {
		Label l1= new Label(this, SWT.LEFT);
		l1.setText(Utils.getAccountTypeName(acct.getTypeBean()));
		l1.setBackground(Utils.WHITE);
		l1.setFont(Utils.BOLD_FONT);
		new Label(this, SWT.NONE);

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
		l4.setText("Dividends earned this period: ");
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

		Label l8= new Label(c, SWT.LEFT);
		l8.setText("Dividends contributed this period: ");
		l8.setBackground(Utils.WHITE);

		Label l9= new Label(c, SWT.RIGHT);
		l9.setText(NumberFormat.getCurrencyInstance(Locale.US).format(acct.getContributions()));
		l9.setLayoutData(new GridData(SWT.END, SWT.CENTER, true, false));
		l9.setBackground(Utils.WHITE);

		Label l10= new Label(c, SWT.LEFT);
		l10.setText("Dividends contributed to date: ");
		l10.setBackground(Utils.WHITE);

		Label l11= new Label(c, SWT.RIGHT);
		l11.setText(NumberFormat.getCurrencyInstance(Locale.US).format(acct.getContributionsETD()));
		l11.setLayoutData(new GridData(SWT.END, SWT.CENTER, true, false));
		l11.setBackground(Utils.WHITE);
	}

	private void createControlButtons() {
		Composite composite= new Composite(this, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));
		GridLayout layout= new GridLayout();
		layout.numColumns= 2;
		composite.setLayout(layout);
		composite.setBackground(Utils.WHITE);

		Button trans= new Button(composite, SWT.PUSH);
		trans.setText("Transaction History");
		trans.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				jke.selectTransactionHistory(acct);
			}
		});

		Button contribute= new Button(composite, SWT.PUSH);
		contribute.setText("Allocate Dividend Percentage");
		contribute.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				jke.makeDividendDeposit(acct);
			}
		});
	}
}