/*******************************************************************************
 * Licensed Materials - Property of IBM
 * (c) Copyright IBM Corporation 2010. All Rights Reserved. 
 * 
 * Note to U.S. Government Users Restricted Rights:  Use, 
 * duplication or disclosure restricted by GSA ADP Schedule 
 * Contract with IBM Corp.
 *******************************************************************************/

using System.Windows.Navigation;
using JKEBusinessData;

namespace JKECSharpUI.wizard
{
    public abstract class WizardLauncher : PageFunction<WizardResult>
    {
        protected WizardData wizardData;
        public event WizardReturnEventHandler WizardReturn;

        protected override void Start()
        {
            base.Start();

            // So we remember the WizardCompleted event registration
            this.KeepAlive = true;

            // Launch the wizard
            PageFunction<WizardResult> wizardPage = this.NewStartPage;
            wizardPage.Return += new ReturnEventHandler<WizardResult>(wizardPage_Return);
            this.NavigationService.Navigate(wizardPage);
        }

        public void wizardPage_Return(object sender, ReturnEventArgs<WizardResult> e)
        {
            // Notify client that wizard has completed
            // NOTE: We need this custom event because the Return event cannot be
            // registered by window code - if client registers an event handler with
            // the WizardLauncher's Return event, the event is not raised.
            if (this.WizardReturn != null)
                this.WizardReturn(this, new WizardReturnEventArgs(e.Result, this.wizardData));
            OnReturn(null);
        }

        protected abstract PageFunction<WizardResult> NewStartPage { get; }
    }
}
