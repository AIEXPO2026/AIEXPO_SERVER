package com.spring.aiexpo2026.domain.bookmark.repository;

import com.spring.aiexpo2026.domain.bookmark.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    List<Bookmark> findByMember_Id(Long memberId);

    boolean existsByMember_IdAndDestination_Id(Long memberId, Long destinationId);

    boolean existsByMember_IdAndCountry_Id(Long memberId, Long countryId);

    void deleteByMember_IdAndDestination_Id(Long memberId, Long destinationId);

    void deleteByMember_IdAndCountry_Id(Long memberId, Long countryId);
}
