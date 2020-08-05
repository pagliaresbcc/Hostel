package br.com.hostel;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Teste {

	public static void main(String[] args) {
		LocalDate hoje = LocalDate.now();
		LocalDate amanha = LocalDate.of(2020, 8, 7);
		
		List<LocalDate> dates = Stream.iterate(hoje, date -> date.plusDays(1))
				.limit(ChronoUnit.DAYS.between(hoje, amanha))
				.collect(Collectors.toList());
		
		System.out.println(hoje);
		System.out.println(amanha);
		System.out.println(dates);
	}

}
