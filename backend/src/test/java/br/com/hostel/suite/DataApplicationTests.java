package br.com.hostel.suite;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import br.com.hostel.tests.get.CustomerGetTests;
import br.com.hostel.tests.get.ReservationGetTests;
import br.com.hostel.tests.get.RoomGetTests;
import br.com.hostel.tests.post.CustomerPostAndDeleteTests;
import br.com.hostel.tests.post.ReservationPostAndDeleteTests;
import br.com.hostel.tests.post.RoomPostAndDeleteTests;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ReservationPostAndDeleteTests.class,
        ReservationGetTests.class,
        CustomerPostAndDeleteTests.class,
        CustomerGetTests.class,
        RoomPostAndDeleteTests.class,
        RoomGetTests.class
})
public class DataApplicationTests {
	
    @Test
    public void contextLoads() {}
    
}