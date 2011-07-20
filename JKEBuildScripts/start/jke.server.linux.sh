#!/bin/sh
#*******************************************************************************
# Licensed Materials - Property of IBM
# (c) Copyright IBM Corporation 2011. All Rights Reserved.
#
# Note to U.S. Government Users Restricted Rights:  
# Use, duplication or disclosure restricted by GSA ADP Schedule 
# Contract with IBM Corp. 
#*******************************************************************************
PRGPATH="`dirname "$0"`"
JAVA_EXE=java
exec "${JAVA_EXE}" -cp jke.jar:libs/derby.jar:libs/javax.servlet_2.5.0.v200910301333.jar:libs/org.mortbay.jetty.server_6.1.23.v201004211559.jar:libs/org.mortbay.jetty.util_6.1.23.v201004211559.jar:libs/com.ibm.team.json_1.0.0.I200908182153.jar com.jke.server.JKEServer
