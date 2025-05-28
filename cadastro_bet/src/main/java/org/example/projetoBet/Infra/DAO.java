package org.example.projetoBet.Infra;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DAO<E> {

    public static final Map<Class<?>, DAO<?>> instances = new HashMap<>();
    private static  EntityManager entityManager;
    private final Class<E> classe;

    private DAO(){
        this(null);
    }

    private DAO(Class<E> classe){
        this.classe = classe;
        entityManager = getEntityManager();
    }

    public static <E> DAO<E> getInstance(Class<E> entityClass) {
        return (DAO<E>) instances.computeIfAbsent(entityClass, c -> new DAO<>(entityClass));
    }

    private EntityManager getEntityManager(){
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("cadastro_bet");

        if (entityManager == null){
            entityManager = entityManagerFactory.createEntityManager();
        }
        return entityManager;
    }

    public void persist(E entity){
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(entity);
            entityManager.getTransaction().commit();
        }catch (Exception e){
            entityManager.getTransaction().rollback();
        }
    }

    public void merge(E entity){
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(entity);
            entityManager.getTransaction().commit();
        }catch (Exception e){
            entityManager.getTransaction().rollback();
        }
    }

    public void delete(Object entity){
        try {
            entityManager.getTransaction().begin();
            entityManager.remove(entity);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
        }
    }

    public List<E> findById(int id){
        try {
            return Collections.singletonList(entityManager.find(classe, id));
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            return null;
        }
    }

    public List<E> findAll(){
        return entityManager.createQuery("FROM " + classe.getName()).getResultList();
    }


    public List<E> findByEquals(String nomeCampo, Object valor){
        try {
            String consulta = "SELECT e FROM " + classe.getSimpleName() + " e WHERE e." + nomeCampo + " = :valor";
            return entityManager.createQuery(consulta, classe)
                    .setParameter("valor", valor)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public List<Object> findDistinctValues (String coluna){
        String consulta = "SELECT DISTINCT e." + coluna + " FROM " + classe.getSimpleName() + " e";
        return entityManager.createQuery(consulta).getResultList();
    }
    public static void closeEntityManager(){
        if (entityManager != null && entityManager.isOpen()){
            entityManager.close();
        }
    }
}
