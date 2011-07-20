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
    public class OrganizationResource
    {
        private string name;
        [DataMember(Name = "name", Order = 1)]
        public string Name { get { return this.name; } set { this.name = value; } }

        private string webPage;
        [DataMember(Name = "website", Order = 2)]
        public string WebPage { get { return this.webPage; } set { this.webPage = value; } }
    }
}