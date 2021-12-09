package br.com.hostel.e2e.admin;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import br.com.hostel.e2e.ChromeConnection;
import br.com.hostel.model.Address;
import br.com.hostel.model.Guest;
import br.com.hostel.model.helper.Role;

@RunWith(JUnitPlatform.class)
@TestMethodOrder(OrderAnnotation.class)
public class GuestTest {

	ChromeConnection chromeConnection = new ChromeConnection();
	WebDriver driver = chromeConnection.Connection();

	Guest newGuest;
	
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
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/a[3]")).click();
		Thread.sleep(3000);
	}
	
	@Test
	@Order(1)
	public void registerANewGuest() throws InterruptedException {
		
		Address newAddress = new Address("Arena Corinthians", "99999-000", "São Paulo", "SP", "Brasil");
		newGuest = new Guest("Melhor Goleiro", "Cássio", "Ramos", LocalDate.of(1987, 6, 6), newAddress,
				"cassiao@corinthians.com", "123456", Role.ROLE_ADMIN);
		
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/header/a")).click();
		Thread.sleep(3000);
		
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
		assertEquals("Cadastrado", driver.switchTo().alert().getText());

		driver.switchTo().alert().accept();

		Thread.sleep(3000);
		driver.close();
		
	}
	
	@Test
	@Order(2)
	public void updateGuest() throws InterruptedException {
		
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/ul/li[2]/button[2]")).click();
		Thread.sleep(3000);
		
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/div[1]/input[1]")).clear();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/div[1]/input[1]")).sendKeys("João");
		Thread.sleep(3000);
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/div[1]/input[2]")).clear();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/div[1]/input[2]")).sendKeys("Paulo");
		Thread.sleep(3000);
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/div[2]/input[1]")).clear();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/div[2]/input[1]")).sendKeys("Vila Belmiro");
		Thread.sleep(3000);
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/button")).click();
		Thread.sleep(3000);
		
		driver.close();
		
	}
	
	@Test
	@Order(3)
	public void deleteGuest() throws InterruptedException {
		
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/ul/li[3]/button[1]")).click();
		Thread.sleep(3000);

		assertEquals("Tem certeza que deseja deletar este usuário?", driver.switchTo().alert().getText());
		driver.switchTo().alert().accept();
		Thread.sleep(3000);

		assertEquals("Usuário deletado com sucesso!", driver.switchTo().alert().getText());
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
