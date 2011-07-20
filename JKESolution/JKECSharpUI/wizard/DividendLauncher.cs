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
    public class DividendLauncher : WizardLauncher
    {
        public DividendLauncher(UserResource user)
        {
            wizardData = new WizardData(user);
        }

        protected override PageFunction<WizardResult> NewStartPage
        {
            get { return new DividendPage(wizardData); }
        }
    }
}
