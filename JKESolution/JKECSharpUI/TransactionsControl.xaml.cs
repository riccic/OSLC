/*******************************************************************************
 * Licensed Materials - Property of IBM
 * (c) Copyright IBM Corporation 2010. All Rights Reserved. 
 * 
 * Note to U.S. Government Users Restricted Rights:  Use, 
 * duplication or disclosure restricted by GSA ADP Schedule 
 * Contract with IBM Corp.
 *******************************************************************************/

using System;
using System.Windows;
using System.Windows.Controls;
using JKEBusinessData;
using JKEService;

namespace JKECSharpUI
{
    /// <summary>
    /// Interaction logic for TransactionsControl.xaml
    /// </summary>
    public partial class TransactionsControl : UserControl
    {
        public TransactionsControl()
        {
            InitializeComponent();
        }

        public void Populate(UserResource user, AccountResource account)
        {
            EnableSelection(false);
            Populate(account);
        }

        public void Populate(UserResource user)
        {
            EnableSelection(true);
            try
            {
                IJKEService channel = ServiceManager.GetChannel();
                AccountResource[] accounts = channel.GetUserAccounts(user.UserName);
                AccountsComboBox.ItemsSource = accounts;
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
            this.DataContext = null;
        }

        private void Populate(AccountResource account)
        {
            try
            {
                IJKEService channel = ServiceManager.GetChannel();
                TransactionResource[] transactions = channel.GetTransactionsForAccount(account.UserName, account.Type);
                AccountTransactions data = new AccountTransactions(account, transactions);
                this.DataContext = data;
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void EnableSelection(bool flag)
        {
            AccountTextBlock.Visibility = flag ? Visibility.Collapsed : Visibility.Visible;
            SelectAccountGrid.Visibility = flag ? Visibility.Visible : Visibility.Collapsed;
        }

        private void AccountsComboBox_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            AccountResource account = AccountsComboBox.SelectedItem as AccountResource;
            if (account != null)
                Populate(account);
        }

        class AccountTransactions
        {
            private AccountResource account;
            private TransactionResource[] transactions;

            public AccountResource Account { get { return this.account; } }
            public TransactionResource[] Transactions { get { return this.transactions; } }

            public AccountTransactions(AccountResource account, TransactionResource[] transactions)
            {
                this.account = account;
                this.transactions = transactions;
            }
        }
    }
}
