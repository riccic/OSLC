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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.jke.beans.TransactionBean;

public class DonationReceiptWidget extends Composite {
	private TransactionBean tran;
	private JKEBanking jke;

	public DonationReceiptWidget(Composite parent, JKEBanking jkeBanking, TransactionBean tran) {
		super(parent, SWT.NONE);
		this.getParent().setBackground(Utils.WHITE);
		this.setBackground(Utils.WHITE);
		setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
		GridLayout layout= new GridLayout();
		layout.numColumns= 1;
		setLayout(layout);
		this.tran= tran;
		this.jke= jkeBanking;
	}

	public void createControls() {
		createSelectionList();
		createControlButtons();
	}

	private void createSelectionList() {
		Label mesg= new Label(this, SWT.NONE);
		mesg.setText("Thank You for your donation to " + tran.getSource() + ". Your transaction number is " + tran.getTransactionID() + ".");
		mesg.setBackground(Utils.WHITE);
	}

	private void createControlButtons() {
		Composite composite= new Composite(this, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.END, SWT.CENTER, true, false));
		GridLayout layout= new GridLayout();
		layout.numColumns= 1;
		composite.setLayout(layout);
		composite.setBackground(Utils.WHITE);
		Button okButton= new Button(composite, SWT.PUSH);
		okButton.setText("Return Home");
		okButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				jke.selectAccountWidget();
			}
		});
	}
}