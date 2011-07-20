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
import com.jke.beans.ContributionBean;
import com.jke.beans.TransactionBean;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class DonationConfirmationWidget extends Composite {
	private JKEBanking jke;
	private ContributionBean cont;
	private TransactionBean proposedTrans;
	private AccountBean proposedAcct;

	public DonationConfirmationWidget(Composite parent, JKEBanking jkeBanking, ContributionBean contribute) {
		super(parent, SWT.NONE);
		this.getParent().setBackground(Utils.WHITE);
		this.setBackground(Utils.WHITE);
		setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
		GridLayout layout= new GridLayout();
		layout.numColumns= 2;
		setLayout(layout);
		jke= jkeBanking;
		this.cont= contribute;
		this.proposedTrans= jke.getTransactionPreview(contribute);
		List<AccountBean> accounts= jke.getLoggedInUserAccounts();
		for (AccountBean accountBean : accounts) {
			if (accountBean.getAccountNumber() == contribute.getAccountNumber())
				this.proposedAcct= accountBean;
		}
	}

	public void createControls() {
		createSelectionList();
		createControlButtons();
	}

	private void createSelectionList() {
		Label l1= new Label(this, SWT.LEFT);
		l1.setText("Donation Confirmation");
		l1.setFont(Utils.BOLD_FONT);
		l1.setBackground(Utils.WHITE);

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
		l2.setText("Organization: ");
		l2.setBackground(Utils.WHITE);

		Label l3= new Label(c, SWT.RIGHT);
		l3.setText(cont.getOrganization());
		l3.setBackground(Utils.WHITE);

		Label l4= new Label(c, SWT.LEFT);
		l4.setText("Amount: ");
		l4.setBackground(Utils.WHITE);

		Label l5= new Label(c, SWT.RIGHT);
		l5.setText(NumberFormat.getCurrencyInstance(Locale.US).format(proposedTrans.getAmount()));
		l5.setLayoutData(new GridData(SWT.END, SWT.CENTER, true, false));
		l5.setBackground(Utils.WHITE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);

		Label l6= new Label(c, SWT.LEFT);
		l6.setText("New Balance: ");
		l6.setBackground(Utils.WHITE);

		Label l7= new Label(c, SWT.RIGHT);
		l7.setText(NumberFormat.getCurrencyInstance(Locale.US).format(proposedAcct.getBalance() - proposedTrans.getAmount()));
		l7.setLayoutData(new GridData(SWT.END, SWT.CENTER, true, false));
		l7.setBackground(Utils.WHITE);

		Label l8= new Label(c, SWT.LEFT);
		l8.setText("Dividends earned: ");
		l8.setBackground(Utils.WHITE);

		Label l9= new Label(c, SWT.RIGHT);
		l9.setText(NumberFormat.getCurrencyInstance(Locale.US).format(proposedAcct.getDividends()));
		l9.setLayoutData(new GridData(SWT.END, SWT.CENTER, true, false));
		l9.setBackground(Utils.WHITE);

		Label l10= new Label(c, SWT.LEFT);
		l10.setText("Dividends earned to date: ");
		l10.setBackground(Utils.WHITE);

		Label l11= new Label(c, SWT.RIGHT);
		l11.setText(NumberFormat.getCurrencyInstance(Locale.US).format(proposedAcct.getDividendsETD()));
		l11.setLayoutData(new GridData(SWT.END, SWT.CENTER, true, false));
		l11.setBackground(Utils.WHITE);

		Label l12= new Label(c, SWT.LEFT);
		l12.setText("Dividends contributed this period: ");
		l12.setBackground(Utils.WHITE);

		Label l13= new Label(c, SWT.RIGHT);
		l13.setBackground(Utils.WHITE);
		l13.setText(NumberFormat.getCurrencyInstance(Locale.US).format(proposedAcct.getContributions()));
		l13.setLayoutData(new GridData(SWT.END, SWT.CENTER, true, false));
		l13.setBackground(Utils.WHITE);

		Label l14= new Label(c, SWT.LEFT);
		l14.setText("Dividends contributed to date: ");
		l14.setBackground(Utils.WHITE);

		Label l15= new Label(c, SWT.RIGHT);
		l15.setText(NumberFormat.getCurrencyInstance(Locale.US).format(proposedAcct.getContributionsETD()));
		l15.setLayoutData(new GridData(SWT.END, SWT.CENTER, true, false));
		l15.setBackground(Utils.WHITE);
	}

	private void createControlButtons() {
		Composite composite= new Composite(this, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.END, SWT.CENTER, true, false));
		GridLayout layout= new GridLayout();
		layout.numColumns= 2;
		composite.setLayout(layout);
		composite.setBackground(Utils.WHITE);
		
		Button cancel = new Button(composite, SWT.PUSH);
		cancel.setText("Cancel");
		cancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				jke.selectAccountWidget();
			}
		});
		
		Button okButton= new Button(composite, SWT.PUSH);
		okButton.setText("Confirm");
		composite.getShell().setDefaultButton(okButton);
		okButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TransactionBean trans= jke.postTransaction(cont);
				jke.donationReceiptWigdet(trans);
			}
		});
	}
}