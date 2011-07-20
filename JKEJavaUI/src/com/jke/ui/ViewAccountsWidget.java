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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;

import com.jke.beans.AccountBean;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ViewAccountsWidget extends Composite {
	private JKEBanking jke;

	public ViewAccountsWidget(Composite parent, JKEBanking j) {
		super(parent, SWT.NONE);
		jke= j;
		this.setBackground(Utils.WHITE);
		this.getParent().setBackground(Utils.WHITE);
		setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
		GridLayout layout= new GridLayout();
		layout.numColumns= 2;
		setLayout(layout);
	}

	public void createControls() {
		createSelectionList();
	}

	private void createSelectionList() {
		Label main1= new Label(this, SWT.LEFT);
		main1.setText("Account Type");
		main1.setBackground(Utils.WHITE);
		main1.setFont(Utils.BOLD_FONT);

		Label main2= new Label(this, SWT.RIGHT);
		main2.setText("Available Balance");
		main2.setBackground(Utils.WHITE);
		main2.setFont(Utils.BOLD_FONT);

		List<AccountBean> accts= jke.getLoggedInUserAccounts();
		for (final AccountBean b : accts) {
			Link temp= new Link(this, SWT.NONE);
			temp.setText("<a>" + Utils.getAccountTypeName(b.getTypeBean()) + "</a>");
			temp.setBackground(Utils.WHITE);
			temp.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					accountPageView(b);
				}
			});

			Label tempL= new Label(this, SWT.RIGHT);
			tempL.setText(NumberFormat.getCurrencyInstance(Locale.US).format(b.getBalance()));
			tempL.setLayoutData(new GridData(SWT.END, SWT.CENTER, true, false));
			tempL.setBackground(Utils.WHITE);
		}
	}

	private void accountPageView(AccountBean b) {
		System.out.println("Goto: " + b.getType());
		jke.selectAccountInfo(b);
	}
}