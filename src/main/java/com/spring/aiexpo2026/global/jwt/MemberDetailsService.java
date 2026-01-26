package com.spring.aiexpo2026.global.jwt;

import com.spring.aiexpo2026.domain.member.entity.Member;
import com.spring.aiexpo2026.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {

	private final MemberRepository memberRepository;

	@Override
	public UserDetails loadUserByUsername(@NonNull
											  String username) throws UsernameNotFoundException {

		Member member = memberRepository.findByUsername(username).orElseThrow(()
				-> new UsernameNotFoundException("유저정보를 찾을 수 없습니다."));
		return new MemberDetails(member);
	}
}
