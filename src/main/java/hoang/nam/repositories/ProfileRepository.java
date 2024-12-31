/**
 * @ (#) UserRepository.java 1.0 30-Dec-24
 * <p>
 * Copyright (c) 2024 IUH. All rights reserved.
 */
package hoang.nam.repositories;

import hoang.nam.models.Profile;
import jakarta.persistence.*;

import java.util.List;
import java.util.Optional;

/**
 * @description:
 * @author: Le Hoang Nam
 * @date: 30-Dec-24
 * @version: 1.0
 */
public class ProfileRepository {

    private final EntityManager em;

    public ProfileRepository(EntityManager em) {
        this.em = em;
    }

    public Optional<Profile> findByUsername(String username) {
        System.out.println("Searching for username: " + username);
        TypedQuery<Profile> query = em.createQuery(
                "SELECT p FROM Profile p WHERE p.username = :username", Profile.class);
        query.setParameter("username", username);

        List<Profile> results = query.getResultList();
        System.out.println("Query results: " + results);

        return results.stream().findFirst();
    }

    public Optional<Profile> findByProfileId(String profileId) {
        System.out.println("Searching for profile with ID: " + profileId);

        TypedQuery<Profile> query = em.createQuery(
                "SELECT p FROM Profile p WHERE p.profileId = :profileId", Profile.class);
        query.setParameter("profileId", profileId);

        List<Profile> results = query.getResultList();
        System.out.println("Query results: " + results);

        return results.stream().findFirst();
    }

    public Optional<Profile> findByEmail(String email) {
        TypedQuery<Profile> query = em.createQuery(
                "SELECT p FROM Profile p WHERE p.email = :email", Profile.class);
        query.setParameter("email", email);
        return query.getResultStream().findFirst();
    }

    public List<Profile> findBySearchTerm(String searchTerm, Integer firstResult, Integer maxResults) {
        TypedQuery<Profile> query = em.createQuery(
                "SELECT p FROM Profile p WHERE LOWER(p.username) LIKE :search OR LOWER(p.email) LIKE :search", Profile.class);
        query.setParameter("search", "%" + searchTerm.toLowerCase() + "%");

        if (firstResult != null) query.setFirstResult(firstResult);
        if (maxResults != null) query.setMaxResults(maxResults);

        List<Profile> ps = query.getResultList();

        return ps;
    }

    public Profile createUser(Profile profile) {
        try {
            em.getTransaction().begin();
            em.persist(profile);
            em.getTransaction().commit();
            return profile;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Error while creating user: " + e.getMessage(), e);
        }
    }

    public boolean deleteById(String id) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Profile profile = em.find(Profile.class, id);
            if (profile != null) {
                em.remove(profile);
                transaction.commit();
                return true;
            }
            transaction.rollback();
            System.out.println("No profile found with ID: " + id);
            return false;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Error occurred while deleting profile with ID: " + id);
            e.printStackTrace();
            return false;
        }
    }

    public int count() {
        Object count = em.createQuery("SELECT COUNT(p) FROM Profile p").getSingleResult();
        return ((Number) count).intValue();
    }

    public void close() {
        if (em.isOpen()) {
            em.close();
        }
    }
}
