/*******************************************************************************
 * Licensed Materials - Property of IBM
 * (c) Copyright IBM Corporation 2010. All Rights Reserved. 
 * 
 * Note to U.S. Government Users Restricted Rights:  Use, 
 * duplication or disclosure restricted by GSA ADP Schedule 
 * Contract with IBM Corp.
 *******************************************************************************/

using System.ServiceModel;
using System.ServiceModel.Web;
using JKEBusinessData;

namespace JKEService
{
    [ServiceContract]
    public interface IJKEService
    {
        [OperationContract]
        [WebGet(UriTemplate = "/user/{userId}")]
        UserResource GetUserResource(string userId);

        [OperationContract]
        [WebGet(UriTemplate = "/user/{userId}/accounts")]
        AccountResource[] GetUserAccounts(string userId);

        [OperationContract]
        [WebGet(UriTemplate = "/organizations")]
        OrganizationResource[] GetOrganizations();

        [OperationContract]
        [WebGet(UriTemplate = "/transactions/{userId}/{accountName}")]
        TransactionResource[] GetTransactionsForAccount(string userId, string accountName);

        [OperationContract]
        [WebGet(UriTemplate = "/transactions/preview?account={accountNumber}&org={organization}&date={date}&percent={percentage}")]
        TransactionResource GetTransactionPreview(int accountNumber, string organization, string date, double percentage);

        [OperationContract]
        [WebInvoke(UriTemplate = "/transactions/create?account={accountNumber}&org={organization}&date={date}&percent={percentage}")]
        TransactionResource PostTransaction(int accountNumber, string organization, string date, double percentage);
    }
}