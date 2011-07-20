/*******************************************************************************
 * Licensed Materials - Property of IBM
 * (c) Copyright IBM Corporation 2010. All Rights Reserved. 
 * 
 * Note to U.S. Government Users Restricted Rights:  Use, 
 * duplication or disclosure restricted by GSA ADP Schedule 
 * Contract with IBM Corp.
 *******************************************************************************/

using System;
using JKEFactory = System.ServiceModel.Web.WebChannelFactory<JKEService.IJKEService>;

namespace JKEService
{
    public class ServiceManager
    {
        static JKEFactory factory;
        static IJKEService channel;

        public static IJKEService GetChannel()
        {
            if (channel == null)
            {
                factory = CreateFactory();
                channel = factory.CreateChannel();
            }
            return channel;
        }

        public static JKEFactory CreateFactory()
        {
            return new JKEFactory(new Uri("http://localhost:8080/"));
        }

        public static void CloseFactory()
        {
            if (factory != null)
                factory.Close();
        }
    }
}