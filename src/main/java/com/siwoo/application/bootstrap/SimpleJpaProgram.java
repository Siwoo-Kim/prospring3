package com.siwoo.application.bootstrap;

import com.siwoo.application.domain.Client;
import com.siwoo.application.domain.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Scanner;

@Slf4j
public class SimpleJpaProgram {

    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("testingunit1");
        Assert.notNull(factory);
        EntityManager entityManager = factory.createEntityManager();
        Assert.notNull(entityManager);
        SimpleEmployeeRepository simpleEmployeeRepository = new SimpleEmployeeRepository(entityManager);
        EntityTransaction tx = entityManager.getTransaction();
        try{
            tx.begin();

            simpleEmployeeRepository.deleteAll();
            Client client = new Client();
            client.setUsername("Siwoo Kim");
            client.setAge(29);
            client = simpleEmployeeRepository.save(client);
            Assert.notNull(client,"Employee must not be empty");
            log.info(client.getId()+" is generated");

            List<Client> clients = simpleEmployeeRepository.findAll();
            Assert.notNull(clients,"Clients must not be null");
            Assert.state(clients.size()==1,"Clients size must be 1");

            simpleEmployeeRepository.remove(client);

            clients = simpleEmployeeRepository.findAll();
            Assert.notNull(clients,"Clients must not be null");
            Assert.state(clients.size()==0,"Clients size must be 1");

            tx.commit();
        }catch (Exception e){
            tx.rollback();
        }finally {
            entityManager.close();
        }
        factory.close();

    }

    private static class SimpleEmployeeRepository{
        EntityManager entityManager;
        SimpleEmployeeRepository(EntityManager entityManager){
            this.entityManager = entityManager;
        }

        Client save(Client client){
            Assert.notNull(client,"Employee must not be empty");
            entityManager.persist(client);
            return client;
        }

        List<Client> findAll(){
            return entityManager
                    .createQuery("select e from Client e",Client.class)
                    .getResultList();
        }

        public void deleteAll() {
            entityManager.createNativeQuery("delete from client")
                    .executeUpdate();
        }

        public void remove(Client client) {
            Assert.notNull(client,"Employee must not be empty");
            entityManager.remove(client);
        }
    }
}
