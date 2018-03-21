package com.siwoo.application.bootstrap;

import com.siwoo.application.domain.Client;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.metamodel.Metamodel;
import java.util.List;
import java.util.Map;

@Slf4j
public class Chapter2BootStrap {


    private static final String NULL_WARN = "must not be null";
    private static final String NOT_TRUE = "must be true";
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("testingunit1");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Assert.notNull(entityManager,NULL_WARN);
        EntityTransaction tx =null;
        try {
            tx = entityManager.getTransaction();
            tx.begin();
            entityManager.createQuery("delete from Client c").executeUpdate();
            PersistenceContextHelper helper = new PersistenceContextHelper(entityManager);
            Assert.notNull(helper,NULL_WARN);
            Client client = new Client();
            client.setUsername("Siwoo Kim");
            client.setAge(20);
            helper.persist(client);
            /*
                Determine the load state of an entity.
                boolean isLoaded(Object entity);
             */
            Assert.isTrue(Persistence.getPersistenceUtil().isLoaded(client),"Must be loaded");
            /*
                Remove the given entity from the persistence context, causing
                a managed entity to become detached.  Unflushed changes made
                to the entity if any (including removal of the entity),
                will not be synchronized to the database.

                public void detach(Object entity);
            */
            helper.detach(client);
            String changedName = "KIM";
            client.setUsername(changedName);
            client = entityManager.find(Client.class,client.getId());
            Assert.isTrue(client.getUsername().equals("Siwoo Kim"),"Must not changed");
            log.info(client.getUsername());

            helper.clear();
            client.setManaged(false);
            client.setUsername(changedName);
            client = helper.find(Client.class,client.getId());
            Assert.isTrue(client.getUsername().equals("Siwoo Kim"),"Must not changed");
            log.info(client.getUsername());

            //Bring the entity from the cache
            Client client2 = helper.find(Client.class,client.getId());
            Assert.isTrue(client == client2,"two instances are same");
            log.info(client.toString());
            log.info(client2.toString());

            int changedAge = 20;
            client2.setAge(changedAge);
            Assert.isTrue(client.getAge()==client2.getAge(),"two property of the same instance must be same");

            Client client3 = helper.find(Client.class,client.getId());
            Assert.isTrue(client3.getAge() == changedAge,"Dirty Checking must be performed");

            changedAge = 30;
            client3.setAge(changedAge);
            Client client4 = helper.createQuery("select c from Client c where id = :id",Client.class)
                    .setParameter("id",client.getId())
                    .getSingleResult();
            Assert.isTrue(client4.getAge() == changedAge,
                    "After executing Query, entity manager must flushes and also performs dirty checking");

            /*
                helper.setFlushMode(FlushModeType.COMMIT);
                client4.setAge(99999);
                client4 = helper.createQuery("select c from Client c where id = :id",Client.class)
                        .setParameter("id",client.getId())
                        .getSingleResult();

                Assert.isTrue(client4.getAge() == changedAge,
                        "FlushModeType.COMMIT only flushes when transaction is committed");
                entityManager.setFlushMode(FlushModeType.AUTO);
                client4.setAge(changedAge);
            */

            /**
             * Commit the current resource transaction, writing any
0             * unflushed changes to the database.
             */

            Client newClient = new Client();
            newClient.setUsername("JiEun Lee");
            newClient.setAge(25);
            helper.persist(newClient);
            helper.detach(newClient);
            Assert.isTrue(newClient.isManaged()!=true,"Not managed");
            log.info( helper.contains(newClient)+"" );
            //I do not know what detached entity is saved in database....
            tx.commit();
        }catch (Exception e){
            if(tx != null){
                tx.rollback();
            }
            throw e;
        }finally {
            entityManager.close();
        }
        entityManagerFactory.close();
    }

    public static class PersistenceContextHelper implements EntityManager {
        private EntityManager target;

        public PersistenceContextHelper(EntityManager entityManager) {
            target = entityManager;
        }

        void managed(Object entity,boolean isManaged){
            ((Client)(entity)).setManaged(isManaged);
        };

        @Override
        public void persist(Object entity) {
            target.persist(entity);
            managed(entity,true);
            log.info("Entity is managed in the persistence context");
        }

        @Override
        public <T> T merge(T entity) {
            return null;
        }

        @Override
        public void remove(Object entity) {

        }

        @Override
        public <T> T find(Class<T> entityClass, Object primaryKey) {
            log.info("Entity Manager query entity (managed)");
            T result = target.find(entityClass,primaryKey);
            managed(result,true);
            return result;
        }

        @Override
        public <T> T find(Class<T> entityClass, Object primaryKey, Map<String, Object> properties) {
            return null;
        }

        @Override
        public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode) {
            return null;
        }

        @Override
        public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode, Map<String, Object> properties) {
            return null;
        }

        @Override
        public <T> T getReference(Class<T> entityClass, Object primaryKey) {
            return null;
        }

        @Override
        public void flush() {

        }

        @Override
        public void setFlushMode(FlushModeType flushMode) {
            log.info("EntityManager change the flushMode");
            target.setFlushMode(flushMode);
        }

        @Override
        public FlushModeType getFlushMode() {
            return null;
        }

        @Override
        public void lock(Object entity, LockModeType lockMode) {

        }

        @Override
        public void lock(Object entity, LockModeType lockMode, Map<String, Object> properties) {

        }

        @Override
        public void refresh(Object entity) {

        }

        @Override
        public void refresh(Object entity, Map<String, Object> properties) {

        }

        @Override
        public void refresh(Object entity, LockModeType lockMode) {

        }

        @Override
        public void refresh(Object entity, LockModeType lockMode, Map<String, Object> properties) {

        }


        /**
         * Clear the persistence context, causing all managed
         * entities to become detached. Changes made to entities that
         * have not been flushed to the database will not be
         * persisted.
         */
        @Override
        public void clear() {
            target.clear();

            log.info("EntityManage clear all entities in the context");
        }

        @Override
        public void detach(Object entity) {
            target.detach(entity);
            managed(entity,false);
            log.info("Entity is not managed by EntityManager");
        }

        @Override
        public boolean contains(Object entity) {
            return false;
        }

        @Override
        public LockModeType getLockMode(Object entity) {
            return null;
        }

        @Override
        public void setProperty(String propertyName, Object value) {

        }

        @Override
        public Map<String, Object> getProperties() {
            return null;
        }

        @Override
        public Query createQuery(String qlString) {
            return null;
        }

        @Override
        public <T> TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery) {
            return null;
        }

        @Override
        public Query createQuery(CriteriaUpdate updateQuery) {
            return null;
        }

        @Override
        public Query createQuery(CriteriaDelete deleteQuery) {
            return null;
        }

        @Override
        public <T> TypedQuery<T> createQuery(String qlString, Class<T> resultClass) {
            log.info("EntityManager flushes all data for sync");
            return target.createQuery(qlString,resultClass);
        }

        @Override
        public Query createNamedQuery(String name) {
            return null;
        }

        @Override
        public <T> TypedQuery<T> createNamedQuery(String name, Class<T> resultClass) {
            return null;
        }

        @Override
        public Query createNativeQuery(String sqlString) {
            return null;
        }

        @Override
        public Query createNativeQuery(String sqlString, Class resultClass) {
            return null;
        }

        @Override
        public Query createNativeQuery(String sqlString, String resultSetMapping) {
            return null;
        }

        @Override
        public StoredProcedureQuery createNamedStoredProcedureQuery(String name) {
            return null;
        }

        @Override
        public StoredProcedureQuery createStoredProcedureQuery(String procedureName) {
            return null;
        }

        @Override
        public StoredProcedureQuery createStoredProcedureQuery(String procedureName, Class... resultClasses) {
            return null;
        }

        @Override
        public StoredProcedureQuery createStoredProcedureQuery(String procedureName, String... resultSetMappings) {
            return null;
        }

        @Override
        public void joinTransaction() {

        }

        @Override
        public boolean isJoinedToTransaction() {
            return false;
        }

        @Override
        public <T> T unwrap(Class<T> cls) {
            return null;
        }

        @Override
        public Object getDelegate() {
            return null;
        }

        @Override
        public void close() {
            target.close();
        }

        @Override
        public boolean isOpen() {
            return false;
        }

        @Override
        public EntityTransaction getTransaction() {
            return null;
        }

        @Override
        public EntityManagerFactory getEntityManagerFactory() {
            return null;
        }

        @Override
        public CriteriaBuilder getCriteriaBuilder() {
            return null;
        }

        @Override
        public Metamodel getMetamodel() {
            return null;
        }

        @Override
        public <T> EntityGraph<T> createEntityGraph(Class<T> rootType) {
            return null;
        }

        @Override
        public EntityGraph<?> createEntityGraph(String graphName) {
            return null;
        }

        @Override
        public EntityGraph<?> getEntityGraph(String graphName) {
            return null;
        }

        @Override
        public <T> List<EntityGraph<? super T>> getEntityGraphs(Class<T> entityClass) {
            return null;
        }
    }
}
