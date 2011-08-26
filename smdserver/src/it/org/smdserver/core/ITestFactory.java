package org.smdserver.core;

import org.smdserver.core.ISmdCoreFactory;
import org.smdserver.core.ITestConfig;

public interface ITestFactory extends ISmdCoreFactory
{
	public ITestConfig createTestConfig();
}
