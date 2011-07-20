/*******************************************************************************
 * Licensed Materials - Property of IBM
 * (c) Copyright IBM Corporation 2010. All Rights Reserved. 
 * 
 * Note to U.S. Government Users Restricted Rights:  Use, 
 * duplication or disclosure restricted by GSA ADP Schedule 
 * Contract with IBM Corp.
 *******************************************************************************/

using System;
using System.Collections.ObjectModel;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Navigation;
using JKEBusinessData;
using JKEService;

namespace JKECSharpUI.wizard
{
    public partial class AccountPage : PageFunction<WizardResult>
    {
        public AccountPage(WizardData wizardData)
        {
            InitializeComponent();

            // Bind wizard state to UI
            this.DataContext = wizardData;
            try
            {
                IJKEService channel = ServiceManager.GetChannel();
                OrganizationResource[] orgs = channel.GetOrganizations();
                this.OrganizationsComboBox.ItemsSource = orgs;
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        #region Wizard
        void nextButton_Click(object sender, RoutedEventArgs e)
        {
            // Go to next wizard page
            ConfirmPage confirmPage = new ConfirmPage((WizardData)this.DataContext);
            confirmPage.Return += new ReturnEventHandler<WizardResult>(wizardPage_Return);
            this.NavigationService.Navigate(confirmPage);
        }

        void cancelButton_Click(object sender, RoutedEventArgs e)
        {
            ClearInvalid();

            // Cancel the wizard and don't return any data
            OnReturn(new ReturnEventArgs<WizardResult>(WizardResult.Canceled));
        }

        public void wizardPage_Return(object sender, ReturnEventArgs<WizardResult> e)
        {
            // If returning, wizard was completed (finished or canceled),
            // so continue returning to calling page
            OnReturn(e);
        }
        #endregion

        #region Validation
        private ObservableCollection<ValidationError> errors = new ObservableCollection<ValidationError>();
        private void PageFunction_Error(object sender, ValidationErrorEventArgs e)
        {
            if (e.Action == ValidationErrorEventAction.Added && !errors.Contains(e.Error))
                errors.Add(e.Error);
            else
                errors.Remove(e.Error);

            nextButton.IsEnabled = (errors.Count == 0);
        }

        private void PageFunction_Loaded(object sender, RoutedEventArgs e)
        {
            PercentageBox.GetBindingExpression(TextBox.TextProperty).UpdateSource();
            OrganizationsComboBox.GetBindingExpression(ComboBox.SelectedItemProperty).UpdateSource();
        }

        private void PageFunction_IsVisibleChanged(object sender, DependencyPropertyChangedEventArgs e)
        {
            if (e.NewValue.ToString() == Boolean.FalseString)
                ClearInvalid();
        }

        private void ClearInvalid()
        {
            Validation.ClearInvalid(PercentageBox.GetBindingExpression(TextBox.TextProperty));
            Validation.ClearInvalid(OrganizationsComboBox.GetBindingExpression(ComboBox.SelectedItemProperty));
        }
        #endregion
    }
}