package br.com.hostel.e2e;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

@RunWith(JUnitPlatform.class)
public class ReservationTest {

	ChromeConnection chromeConnection = new ChromeConnection();
	WebDriver driver = chromeConnection.Connection();

	@BeforeEach
	public void init() throws InterruptedException {
		driver.get("http://localhost:3000/");
		driver.manage().window().maximize();
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/section/form/input[1]")).sendKeys("admin@email.com");
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/section/form/input[2]")).sendKeys("123456");
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/section/form/button")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/header/a")).click();
		Thread.sleep(3000);
	}

	@Test
	public void registerANonExistentGuest() throws InterruptedException {

		driver.findElement(By.xpath("//*[@id=\"check-in\"]")).sendKeys("24102020");
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"check-out\"]")).sendKeys("26102020");
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"numberOfGuests\"]")).sendKeys("3");
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"maxDailyRate\"]")).sendKeys("300");
		Thread.sleep(4000);

		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/button")).click();
		Thread.sleep(3000);

		assertEquals(driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/h1")).getText(), "Quartos Disponiveis");

		Thread.sleep(3000);
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/ul/li[1]/button")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/ul/li[2]/button")).click();
		Thread.sleep(3000);
		
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/header/a")).click();
		Thread.sleep(3000);
		
		assertEquals(driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/section/h1")).getText(), "Selecione a forma de pagamento");
		
		
		WebElement pagamento = driver.findElement(By.xpath("//*[@id=\"payment\"]/div/div[1]/div[1]"));

		pagamento.click();
        
        Thread.sleep(3000);
        
        pagamento.sendKeys(Keys.DOWN);
		
		Thread.sleep(3000);
		
		
		
		driver.close();
	}

}
