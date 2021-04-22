package br.com.hostel.e2e;

import java.io.File;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class ChromeConnection {

	public ChromeConnection() {
		
	}

	public WebDriver Connection() {
		File file = new File("./chromedriver.exe");
		System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());
		ChromeOptions options = new ChromeOptions();
		WebDriver driver = new ChromeDriver(options);

		return driver;
	}
}
