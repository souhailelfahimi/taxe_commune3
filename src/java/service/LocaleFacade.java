/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Locale;
import bean.Redevable;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author HP
 */
@Stateless
public class LocaleFacade extends AbstractFacade<Locale> {

    @PersistenceContext(unitName = "projet_java_taxPU")
    private EntityManager em;

    public List<Locale> findByRedevableCin(String redevable) {
        return em.createQuery("SELECT l FROM Locale l WHERE l.proprietaire.cin='" + redevable + "' OR l.gerant.cin='" + redevable + "'").getResultList();
    }

    public List<Locale> findByRedevableRc(String redevable) {
        return em.createQuery("SELECT l FROM Locale l WHERE l.proprietaire.rc= '" + redevable + "' OR l.gerant.rc='" + redevable + "'").getResultList();
    }

    public List<Locale> findByGerant(Redevable redevable) {
        return em.createQuery("SELECT l FROM Locale l WHERE l.gerant.id=" + redevable.getId()).getResultList();
    }

    public List<Redevable> findByReference(Locale locale) {
        return em.createQuery("SELECT l FROM Locale l WHERE l.reference='" + locale.getReference() + "'").getResultList();
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LocaleFacade() {
        super(Locale.class);
    }

}
