/*******************************************************************************
 * Licensed Materials - Property of IBM
 * (c) Copyright IBM Corporation 2010. All Rights Reserved. 
 * 
 * Note to U.S. Government Users Restricted Rights:  Use, 
 * duplication or disclosure restricted by GSA ADP Schedule 
 * Contract with IBM Corp.
 *******************************************************************************/

using System.Runtime.Serialization;

namespace JKEBusinessData
{
    [DataContract]
    public class TransactionResource
    {
        private int accountNumber;
        [DataMember(Name = "accountNumber", Order = 1)]
        public int AccountNumber { get { return this.accountNumber; } set { this.accountNumber = value; } }

        private double amount;
        [DataMember(Name = "amount", Order = 2)]
        public double Amount { get { return this.amount; } set { this.amount = value; } }

        private string date;
        [DataMember(Name = "date", Order = 3)]
        public string Date { get { return this.date; } set { this.date = value; } }

        private double postBalance;
        [DataMember(Name = "balance", Order = 4)]
        public double PostBalance { get { return this.postBalance; } set { this.postBalance = value; } }

        private string source;
        [DataMember(Name = "source", Order = 5)]
        public string Source { get { return this.source; } set { this.source = value; } }

        private int transactionID;
        [DataMember(Name = "id", Order = 6)]
        public int TransactionID { get { return this.transactionID; } set { this.transactionID = value; } }

        private string transactionType;
        [DataMember(Name = "type", Order = 7)]
        public string TransactionType { get { return this.transactionType; } set { this.transactionType = value; } }
    }
}