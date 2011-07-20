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
    public partial class ReturnPage : PageFunction<WizardResult>
    {
        public ReturnPage(WizardData wizardData)
        {
            InitializeComponent();

            try
            {
                IJKEService channel = ServiceManager.GetChannel();
                string today = DateTime.Now.ToShortDateString();
                TransactionResource transaction = channel.PostTransaction(wizardData.Account.AccountNumber,
                    wizardData.Organization.Name, today, wizardData.Percentage);
                wizardData.Transaction = transaction;
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }

            // Bind wizard state to UI
            this.DataContext = wizardData;
        }

        void finishButton_Click(object sender, RoutedEventArgs e)
        {
            // Finish the wizard and return bound data to calling page
            OnReturn(new ReturnEventArgs<WizardResult>(WizardResult.Finished));
        }
    }
}