package br.com.hostel.suite;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SelectPackages({ "br.com.hostel.e2e", 
					"br.com.hostel.e2e.admin", 
					"br.com.hostel.e2e.user"
				})
public class E2eTestsSuite {}