/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team YOCO (You Only Compile Once)
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.cal;

import java.lang.reflect.Field;

public class ReflectUtils
{
	@SuppressWarnings("rawtypes")
	public static Field getField(Object obj, String name) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException
	{
		Class c = obj.getClass();
		while (c != Object.class)
		{
			try
			{
				Field f = c.getDeclaredField(name);
				f.setAccessible(true);
				return f;
			}
			catch (NoSuchFieldException e)
			{
				c = c.getSuperclass();
			}
		}
		throw new NoSuchFieldException(name);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getFieldValue(Object obj, String name) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException
	{
		return (T)getField(obj, name).get(obj);
	}
}
