package org.openmrs.module.pmtct.service;
import org.junit.Assert;
import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.module.pmtct.db.PmtctService;
import org.openmrs.test.BaseModuleContextSensitiveTest;

public class PmtctServiceTest extends BaseModuleContextSensitiveTest{

	@Test
	public void testIsServiceAccessible() {
		Assert.assertNotNull(Context.getService(PmtctService.class));
	}
}
