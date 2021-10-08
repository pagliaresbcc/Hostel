package br.com.hostel.suite;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.suite.api.SelectPackages;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SelectPackages("br.com.hostel.tests")
public class DataApplicationTests {}