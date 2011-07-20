/*******************************************************************************
 * Licensed Materials - Property of IBM
 * (c) Copyright IBM Corporation 2005, 2010. All Rights Reserved. 
 * 
 * Note to U.S. Government Users Restricted Rights:  Use, 
 * duplication or disclosure restricted by GSA ADP Schedule 
 * Contract with IBM Corp.
 *******************************************************************************/

package com.jke.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;

import com.jke.beans.AccountTypeBean;

public class Utils {
	static Font WELCOME_FONT;
	static Font BOLD_FONT;
	public static Color WHITE;
	public static Color GRAY;

	static void init(Display display) {
		Font systemFont= display.getSystemFont();
		WELCOME_FONT= systemFont;
		FontData[] fontData= systemFont.getFontData();

		if (fontData.length > 0) {
			WELCOME_FONT= new Font(display, fontData[0].getName(), fontData[0].getHeight() + 2, SWT.BOLD);
			BOLD_FONT= new Font(display, fontData[0].getName(), fontData[0].getHeight(), SWT.BOLD);
		}

		WHITE= Display.getDefault().getSystemColor(SWT.COLOR_WHITE);
		GRAY= new Color(display, 100, 100, 100);
	}

	static String getAccountTypeName(AccountTypeBean bean) {
		switch (bean) {
			case Checking:
				return "Checking Account";

			case IRA:
				return "IRA Retirement Account";

			case Money_Market:
				return "Money Market Savings Account";

			case Savings:
				return "Savings Account";

			default:
				return "Unknown Account";
		}
	}
}