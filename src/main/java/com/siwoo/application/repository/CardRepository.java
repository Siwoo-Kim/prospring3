package com.siwoo.application.repository;

import com.siwoo.application.domain.Card;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface CardRepository {

    List<Card> findByPoint(double point);

    Card save(Card card);
}
