package br.com.hostel.e2e;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import br.com.hostel.model.Guest;

@RunWith(JUnitPlatform.class)
public class LoginTest {

	ChromeConnection chromeConnection = new ChromeConnection();
	WebDriver driver = chromeConnection.Connection();
	Guest existentGuest, nonExistentGuest;

	@BeforeEach
	public void init() {
		existentGuest = new Guest("maria@email.com", "123456");
		nonExistentGuest = new Guest("random@gmail.com", "123456");
		
		driver.get("http://localhost:3000/");
		driver.manage().window().maximize();
	}

	@Test
	public void loginANonExistentGuest() throws InterruptedException {

		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/section/form/input[1]"))
				.sendKeys(nonExistentGuest.getEmail());
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/section/form/input[2]"))
				.sendKeys(nonExistentGuest.getPassword());
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/section/form/button")).click();
		Thread.sleep(3000);

		assertEquals("Falha no Login, tente novamente", driver.switchTo().alert().getText());
		driver.switchTo().alert().accept();

		Thread.sleep(3000);
		driver.close();
	}

	@Test
	public void registerAnExistentGuest() throws InterruptedException {

		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/section/form/input[1]"))
				.sendKeys(existentGuest.getEmail());
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/section/form/input[2]"))
				.sendKeys(existentGuest.getPassword());
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/section/form/button")).click();
		Thread.sleep(3000);

		assertEquals("Olá Administrador, bem-vindo ao Hostel!", driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/header/span")).getText());
		Thread.sleep(3000);

		driver.close();
	}

	public String convertLocalDateIntoBrazilianString(LocalDate birthday) {
		return (birthday.getDayOfMonth() < 10 ? (0 + "" + birthday.getDayOfMonth()) : birthday.getDayOfMonth()) + ""
				+ (birthday.getMonth().getValue() < 10 ? (0 + "" + birthday.getMonth().getValue())
						: birthday.getMonth().getValue())
				+ "" + birthday.getYear();
	}

}
