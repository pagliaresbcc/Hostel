package br.com.hostel.e2e;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

@RunWith(JUnitPlatform.class)
public class ReservationTest {

	ChromeConnection chromeConnection = new ChromeConnection();
	WebDriver driver = chromeConnection.Connection();

	@BeforeEach
	public void init() throws InterruptedException {
		// make login
		driver.get("http://localhost:3000/");
		driver.manage().window().maximize();
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/section/form/input[1]")).sendKeys("admin@email.com");
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/section/form/input[2]")).sendKeys("123456");
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/section/form/button")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/a[1]")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/header/a")).click();
		Thread.sleep(3000);
	}

	@Test
	public void registerANonExistentGuest() throws InterruptedException {

		driver.findElement(By.xpath("//*[@id=\"check-in\"]")).sendKeys("24102025");
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"check-out\"]")).sendKeys("26102025");
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"numberOfGuests\"]")).sendKeys("3");
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"maxDailyRate\"]")).sendKeys("300");
		Thread.sleep(4000);

		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/button")).click();
		Thread.sleep(3000);

		assertEquals(driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/h1")).getText(), "Quartos Disponiveis");

		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/ul/li[1]/button")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/ul/li[2]/button")).click();
		Thread.sleep(1000);

		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/a")).click();
		Thread.sleep(3000);

		assertEquals(driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/section/h1")).getText(),
				"Selecione a forma de pagamento");

		driver.findElement(By.xpath("//*[@id=\"payment\"]")).click();

		Thread.sleep(3000);
		driver.findElement(By.xpath("//*[@id=\"react-select-3-option-0\"]")).click();

		Thread.sleep(3000);
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/button")).click();
		Thread.sleep(5000);
		
		driver.close();
	}

}
