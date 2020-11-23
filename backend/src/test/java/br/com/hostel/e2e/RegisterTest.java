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
import br.com.hostel.model.Customer;
import br.com.hostel.model.Role;

@RunWith(JUnitPlatform.class)
public class RegisterTest {

	ChromeConnection chromeConnection = new ChromeConnection();
	WebDriver driver = chromeConnection.Connection();
	Customer newCustomer, existentCustomer;

	@BeforeEach
	public void init() {
		Address newAddress = new Address("Av Paulista", "01311-000", "São Paulo", "SP", "Brasil");
		newCustomer = new Customer("Dr.", "Sócrates", "Oliveira", LocalDate.of(1954, 2, 19), newAddress,
				"socratesccp@gmail.com", "123456", Role.ROLE_ADMIN);
		Address existentAddress = new Address("Rua 2", "13900-000", "Amparo", "SP", "Brasil");
		existentCustomer = new Customer("Mrs.", "Admin", "Hostel", LocalDate.of(2000, 9, 1), existentAddress,
				"admin@email.com", "123456", Role.ROLE_ADMIN);
		
		driver.get("http://localhost:3000/");
		driver.manage().window().maximize();
		driver.findElement(By.linkText("Não tenho cadastro")).click();

	}

	@Test
	public void RegisterANonExistentcustomer() throws InterruptedException {

		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/input[1]")).sendKeys(newCustomer.getTitle());
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/div[1]/input[1]"))
				.sendKeys(newCustomer.getName());
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/div[1]/input[2]	"))
				.sendKeys(newCustomer.getLastName());
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"birthday\"]"))
				.sendKeys(ConvertLocalDateIntoBrazilianString(newCustomer.getBirthday()));
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/div[2]/input[1]"))
				.sendKeys(newCustomer.getAddress().getAddressName());
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/div[2]/div/input[1]"))
				.sendKeys(newCustomer.getAddress().getCity());
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/div[2]/div/input[2]"))
				.sendKeys(newCustomer.getAddress().getState());
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/div[2]/input[2]"))
				.sendKeys(newCustomer.getAddress().getCountry());
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/div[2]/input[3]"))
				.sendKeys(newCustomer.getAddress().getZipCode());
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/input[3]")).sendKeys(newCustomer.getEmail());
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/input[4]")).sendKeys(newCustomer.getPassword());
		Thread.sleep(3000);

		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/button")).click();
		Thread.sleep(3000);
		assertEquals(driver.switchTo().alert().getText(), "Cadastrado");

		driver.switchTo().alert().accept();

		Thread.sleep(3000);
		driver.close();
	}

	@Test
	public void RegisterAExistentCustomer() throws InterruptedException {

		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/input[1]"))
				.sendKeys(existentCustomer.getTitle());
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/div[1]/input[1]"))
				.sendKeys(existentCustomer.getName());
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/div[1]/input[2]"))
				.sendKeys(existentCustomer.getLastName());
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"birthday\"]"))
				.sendKeys(ConvertLocalDateIntoBrazilianString(existentCustomer.getBirthday()));
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/div[2]/input[1]"))
				.sendKeys(existentCustomer.getAddress().getAddressName());
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/div[2]/div/input[1]"))
				.sendKeys(existentCustomer.getAddress().getCity());
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/div[2]/div/input[2]"))
				.sendKeys(existentCustomer.getAddress().getState());
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/div[2]/input[2]"))
				.sendKeys(existentCustomer.getAddress().getCountry());
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/div[2]/input[3]"))
				.sendKeys(existentCustomer.getAddress().getZipCode());
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/input[3]"))
				.sendKeys(existentCustomer.getEmail());
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/input[4]"))
				.sendKeys(existentCustomer.getPassword());
		Thread.sleep(3000);

		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/button")).click();

		Thread.sleep(3000);

		assertEquals(driver.switchTo().alert().getText(), "Erro no cadastro, tente novamente");
		driver.switchTo().alert().accept();
		Thread.sleep(3000);

		driver.close();
	}

	public String ConvertLocalDateIntoBrazilianString(LocalDate birthday) {
		return (birthday.getDayOfMonth() < 10 ? (0 + "" + birthday.getDayOfMonth()) : birthday.getDayOfMonth()) + ""
				+ (birthday.getMonth().getValue() < 10 ? (0 + "" + birthday.getMonth().getValue())
						: birthday.getMonth().getValue())
				+ "" + birthday.getYear();
	}

}
