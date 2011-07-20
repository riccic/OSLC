/*******************************************************************************
 * Licensed Materials - Property of IBM
 * (c) Copyright IBM Corporation 2010. All Rights Reserved. 
 * 
 * Note to U.S. Government Users Restricted Rights:  Use, 
 * duplication or disclosure restricted by GSA ADP Schedule 
 * Contract with IBM Corp.
 *******************************************************************************/

namespace JKECSharpUI.wizard
{
    public class WizardReturnEventArgs
    {
        WizardResult result;
        object data;

        public WizardReturnEventArgs(WizardResult result, object data)
        {
            this.result = result;
            this.data = data;
        }

        public WizardResult Result
        {
            get { return this.result; }
        }

        public object Data
        {
            get { return this.data; }
        }
    }
}
