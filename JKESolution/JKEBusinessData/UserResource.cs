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
    public class UserResource
    {
        private string firstName;
        [DataMember(Name = "first", Order = 1)]
        public string FirstName { get { return this.firstName; } set { this.firstName = value; } }

        private string lastName;
        [DataMember(Name = "last", Order = 2)]
        public string LastName { get { return this.lastName; } set { this.lastName = value; } }

        private string userName;
        [DataMember(Name = "userId", Order = 3)]
        public string UserName { get { return this.userName; } set { this.userName = value; } }
    }
}