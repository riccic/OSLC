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

namespace JKECSharpUI
{
    /// <summary>
    /// Interaction logic for AccountDetailsControl.xaml
    /// </summary>
    public partial class AccountDetailsControl : UserControl
    {
        public event EventHandler<AccountArgs> TransactionHistory;
        public event EventHandler<AccountArgs> AllocatePercentage;

        public AccountDetailsControl()
        {
            InitializeComponent();
        }

        public void Populate(AccountResource account)
        {
            this.DataContext = account;
        }

        private void Transaction_History_Click(object sender, RoutedEventArgs e)
        {
            if (TransactionHistory != null)
                TransactionHistory(this, new AccountArgs(this.DataContext as AccountResource));
        }

        private void Allocate_Percentage_Click(object sender, RoutedEventArgs e)
        {
            if (AllocatePercentage != null)
                AllocatePercentage(this, new AccountArgs(this.DataContext as AccountResource));
        }
    }
}
