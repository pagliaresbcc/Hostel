package br.com.hostel.suite;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SelectPackages({ "br.com.hostel.tests", 
					"br.com.hostel.tests.integration", 
					"br.com.hostel.tests.integration.reservation", 
					"br.com.hostel.tests.integration.reservation.delete", 
					"br.com.hostel.tests.unit.guest", 
					"br.com.hostel.tests.unit.room"
				})
public class IntegrationAndUnityTestsSuite {}