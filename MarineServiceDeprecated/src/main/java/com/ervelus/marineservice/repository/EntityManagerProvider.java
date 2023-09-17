package com.ervelus.marineservice.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.ext.Provider;

@Provider
public class EntityManagerProvider {
    @PersistenceContext
    private EntityManager em;

    public EntityManager getEntityManager() {
        return em;
    }
}
