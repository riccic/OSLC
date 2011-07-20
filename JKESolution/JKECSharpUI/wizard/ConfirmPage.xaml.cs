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
using System.Windows.Navigation;
using JKEBusinessData;
using JKEService;

namespace JKECSharpUI.wizard
{
    public partial class ConfirmPage : PageFunction<WizardResult>
    {
        public ConfirmPage(WizardData wizardData)
        {
            InitializeComponent();

            AccountResource account = wizardData.Account;
            try
            {
                IJKEService channel = ServiceManager.GetChannel();
                string today = DateTime.Now.ToShortDateString();
                TransactionResource previewTansaction = channel.GetTransactionPreview(account.AccountNumber,
                    wizardData.Organization.Name, today, wizardData.Percentage);
                wizardData.PreviewTransaction = previewTansaction;

                AccountResource previewAccount = new AccountResource();
                previewAccount.Balance = previewTansaction.PostBalance;
                previewAccount.Dividends = account.Dividends;
                previewAccount.DividendsETD = account.DividendsETD;
                previewAccount.Contributions = account.Contributions + previewTansaction.Amount;
                previewAccount.ContributionsETD = account.ContributionsETD + previewTansaction.Amount;
                wizardData.PreviewAccount = previewAccount;
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }

            // Bind wizard state to UI
            this.DataContext = wizardData;
        }

        #region Wizard
        void backButton_Click(object sender, RoutedEventArgs e)
        {
            // Go to previous wizard page
            this.NavigationService.GoBack();
        }

        void cancelButton_Click(object sender, RoutedEventArgs e)
        {
            // Cancel the wizard and don't return any data
            OnReturn(new ReturnEventArgs<WizardResult>(WizardResult.Canceled));
        }

        void nextButton_Click(object sender, RoutedEventArgs e)
        {
            // Go to next wizard page
            ReturnPage returnPage = new ReturnPage((WizardData)this.DataContext);
            returnPage.Return += new ReturnEventHandler<WizardResult>(wizardPage_Return);
            this.NavigationService.Navigate(returnPage);
        }

        public void wizardPage_Return(object sender, ReturnEventArgs<WizardResult> e)
        {
            // If returning, wizard was completed (finished or canceled),
            // so continue returning to calling page
            OnReturn(e);
        }
        #endregion
    }
}