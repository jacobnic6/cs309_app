package com.coms309.nutrifit.service;

/**
 * The type Service handler.
 */
public abstract class ServiceHandler {
	/**
	 * The Success.
	 */
	protected String success = "{\"message\":\"success\"}";

	/**
	 * The Failure.
	 */
	protected String failure = "{\"message\":\"failure\"}";

	/**
	 * Is numeric boolean.
	 *
	 * @param str the str
	 *
	 * @return the boolean
	 */
	public static boolean isNumeric(String str) {
		if (str == null)
		{
			return false;
		}
		for (char c : str.toCharArray())
		{
			if (!Character.isDigit(c))
			{
				return false;
			}
		}
		return true;
	}

}
