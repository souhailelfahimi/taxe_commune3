/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Journal;
import bean.Redevable;
import bean.User;
import controler.util.SessionUtil;
import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author ayoub
 */
@Stateless
public class JournalFacade extends AbstractFacade<Journal> {

    @PersistenceContext(unitName = "projet_java_taxPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public JournalFacade() {
        super(Journal.class);
    }
//     public  String deconnection(){
//        User user=SessionUtil.getConnectedUser();
//        SessionUtil.unSetUser(user);
//        selected=new Historique(new Date(),2,user);
//        ejbFacade.create(selected);
//        return "/faces/index";
//    }

    public void journalCreatorDelet(String beanName, int type) {
        User user = SessionUtil.getConnectedUser();
        Journal journal = new Journal(new Date(), type, beanName, user);
        create(journal);
    }

    public void journalUpdateRedevable(String beanName, int type, Redevable oldred, Redevable newred) {

        User user = SessionUtil.getConnectedUser();

        Journal journal = new Journal(new Date(), type, oldred.toString(), newred.toString(), beanName, user);
        create(journal);

    }

    public void clone(Journal journalSource, Journal journalDestaination) {
        journalDestaination.setId(journalSource.getId());

    }

    public Journal clone(Journal journal) {
        Journal cloned = new Journal();
        clone(journal, cloned);
        return cloned;
    }
}
