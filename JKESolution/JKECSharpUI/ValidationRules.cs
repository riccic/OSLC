/*******************************************************************************
 * Licensed Materials - Property of IBM
 * (c) Copyright IBM Corporation 2010. All Rights Reserved. 
 * 
 * Note to U.S. Government Users Restricted Rights:  Use, 
 * duplication or disclosure restricted by GSA ADP Schedule 
 * Contract with IBM Corp.
 *******************************************************************************/

using System;
using System.Globalization;
using System.Windows.Controls;

namespace JKECSharpUI
{
    public class PercentageRule : ValidationRule
    {
        public override ValidationResult Validate(object value, CultureInfo cultureInfo)
        {
            string strDouble = value.ToString();
            double objDouble = -1;
            if (!Double.TryParse(strDouble, out objDouble))
                return new ValidationResult(false, "Enter a number");
            if (objDouble <= 0)
                return new ValidationResult(false, "Enter a positive percentage");
            return new ValidationResult(true, null);
        }
    }

    public class OrganizationRule : ResourceRule
    {
        public override string ErrorText
        {
            get { return "Select an organization"; }
        }
    }

    public class AccountRule : ResourceRule
    {
        public override string ErrorText
        {
            get { return "Select an account type"; }
        }
    }

    public abstract class ResourceRule : ValidationRule
    {
        public abstract string ErrorText { get; }

        public override ValidationResult Validate(object value, CultureInfo cultureInfo)
        {
            if (value == null)
                return new ValidationResult(false, this.ErrorText);
            return new ValidationResult(true, null);
        }
    }
}
