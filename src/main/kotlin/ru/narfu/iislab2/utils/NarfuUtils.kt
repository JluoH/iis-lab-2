package ru.narfu.iislab2.utils

import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.HttpMethod
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange
import java.time.LocalDate
import java.time.LocalTime

object Narfu {
	private val restTemp: RestTemplate = RestTemplateBuilder()
		.defaultHeader("Content-Type", "application/json")
		.defaultHeader("Accept", "application/json")
		.build()

	private const val HOST_URI = "http://testruz.agtu.ru/RUZService/RUZService.svc"
	private const val BUILDINGS_URI = "$HOST_URI/buildings"
	private const val AUDITORIUMS_URI = "$HOST_URI/auditoriums"
	private const val SCHEDULE_URI = "$HOST_URI/lessons"

	fun getBuildings(): List<BuildingDto> =
		restTemp.exchange<List<BuildingDto>>(
			url = BUILDINGS_URI,
			method = HttpMethod.GET
		).body?: emptyList()

	fun getAuditoriums(buildingId: Int = 0): List<AuditoriumDto> =
		restTemp.exchange<List<AuditoriumDto>>(
			url = "$AUDITORIUMS_URI?buildingOid=$buildingId",
			method = HttpMethod.GET
		).body?: emptyList()

	fun getAuditoriumSchedule(auditoriumId: Int, startDate: LocalDate, endDate: LocalDate): List<ScheduleDto> =
		restTemp.exchange<List<ScheduleDto>>(
			url = "$SCHEDULE_URI?fromDate=$startDate&toDate=$endDate&auditoriumOid=$auditoriumId",
			method = HttpMethod.GET
		).body ?: emptyList()
}

data class BuildingDto(
	val buildingOid: Int,
	val name: String,
	val address: String?,
	val abbr: String?,
)

data class AuditoriumDto(
	val auditoriumOid: Int,
	val number: String,
	val buildingOid: Int,
	val building: String,
	val typeOfAuditorium: String,
)

data class ScheduleDto(
	@JsonFormat(pattern = "yyyy.MM.dd")
	val date: LocalDate,
	val discipline: String,
	val lecturer: String,
	val beginLesson: LocalTime,
	val endLesson: LocalTime,
	val stream: String?,
) {
	override fun toString(): String =
		"""
		Дисциплина: $discipline
		Преподаватель: $lecturer
		Период занятия: $beginLesson - $endLesson
		Группы: ${stream ?: "-"}
		""".trimIndent()
}