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
using System.Windows.Documents;
using JKEBusinessData;
using JKEService;

namespace JKECSharpUI
{
    /// <summary>
    /// Interaction logic for AccountsOverviewControl.xaml
    /// </summary>
    public partial class AccountsOverviewControl : UserControl
    {
        public event EventHandler<AccountArgs> AccountDetails;

        public AccountsOverviewControl()
        {
            InitializeComponent();
        }

        public void Populate(UserResource user)
        {
            try
            {
                IJKEService channel = ServiceManager.GetChannel();
                AccountResource[] accounts = channel.GetUserAccounts(user.UserName);
                this.DataContext = accounts;
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void Hyperlink_Click(object sender, RoutedEventArgs e)
        {
            Hyperlink hyperlink = sender as Hyperlink;
            if (hyperlink != null)
            {
                AccountResource account = hyperlink.DataContext as AccountResource;
                if (account != null && AccountDetails != null)
                    AccountDetails(this, new AccountArgs(account));
            }
        }
    }

    public class AccountArgs : EventArgs
    {
        private AccountResource account;
        public AccountResource Account { get { return account; } }

        public AccountArgs(AccountResource account)
        {
            this.account = account;
        }
    }
}