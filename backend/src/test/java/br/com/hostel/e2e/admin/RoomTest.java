package br.com.hostel.e2e.admin;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import br.com.hostel.e2e.ChromeConnection;

@RunWith(JUnitPlatform.class)
public class RoomTest {

	ChromeConnection chromeConnection = new ChromeConnection();
	WebDriver driver = chromeConnection.Connection();

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
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/a[2]")).click();
		Thread.sleep(3000);
	}

	@Test
	public void registerANewRoom() throws InterruptedException {

		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/header/a")).click();
		Thread.sleep(3000);

		assertEquals(driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/section/h1")).getText(),
				"Cadastrar novo quarto");

		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/input[1]")).sendKeys("299");
		Thread.sleep(3000);
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/textarea")).sendKeys(
				"A CASA DA ÁRVORE é uma acomodação diferenciada e única, ela esta localizada a 10 metros de altura, em um ambiente com bastante privacidade... no topo das árvores.");
		Thread.sleep(3000);
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/input[2]")).sendKeys("13");
		Thread.sleep(3000);
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/input[3]")).sendKeys("2");
		Thread.sleep(3000);
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/input[4]")).sendKeys("300");
		Thread.sleep(3000);
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/button")).click();
		Thread.sleep(3000);
		
		Alert alert = driver.switchTo().alert();
        alert.accept();
        Thread.sleep(3000);

		driver.close();
	}

	@Test
	public void deleteRoom() throws InterruptedException {

		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/ul/li[2]/button")).click();
		Thread.sleep(3000);
		Alert firstAlert = driver.switchTo().alert();
		firstAlert.accept();
        Thread.sleep(3000);
		Alert secondAlert = driver.switchTo().alert();
		secondAlert.accept();
        Thread.sleep(3000);

		driver.close();
	}
}

