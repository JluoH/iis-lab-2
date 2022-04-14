package ru.narfu.iislab2

import ru.narfu.iislab2.utils.AuditoriumDto
import ru.narfu.iislab2.utils.BuildingDto
import ru.narfu.iislab2.utils.Narfu
import ru.narfu.iislab2.utils.ScheduleDto
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

fun main() {
	print("Введите адрес корпуса: ")
	val building = findBuilding(readln(), Narfu.getBuildings())
	val auditorium = findAuditorium(Narfu.getAuditoriums(building.buildingOid))

	print("Введите дату (для примера 2018-09-08), чтобы получить расписание на неделю: ")
	val date = LocalDate.parse(readln())
	val weekSchedule = Narfu.getAuditoriumSchedule(
		auditoriumId = auditorium.auditoriumOid,
		startDate = date.with(DayOfWeek.MONDAY),
		endDate = date.with(DayOfWeek.SUNDAY)
	)
	printSchedule(weekSchedule)
}

tailrec fun findBuilding(address: String, buildings: List<BuildingDto>): BuildingDto {
	val matchedBuildings = buildings.filter {
		it.address?.contains(address, true) ?: false
	}.onEachIndexed { index, building ->
		println("[${index.inc()}] ${building.name}")
		println(building.address)
		println()
	}
	print("Введите номер с подходящим корпусом, либо уточните адрес: ")
	val readLn = readln()
	println()
	return when (val answer: Any = readLn.toIntOrNull() ?: readLn) {
		is Int -> matchedBuildings[answer.dec()]
		else -> findBuilding(answer as String, buildings)
	}
}

tailrec fun findAuditorium(auditoriums: List<AuditoriumDto>): AuditoriumDto {
	auditoriums.forEachIndexed { index, auditorium ->
		println("[${index.inc()}] ${auditorium.number} (${auditorium.typeOfAuditorium})")
	}
	println()
	print("Введите номер с нужной аудиторией: ")
	val readLn = readln()
	return when (val answer: Any = readLn.toIntOrNull() ?: readLn) {
		is Int -> auditoriums[answer.dec()]
		else -> findAuditorium(auditoriums)
	}
}

private fun printSchedule(schedule: List<ScheduleDto>) {
	schedule.groupBy(ScheduleDto::date).forEach { (dayDate, daySchedule) ->
		val dayOfWeekRus = dayDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale("ru")).uppercase()
		println("---------------------------------------------")
		println("$dayOfWeekRus - $dayDate")
		println("---------------------------------------------")
		daySchedule.forEach {
			println(it)
			println()
		}
	}
}
