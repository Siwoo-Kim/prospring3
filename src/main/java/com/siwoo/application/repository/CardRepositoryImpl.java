package com.siwoo.application.repository;

import com.siwoo.application.domain.Card;
import com.siwoo.application.domain.Card_;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.List;

@Repository("cardRepository")
public class CardRepositoryImpl implements CardRepository {

    @PersistenceContext EntityManager entityManager;

    @Override
    public List<Card> findByPoint(double point) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Card> query = cb.createQuery(Card.class);
        /*Root forms the basis for path expressions*/
        Root<Card> cardRoot = query.from(Card.class);
        cardRoot.fetch(Card_.owner, JoinType.LEFT);

        query.select(cardRoot).distinct(true);

        Predicate predicate = cb.equal(cardRoot.get(Card_.point),point);
        query.where(predicate);

        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public Card save(Card card){
        Assert.notNull(card,"Card must not be null");
        if(card.getId() == null){
            System.out.println("card is saved");
            entityManager.persist(card);
        }else{
            entityManager.merge(card);
        }
        return card;
    }
}
