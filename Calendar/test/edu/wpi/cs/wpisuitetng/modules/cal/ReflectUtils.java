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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


@SuppressWarnings({ "rawtypes", "unchecked" })
public class ReflectUtils
{
	/**
	 * Easy way to get accessible private fields from inherited children
	 * @param obj The object
	 * @param name name of field to pull
	 * @return accessible field
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws NoSuchFieldException
	 */
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
	
	/**
	 * Easy way to get private fields values from inherited children
	 * @param obj The object
	 * @param name name of field to pull
	 * @return accessible field
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws NoSuchFieldException
	 */
	public static <T> T getFieldValue(Object obj, String name) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException
	{
		return (T)getField(obj, name).get(obj);
	}
	
	/**
	 * Easy way to get accessible methods from inherited children
	 * @param obj The object
	 * @param name name of method to pull from the project
	 * @param types argument types
	 * @return accessible method
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 */
	public static Method getMethod(Object obj, String name, Class... types)  throws IllegalArgumentException, IllegalAccessException, NoSuchMethodException
	{
		Class c = obj.getClass();
		while (c != Object.class)
		{
			try
			{
				Method m = c.getDeclaredMethod(name, types);
				m.setAccessible(true);
				return m;
			}
			catch (NoSuchMethodException e)
			{
				c = c.getSuperclass();
			}
		}
		throw new NoSuchMethodException(name);	
	}

	/**
	 * Easy way to call accessible methods from inherited children
	 * @param obj The object
	 * @param name name of method to pull from the project
	 * @param types arguments for the method
	 * @return return value of the method
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 */
	public static <T> T callMethod(Object obj, String name, Object... args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException
	{
		Class[] argTypes = new Class[args.length];
		int i = 0;
		for (Object argo : args)
		{
			argTypes[i++] = argo.getClass();
		}
		return (T)getMethod(obj, name, argTypes).invoke(obj, args);
	}
}
