package com.siwoo.application.repository;

import com.siwoo.application.domain.Address;
import com.siwoo.application.domain.Member;
import com.siwoo.application.domain.MemberSummary;
import com.siwoo.application.domain.Point;

import java.time.LocalDate;
import java.util.List;

public interface MemberRepository {
    Member save(Member member);

    List<Member> findByUsername(String username);

    List<Member> findAll();

    List<Object[]> findUsernameAndAge();

    Member findById(long id);

    List<Member> findByJoinDate(LocalDate localDate);

    Point findPointsById(long memberId);

    Address findAddressById(long memberId);

    double findAvgPoints();

    List<Object[]> findPointAndUsernameAndJoinDate();

    List<MemberSummary> findMemberSummary();

    List<Member> findAll(MemberRepositoryImpl.SimplePaging paging);
}
