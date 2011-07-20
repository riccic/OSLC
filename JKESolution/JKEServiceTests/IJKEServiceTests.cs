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
using JKEBusinessData;
using JKEService;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using JKEFactory = System.ServiceModel.Web.WebChannelFactory<JKEService.IJKEService>;

namespace JKEServiceTests
{


    /// <summary>
    ///This is a test class for IJKEServiceTest and is intended
    ///to contain all IJKEServiceTest Unit Tests
    ///</summary>
    [TestClass()]
    public class IJKEServiceTests
    {
        private TestContext testContextInstance;

        /// <summary>
        ///Gets or sets the test context which provides
        ///information about and functionality for the current test run.
        ///</summary>
        public TestContext TestContext
        {
            get
            {
                return testContextInstance;
            }
            set
            {
                testContextInstance = value;
            }
        }

        #region Additional test attributes
        // 
        //You can use the following additional attributes as you write your tests:
        //
        //Use ClassInitialize to run code before running the first test in the class
        //[ClassInitialize()]
        //public static void MyClassInitialize(TestContext testContext)
        //{
        //}
        //
        //Use ClassCleanup to run code after all tests in a class have run
        //[ClassCleanup()]
        //public static void MyClassCleanup()
        //{
        //}
        //
        //Use TestInitialize to run code before running each test
        //[TestInitialize()]
        //public void MyTestInitialize()
        //{
        //}
        //
        //Use TestCleanup to run code after each test has run
        //[TestCleanup()]
        //public void MyTestCleanup()
        //{
        //}
        //
        #endregion

        /// <summary>
        ///A test for GetUserResource
        ///</summary>
        [TestMethod()]
        public void GetUserResourceTest()
        {
            using (JKEFactory factory = ServiceManager.CreateFactory())
            {
                IJKEService target = factory.CreateChannel();
                string userId = "jbrown";
                UserResource user = target.GetUserResource(userId);
                Assert.IsNotNull(user);
                Assert.AreEqual(userId, user.UserName);
            }
        }

        /// <summary>
        ///A test for GetUserAccounts
        ///</summary>
        [TestMethod()]
        public void GetUserAccountsTest()
        {
            using (JKEFactory factory = ServiceManager.CreateFactory())
            {
                IJKEService target = factory.CreateChannel();
                string userId = "jbrown";
                AccountResource[] accounts = target.GetUserAccounts(userId);
                Assert.IsNotNull(accounts);
                foreach (AccountResource account in accounts)
                    Assert.AreEqual(userId, account.UserName);
            }
        }

        /// <summary>
        ///A test for GetOrganizations
        ///</summary>
        [TestMethod()]
        public void GetOrganizationsTest()
        {
            using (JKEFactory factory = ServiceManager.CreateFactory())
            {
                IJKEService target = factory.CreateChannel();
                OrganizationResource[] organizations = target.GetOrganizations();
                Assert.IsNotNull(organizations);
                Assert.AreEqual(4, organizations.Length);
            }
        }

        /// <summary>
        ///A test for GetTransactionsForAccount
        ///</summary>
        [TestMethod()]
        public void GetTransactionsForAccountTest()
        {
            using (JKEFactory factory = ServiceManager.CreateFactory())
            {
                IJKEService target = factory.CreateChannel();
                string userId = "jbrown";

                AccountResource[] accounts = target.GetUserAccounts(userId);
                Assert.IsNotNull(accounts);
                Assert.IsTrue(accounts.Length > 0);
                AccountResource account = accounts[0];

                TransactionResource[] transactions = target.GetTransactionsForAccount(userId, account.Type);
                Assert.IsNotNull(transactions);
                if (transactions.Length > 0)
                {
                    foreach (TransactionResource transaction in transactions)
                        Assert.AreEqual(account.AccountNumber, transaction.AccountNumber);
                    TransactionResource lastTransaction = transactions[transactions.Length - 1];
                    Assert.AreEqual(account.Balance, lastTransaction.PostBalance);
                }
            }
        }

        /// <summary>
        ///A test for GetTransactionPreview
        ///</summary>
        [TestMethod()]
        public void GetTransactionPreviewTest()
        {
            using (JKEFactory factory = ServiceManager.CreateFactory())
            {
                IJKEService target = factory.CreateChannel();
                string userId = "jbrown";

                AccountResource[] accounts = target.GetUserAccounts(userId);
                Assert.IsNotNull(accounts);
                Assert.IsTrue(accounts.Length > 0);
                AccountResource account = accounts[0];

                OrganizationResource[] organizations = target.GetOrganizations();
                Assert.IsNotNull(organizations);
                Assert.IsTrue(organizations.Length > 0);
                OrganizationResource organization = organizations[0];

                TransactionResource[] transactions = target.GetTransactionsForAccount(userId, account.Type);
                Assert.IsNotNull(transactions);
                int before = transactions.Length;

                double percentage = 2;
                double amount = account.Dividends * percentage / 100;

                string today = DateTime.Now.ToShortDateString();

                TransactionResource preview = target.GetTransactionPreview(account.AccountNumber, organization.Name, today, percentage);
                Assert.IsNotNull(preview);

                Assert.AreEqual(amount, preview.Amount);
                Assert.AreEqual(account.AccountNumber, preview.AccountNumber);

                DateTime newDate = Convert.ToDateTime(preview.Date, CultureInfo.InvariantCulture);
                Assert.AreEqual(today, newDate.ToShortDateString());

                Assert.AreEqual(organization.Name, preview.Source);
                Assert.AreEqual(account.Balance - amount, preview.PostBalance);
            }
        }

        /// <summary>
        ///A test for PostTransaction
        ///</summary>
        [TestMethod()]
        public void PostTransactionTest()
        {
            using (JKEFactory factory = ServiceManager.CreateFactory())
            {
                IJKEService target = factory.CreateChannel();
                string userId = "jbrown";

                AccountResource[] accounts = target.GetUserAccounts(userId);
                Assert.IsNotNull(accounts);
                Assert.IsTrue(accounts.Length > 0);
                AccountResource account = accounts[0];

                OrganizationResource[] organizations = target.GetOrganizations();
                Assert.IsNotNull(organizations);
                Assert.IsTrue(organizations.Length > 0);
                OrganizationResource organization = organizations[0];

                TransactionResource[] transactions = target.GetTransactionsForAccount(userId, account.Type);
                Assert.IsNotNull(transactions);
                int before = transactions.Length;

                double percentage = 2;
                double amount = account.Dividends * percentage / 100;

                string today = DateTime.Now.ToShortDateString();

                TransactionResource newTransaction = target.PostTransaction(account.AccountNumber, organization.Name, today, percentage);
                Assert.IsNotNull(newTransaction);

                Assert.AreEqual(amount, newTransaction.Amount);
                Assert.AreEqual(account.AccountNumber, newTransaction.AccountNumber);

                DateTime newDate = Convert.ToDateTime(newTransaction.Date, CultureInfo.InvariantCulture);
                Assert.AreEqual(today, newDate.ToShortDateString());

                Assert.AreEqual(organization.Name, newTransaction.Source);
                Assert.AreEqual(account.Balance - amount, newTransaction.PostBalance);

                transactions = target.GetTransactionsForAccount(userId, account.Type);
                Assert.IsNotNull(transactions);
                int after = transactions.Length;

                Assert.AreEqual(before + 1, after);
            }
        }
    }
}
