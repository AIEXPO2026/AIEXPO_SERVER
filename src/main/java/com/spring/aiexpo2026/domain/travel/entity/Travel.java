package com.spring.aiexpo2026.domain.travel.entity;

import com.spring.aiexpo2026.domain.auth.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Travel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@Column(name = "budget_min")
	private int budgetMin;

	@Column(name = "budget_max")
	private int budgetMax;

	@Column(name = "start_date")
	private LocalDate startDate;

	@Column(name = "end_date")
	private LocalDate endDate;

	@Builder.Default
	@Column(name = "people_count")
	private int peopleCount = 1;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Enumerated(EnumType.STRING)
	private TravelStatus travelStatus;

	@Builder.Default
	private int mood = 5; // 여행 분위기

	@Column(name = "avg_weather")
	private String avgWeather; // 여행 중 평균 날씨

	@Builder.Default
	private boolean publicTravel = false; // 여행 공개 여부

	public void updateEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public void updateTravelStatus(TravelStatus travelStatus) {
		this.travelStatus = travelStatus;
	}

	public void updateMood(int mood) {
		this.mood = mood;
	}

	public void updateAvgWeather(String avgWeather) {
		this.avgWeather = avgWeather;
	}

	public void updatePeopleCount(int peopleCount) {
		this.peopleCount = peopleCount;
	}

	public void updatePublicTravel(boolean publicTravel) {
		this.publicTravel = publicTravel;
	}
}
