package br.com.hostel.e2e.admin;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import br.com.hostel.e2e.ChromeConnection;
import br.com.hostel.model.Guest;

@RunWith(JUnitPlatform.class)
@TestMethodOrder(OrderAnnotation.class)
public class Functionalities {

	ChromeConnection chromeConnection = new ChromeConnection();
	WebDriver driver = chromeConnection.Connection();

	Guest newGuest, existentGuest;

	@BeforeEach
	public void init() throws InterruptedException {
		// make login
		driver.get("http://localhost:3000/");
		driver.manage().window().maximize();
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/section/form/input[1]")).sendKeys("maria@email.com");
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/section/form/input[2]")).sendKeys("123456");
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/section/form/button")).click();
		Thread.sleep(3000);
	}

	@Test
	public void checkFunctionalities() throws InterruptedException {
		assertEquals(driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/a[1]")).getText(), "Gerenciar reservas");
		assertEquals(driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/a[2]")).getText(), "Gerenciar quartos");
		assertEquals(driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/a[3]")).getText(), "Gerenciar hóspedes");

		Thread.sleep(3000);
		driver.close();
	}
}
