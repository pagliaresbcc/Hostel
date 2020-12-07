package br.com.hostel.e2e;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import br.com.hostel.model.Address;
import br.com.hostel.model.Guest;
import br.com.hostel.model.Role;

@RunWith(JUnitPlatform.class)
public class RegisterTest {

	ChromeConnection chromeConnection = new ChromeConnection();
	WebDriver driver = chromeConnection.Connection();
	Guest newGuest, existentGuest;

	@BeforeEach
	public void init() {
		Address newAddress = new Address("Av Paulista", "01311-000", "São Paulo", "SP", "Brasil");
		newGuest = new Guest("Dr.", "Sócrates", "Oliveira", LocalDate.of(1954, 2, 19), newAddress,
				"socratesccp@gmail.com", "123456", Role.ROLE_ADMIN);
		Address existentAddress = new Address("Rua 2", "13900-000", "Amparo", "SP", "Brasil");
		existentGuest = new Guest("Mrs.", "Admin", "Hostel", LocalDate.of(2000, 9, 1), existentAddress,
				"admin@email.com", "123456", Role.ROLE_ADMIN);
		
		driver.get("http://localhost:3000/");
		driver.manage().window().maximize();
		driver.findElement(By.linkText("Não tenho cadastro")).click();

	}

	@Test
	public void registerANonExistentGuest() throws InterruptedException {

		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/input[1]")).sendKeys(newGuest.getTitle());
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/div[1]/input[1]"))
				.sendKeys(newGuest.getName());
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/div[1]/input[2]	"))
				.sendKeys(newGuest.getLastName());
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"birthday\"]"))
				.sendKeys(convertLocalDateIntoBrazilianString(newGuest.getBirthday()));
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/div[2]/input[1]"))
				.sendKeys(newGuest.getAddress().getAddressName());
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/div[2]/div/input[1]"))
				.sendKeys(newGuest.getAddress().getCity());
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/div[2]/div/input[2]"))
				.sendKeys(newGuest.getAddress().getState());
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/div[2]/input[2]"))
				.sendKeys(newGuest.getAddress().getCountry());
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/div[2]/input[3]"))
				.sendKeys(newGuest.getAddress().getZipCode());
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/input[3]")).sendKeys(newGuest.getEmail());
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/input[4]")).sendKeys(newGuest.getPassword());
		Thread.sleep(3000);

		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/button")).click();
		Thread.sleep(3000);
		assertEquals(driver.switchTo().alert().getText(), "Cadastrado");

		driver.switchTo().alert().accept();

		Thread.sleep(3000);
		driver.close();
	}

	@Test
	public void registerAnExistentGuest() throws InterruptedException {

		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/input[1]"))
				.sendKeys(existentGuest.getTitle());
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/div[1]/input[1]"))
				.sendKeys(existentGuest.getName());
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/div[1]/input[2]"))
				.sendKeys(existentGuest.getLastName());
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"birthday\"]"))
				.sendKeys(convertLocalDateIntoBrazilianString(existentGuest.getBirthday()));
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/div[2]/input[1]"))
				.sendKeys(existentGuest.getAddress().getAddressName());
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/div[2]/div/input[1]"))
				.sendKeys(existentGuest.getAddress().getCity());
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/div[2]/div/input[2]"))
				.sendKeys(existentGuest.getAddress().getState());
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/div[2]/input[2]"))
				.sendKeys(existentGuest.getAddress().getCountry());
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/div[2]/input[3]"))
				.sendKeys(existentGuest.getAddress().getZipCode());
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/input[3]"))
				.sendKeys(existentGuest.getEmail());
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/input[4]"))
				.sendKeys(existentGuest.getPassword());
		Thread.sleep(3000);

		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/button")).click();

		Thread.sleep(3000);

		assertEquals(driver.switchTo().alert().getText(), "Erro no cadastro, tente novamente");
		driver.switchTo().alert().accept();
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
