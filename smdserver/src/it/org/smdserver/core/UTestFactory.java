package org.smdserver.core;

import java.util.ResourceBundle;
import org.smdserver.core.small.ISmdLogger;
import org.smdserver.db.ISmdDB;

class UTestFactory extends USmdCoreFactory implements ITestFactory
{
	private ResourceBundle rb;
	
	public UTestFactory(ResourceBundle rb, ResourceBundle serverRB,
			            String localeRBFile, ISmdLogger logger, ISmdDB db, 
			            String basePath)
	{
		super(rb, serverRB, localeRBFile, logger, db, basePath);
		this.rb = rb;
	}
	
	public ITestConfig createTestConfig()
	{
		return new UTestConfig(rb);
	}
}
