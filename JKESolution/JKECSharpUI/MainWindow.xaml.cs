/*******************************************************************************
 * Licensed Materials - Property of IBM
 * (c) Copyright IBM Corporation 2010. All Rights Reserved. 
 * 
 * Note to U.S. Government Users Restricted Rights:  Use, 
 * duplication or disclosure restricted by GSA ADP Schedule 
 * Contract with IBM Corp.
 *******************************************************************************/

using System;
using System.ComponentModel;
using System.Windows;
using JKEBusinessData;
using JKECSharpUI.wizard;
using JKEService;

namespace JKECSharpUI
{
    /// <summary>
    /// Interaction logic for Window1.xaml
    /// </summary>

    public partial class MainWindow : System.Windows.Window, INotifyPropertyChanged
    {
        private UserResource user;
        public bool IsLoggedIn { get { return user != null; } }

        public MainWindow()
        {
            InitializeComponent();
            this.DataContext = this;
            this.AccountAccessControl.LogIn += new EventHandler<LoginArgs>(AccountAccessControl_LogIn);
            this.AccountsOverviewControl.AccountDetails += new EventHandler<AccountArgs>(AccountsOverviewControl_AccountDetails);
            this.AccountDetailsControl.TransactionHistory += new EventHandler<AccountArgs>(AccountDetailsControl_TransactionHistory);
            this.AccountDetailsControl.AllocatePercentage += new EventHandler<AccountArgs>(AccountDetailsControl_AllocatePercentage);
        }

        #region INotifyPropertyChanged Members

        public event PropertyChangedEventHandler PropertyChanged;

        #endregion

        protected virtual void OnChanged(string propertyName)
        {
            if (PropertyChanged != null)
                PropertyChanged(this, new PropertyChangedEventArgs(propertyName));
        }

        #region Event Handlers
        private void Window_Closed(object sender, EventArgs e)
        {
            try
            {
                ServiceManager.CloseFactory();
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void Logout_Click(object sender, RoutedEventArgs e)
        {
            HideAll();
            AccountAccessGrid.Visibility = Visibility.Visible;

            this.user = null;
            NameText.Text = null;
            this.AccountAccessControl.Clear();
            OnChanged("IsLoggedIn");
        }

        void AccountAccessControl_LogIn(object sender, LoginArgs e)
        {
            try
            {
                IJKEService channel = ServiceManager.GetChannel();
                this.user = channel.GetUserResource(e.Username);
                NameText.Text = string.Format("Welcome, {0}", this.user.FirstName);
                ShowAccountsOverview();
                OnChanged("IsLoggedIn");
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        void AccountsOverviewControl_AccountDetails(object sender, AccountArgs e)
        {
            HideAll();
            AccountDetailsGrid.Visibility = Visibility.Visible;
            AccountDetailsControl.Populate(e.Account);
        }

        void AccountDetailsControl_TransactionHistory(object sender, AccountArgs e)
        {
            HideAll();
            TransactionsGrid.Visibility = Visibility.Visible;
            TransactionsControl.Populate(this.user, e.Account);
        }

        private void Account_Summary_Click(object sender, RoutedEventArgs e)
        {
            ShowAccountsOverview();
        }

        void AccountDetailsControl_AllocatePercentage(object sender, AccountArgs e)
        {
            // Launch the wizard
            LaunchWizard(new AccountLauncher(e.Account));
        }

        void wizardLauncher_WizardReturn(object sender, WizardReturnEventArgs e)
        {
            // Handle wizard return
            ShowAccountsOverview();
        }

        private void Dividend_Contribution_Click(object sender, RoutedEventArgs e)
        {
            // Launch the wizard
            LaunchWizard(new DividendLauncher(this.user));
        }

        private void Transaction_History_Click(object sender, RoutedEventArgs e)
        {
            try
            {
                HideAll();
                TransactionsGrid.Visibility = Visibility.Visible;
                TransactionsControl.Populate(this.user);
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }
        #endregion

        #region Helpers
        void HideAll()
        {
            foreach (UIElement control in FillGrid.Children)
                control.Visibility = Visibility.Collapsed;
        }

        void ShowAccountsOverview()
        {
            HideAll();
            AccountsOverviewGrid.Visibility = Visibility.Visible;
            AccountsOverviewControl.Populate(this.user);
        }

        void LaunchWizard(WizardLauncher wizardLauncher)
        {
            HideAll();
            AllocateGrid.Visibility = Visibility.Visible;

            wizardLauncher.WizardReturn += new WizardReturnEventHandler(wizardLauncher_WizardReturn);
            this.NavigationFrame.Navigate(wizardLauncher);
        }
        #endregion
    }
}