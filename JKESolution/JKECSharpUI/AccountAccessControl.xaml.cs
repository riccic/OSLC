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

namespace JKECSharpUI
{
    /// <summary>
    /// Interaction logic for AccountAccessControl.xaml
    /// </summary>
    public partial class AccountAccessControl : UserControl
    {
        public event EventHandler<LoginArgs> LogIn;
        public AccountAccessControl()
        {
            InitializeComponent();
        }

        public void Clear()
        {
            this.UserTextBox.Clear();
            this.PasswordBox.Clear();
        }

        private void Login_Click(object sender, RoutedEventArgs e)
        {
            string username = UserTextBox.Text;
            if (LogIn != null)
                LogIn(this, new LoginArgs(username));
        }
    }

    public class LoginArgs : EventArgs
    {
        private string username;
        public string Username { get { return username; } }

        public LoginArgs(string username)
        {
            this.username = username;
        }
    }
}