package com.spring.aiexpo2026.domain.travel.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Destination {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// 여행지 이름
	private String name;

	private String city;

	@Column(precision = 10, scale = 7)
	private BigDecimal latitude;

	@Column(precision = 10, scale = 7)
	private BigDecimal longitude;

	@Column(precision = 5, scale = 2)
	private BigDecimal base_score;
}
