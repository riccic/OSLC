/*******************************************************************************
 * Licensed Materials - Property of IBM
 * (c) Copyright IBM Corporation 2010. All Rights Reserved. 
 * 
 * Note to U.S. Government Users Restricted Rights:  Use, 
 * duplication or disclosure restricted by GSA ADP Schedule 
 * Contract with IBM Corp.
 *******************************************************************************/

using JKEBusinessData;

namespace JKECSharpUI.wizard
{
    /// <summary>
    /// Data that is collected by the wizard
    /// </summary>
    public class WizardData
    {
        private AccountResource account;
        private UserResource user;
        private OrganizationResource organization;
        private double percentage;
        private TransactionResource previewTransaction;
        private AccountResource previewAccount;
        private TransactionResource transaction;

        public WizardData(AccountResource account)
        {
            this.account = account;
        }

        public WizardData(UserResource user)
        {
            this.user = user;
        }

        public AccountResource Account
        {
            get { return this.account; }
            set { this.account = value; }
        }

        public UserResource User
        {
            get { return this.user; }
        }

        public OrganizationResource Organization
        {
            get { return this.organization; }
            set { this.organization = value; }
        }

        public double Percentage
        {
            get { return this.percentage; }
            set { this.percentage = value; }
        }

        public TransactionResource PreviewTransaction
        {
            get { return this.previewTransaction; }
            set { this.previewTransaction = value; }
        }
        
        public AccountResource PreviewAccount
        {
            get { return this.previewAccount; }
            set { this.previewAccount = value; }
        }

        public TransactionResource Transaction
        {
            get { return this.transaction; }
            set { this.transaction = value; }
        }
    }
}
