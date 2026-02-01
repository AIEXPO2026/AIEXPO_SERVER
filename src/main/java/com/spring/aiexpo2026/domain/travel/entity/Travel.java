package com.spring.aiexpo2026.domain.travel.entity;

import com.spring.aiexpo2026.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "travel")
public class Travel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// 방문 여행지 목록
	@OneToMany(mappedBy = "travel", fetch = FetchType.LAZY)
	private List<Destination> destinations = new ArrayList<>();

	// 여행 경로
	private String destination_route;

	// 여행 상태
	@Enumerated(EnumType.STRING)
	private TravelStatus travelStatus;

	// 여행 시작 일자
	private LocalDate startAt;

	// 여행 종료 일자
	private LocalDate endAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;
}
