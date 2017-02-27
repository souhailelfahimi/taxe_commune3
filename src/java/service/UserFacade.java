/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.User;
import controler.util.JsfUtil;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author ayoub
 */
@Stateless
public class UserFacade extends AbstractFacade<User> {

    @PersistenceContext(unitName = "projet_java_taxPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserFacade() {
        super(User.class);
    }
     public Object[] addUser(User user) {

        if ("".equals(user.getLogin()) || user.getLogin() == null) {

            return new Object[]{-1, null};

        }

        User loadedUser = find(user.getLogin());

        if (loadedUser != null) {

            return new Object[]{-2, null};

        } else if ("".equals(user.getPassword()) || user.getPassword() == null) {

            return new Object[]{-3, null};

        } else {

            user.setBlocked(0);

            create(user);

            return new Object[]{1, loadedUser};

        }

    }

    

    public Object[] modify(User user, String nvPasswd) {

        if ("".equals(nvPasswd) || nvPasswd == null) {

            return new Object[]{-1, null};

        } else {

            user.setPassword(nvPasswd);

            edit(user);

            return new Object[]{1, user};

        }

    }

    

    public Object[] seConnecte(User user) {

        User loadedUser = find(user.getLogin());

        if (loadedUser == null) {
            JsfUtil.addErrorMessage("ERROR!!");

            return new Object[]{-1, null};

        } else if (!loadedUser.getPassword().equals(user.getPassword())) {

            return new Object[]{-2, null};

        } else if (loadedUser.getBlocked() == 1) {

            return new Object[]{-3, null};

        } else {

            return new Object[]{1, loadedUser};

        }

    }



    public int verifiePassword(User user, String pass) {

        if (!user.getPassword().equals(pass)) {

            return -1;

        } else {

            return 1;

        }

    }

    

    public void bloquer(User user) {

        user.setBlocked(1);

        edit(user);

    }
    
}
