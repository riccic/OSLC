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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.jke.beans.AccountBean;
import com.jke.beans.TransactionBean;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * The Class TransactionHistoryWidget.
 */
public class TransactionHistoryWidget extends Composite {
	private static final String ENTER_ACCOUNT_NUMBER_TEXT= "Please select an Account from the list:";
	private static final String[] TABLE_TITLES= { "ID", "Type", "Source", "Amount", "Remaining Balance", "Date" };
	private static final int[] TABLE_STYLES= { SWT.NONE, SWT.NONE, SWT.NONE, SWT.RIGHT, SWT.RIGHT, SWT.NONE };
	private AccountBean acct;
	private Combo fAccountTypeCombo;
	private Table fTransactionsTable;
	private final JKEBanking jke;

	/**
	 * Instantiates a new transaction history widget.
	 * 
	 * @param parent the parent
	 * @param u the u
	 * @param b
	 */
	public TransactionHistoryWidget(Composite parent, JKEBanking jke, AccountBean b) {
		super(parent, SWT.NONE);
		this.jke= jke;
		this.getParent().setBackground(Utils.WHITE);
		this.setBackground(Utils.WHITE);
		setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, true));
		GridLayout layout= new GridLayout();
		layout.numColumns= 2;
		setLayout(layout);

		acct= b;
	}

	/**
	 * Creates the controls.
	 */
	public void createControls() {
		createSelectionList();
	}

	/**
	 * Creates the selection list.
	 */
	private void createSelectionList() {
		Label acctEntryLabel= new Label(this, SWT.LEFT);
		if (acct == null)
			acctEntryLabel.setText(ENTER_ACCOUNT_NUMBER_TEXT);
		else
			acctEntryLabel.setText(Utils.getAccountTypeName(acct.getTypeBean()));
		acctEntryLabel.setBackground(Utils.WHITE);
		acctEntryLabel.setFont(Utils.BOLD_FONT);

		if (acct == null) {
			fAccountTypeCombo= new Combo(this, SWT.DROP_DOWN | SWT.READ_ONLY);
			GridData acctTypeGD= new GridData();
			acctTypeGD.horizontalSpan= 2;
			fAccountTypeCombo.setLayoutData(acctTypeGD);
			List<AccountBean> accounts= jke.getLoggedInUserAccounts();
			List<String> names= new ArrayList<String>(accounts.size());
			for (AccountBean account : accounts) {
				names.add(Utils.getAccountTypeName(account.getTypeBean()));
			}
			fAccountTypeCombo.setItems((String[]) names.toArray(new String[0]));

			fAccountTypeCombo.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					fTransactionsTable.removeAll();
					int selectionIndex= fAccountTypeCombo.getSelectionIndex();
					AccountBean accountBean= jke.getLoggedInUserAccounts().get(selectionIndex);

					List<TransactionBean> transactions= jke.getLoggedInUserTransactions().get(accountBean.getType());
					for (TransactionBean transaction : transactions) {
						TableItem item= new TableItem(fTransactionsTable, SWT.NONE);
						item.setText(0, transaction.getTransactionID() + "");
						item.setText(1, transaction.getTransactionType());
						item.setText(2, transaction.getSource());
						item.setText(3, NumberFormat.getCurrencyInstance(Locale.US).format(transaction.getAmount()));
						item.setText(4, NumberFormat.getCurrencyInstance(Locale.US).format(transaction.getPostBalance()));
						item.setText(5, transaction.getDate());
					}

					for (TableColumn col : fTransactionsTable.getColumns()) {
						col.pack();
					}
				}
			});
		}

		fTransactionsTable= new Table(this, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		fTransactionsTable.setLinesVisible(true);
		fTransactionsTable.setHeaderVisible(true);
		GridData data= new GridData(SWT.FILL, SWT.FILL, true, true);
		data.heightHint= 200;
		data.widthHint= 520;
		data.horizontalSpan= 2;
		fTransactionsTable.setLayoutData(data);
		for (int i= 0; i < TABLE_TITLES.length; i++) {
			TableColumn column= new TableColumn(fTransactionsTable, TABLE_STYLES[i]);
			column.setText(TABLE_TITLES[i]);
		}

		if (acct != null) {
			List<TransactionBean> transactions= jke.getLoggedInUserTransactions().get(acct.getTypeBean().name());
			for (TransactionBean transaction : transactions) {
				TableItem item= new TableItem(fTransactionsTable, SWT.NONE);
				item.setText(0, transaction.getTransactionID() + "");
				item.setText(1, transaction.getTransactionType());
				item.setText(2, transaction.getSource());
				item.setText(3, NumberFormat.getCurrencyInstance(Locale.US).format(transaction.getAmount()));
				item.setText(4, NumberFormat.getCurrencyInstance(Locale.US).format(transaction.getPostBalance()));
				item.setText(5, transaction.getDate());
			}

			for (TableColumn col : fTransactionsTable.getColumns()) {
				col.pack();
			}
		}
	}
}