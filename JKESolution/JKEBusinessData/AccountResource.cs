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
    public class AccountResource
    {
        private int accountNumber;
        [DataMember(Name = "accountNumber", Order = 1)]
        public int AccountNumber { get { return this.accountNumber; } set { this.accountNumber = value; } }

        private string userName;
        [DataMember(Name = "userName", Order = 2)]
        public string UserName { get { return this.userName; } set { this.userName = value; } }

        private string type;
        [DataMember(Name = "type", Order = 3)]
        public string Type { get { return this.type; } set { this.type = value; } }

        private string typeName;
        [DataMember(Name = "typeName", Order = 4)]
        public string TypeName { get { return this.typeName; } set { this.typeName = value; } }

        private double balance;
        [DataMember(Name = "balance", Order = 5)]
        public double Balance { get { return this.balance; } set { this.balance = value; } }

        private double dividends;
        [DataMember(Name = "dividends", Order = 6)]
        public double Dividends { get { return this.dividends; } set { this.dividends = value; } }

        private double dividendsETD;
        [DataMember(Name = "dividendsETD", Order = 7)]
        public double DividendsETD { get { return this.dividendsETD; } set { this.dividendsETD = value; } }

        private double contributions;
        [DataMember(Name = "contributions", Order = 8)]
        public double Contributions { get { return this.contributions; } set { this.contributions = value; } }

        private double contributionsETD;
        [DataMember(Name = "contributionsETD", Order = 9)]
        public double ContributionsETD { get { return this.contributionsETD; } set { this.contributionsETD = value; } }
    }
}