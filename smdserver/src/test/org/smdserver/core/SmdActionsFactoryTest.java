package org.smdserver.core;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.junit.Test;
import static org.junit.Assert.*;

public class SmdActionsFactoryTest
{
	/**
	 * Test of registerAction method, of class SmdActionsFactory.
	 */
	@Test
	public void testRegisterAndCreateAction() throws InstantiationException, IllegalAccessException
	{
		String name1 = "first";
		String name2 = "second";
		SmdActionsFactory instance = new SmdActionsFactory(null);
		instance.registerAction(name1, OtherAction.class);
		instance.registerAction(name2, AnotherAction.class);

		assertNotNull(instance.createAction(name1));
		assertNotNull(instance.createAction(name2));
		assertTrue(instance.createAction(name1) instanceof OtherAction);
		assertTrue(instance.createAction(name2) instanceof AnotherAction);

		instance.registerAction(name1, SomeAction.class);
		assertTrue(instance.createAction(name1) instanceof OtherAction);
		assertFalse(instance.createAction(name2) instanceof OtherAction);
	}

	/**
	 * Test of registerMap method, of class SmdActionsFactory.
	 */
	@Test
	public void testRegisterMap() {
		System.out.println("registerMap");
		Map<String, Class> map = new HashMap<String, Class>();
		map.put("first", OtherAction.class);
		map.put("second", AnotherAction.class);

		SmdActionsFactory instance = new SmdActionsFactory(null);
		instance.registerMap(map);

		assertTrue(instance.createAction("first") instanceof OtherAction);
		assertTrue(instance.createAction("second") instanceof AnotherAction);
	}

	static class SomeAction extends SmdAction
	{
		public SomeAction()
		{
			super();
		}
		public String doAction(HttpServletRequest request)
		{return null;}
	}

	static class AnotherAction extends SomeAction{}
	static class OtherAction extends SomeAction{}
}