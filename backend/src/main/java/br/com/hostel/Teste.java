package br.com.hostel;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class Teste {

	public static void main(String[] args) {
		LocalDate now = LocalDate.now();
		ZonedDateTime chicago = now.atStartOfDay(ZoneId.of("America/Sao_Paulo"));
		System.out.println("Chicago: " + chicago.toLocalDate());
	}

}
