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
