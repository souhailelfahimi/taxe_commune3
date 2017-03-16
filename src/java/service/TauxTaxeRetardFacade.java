/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Categorie;
import bean.TauxTaxeRetard;
import bean.TaxeTrim;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author HP
 */
@Stateless
public class TauxTaxeRetardFacade extends AbstractFacade<TauxTaxeRetard> {

//    public double calculeTauxRetard(TaxeTrim taxeTrim)
//    {
//         Date  date = new Date();
//       int numbreofmonth=getMonthsDifference(date1,taxeTrim.getDatePaiement());
//             
//    }
//    public int  calculeNombreMoisRetard(int trimestre,int anne)
//    {
//            Date  date1 = new Date();
//          int numbreofmonth=getMonthsDifference(date1,)
//        
//    
//    }
    public static final int getMonthsDifference(Date date1, Date date2) {
    int m1 = date1.getYear() * 12 + date1.getMonth();
    int m2 = date2.getYear() * 12 + date2.getMonth();
    return m2 - m1 + 1;
}
    public Date trimestreLimit(int trimestre, int annee) {

        Calendar calendre = GregorianCalendar.getInstance();
        switch (trimestre) {
            case 1:
                calendre.set(annee, Calendar.MARCH, 31);
                break;
            case 2:
                calendre.set(annee, Calendar.JUNE, 30);
                break;
            case 3:
                calendre.set(annee, Calendar.SEPTEMBER, 30);
                break;
            case 4:
                calendre.set(annee, Calendar.DECEMBER, 31);
                break;
            default:
                break;
        }
        return calendre.getTime();
    }

    public Calendar debutChaqueMois(int mois, int annee) {
        Calendar calendre = GregorianCalendar.getInstance();
        switch (mois) {
            case 1:
                calendre.set(annee, Calendar.JANUARY, 01);
                break;
            case 2:
                calendre.set(annee, Calendar.FEBRUARY, 01);
                break;
            case 3:
                calendre.set(annee, Calendar.MARCH, 01);
                break;
            case 4:
                calendre.set(annee, Calendar.APRIL, 01);
                break;
            case 5:
                calendre.set(annee, Calendar.MAY, 01);
                break;
            case 6:
                calendre.set(annee, Calendar.JUNE, 01);
                break;
            case 7:
                calendre.set(annee, Calendar.JULY, 01);
                break;
            case 8:
                calendre.set(annee, Calendar.AUGUST, 01);
                break;
            case 9:
                calendre.set(annee, Calendar.SEPTEMBER, 01);
                break;
            case 10:
                calendre.set(annee, Calendar.OCTOBER, 01);
                break;
            case 11:
                calendre.set(annee, Calendar.NOVEMBER, 01);
                break;
            case 12:
                calendre.set(annee, Calendar.DECEMBER, 01);
                break;
            default:
                break;
        }
        return calendre;
    }

    public TauxTaxeRetard findTauxTaxeRetardByCategorie(Categorie categorie) {
        if (categorie != null && categorie.getId() != null) {
            List<TauxTaxeRetard> lst = em.createQuery("SELECT t FROM TauxTaxeRetard t WHERE t.categorie.id='" + categorie.getId() + "'").getResultList();
            if (lst != null && !lst.isEmpty()) {
                return lst.get(0);
            } else {
                return null;
            }
        } else {
            return null;
        }

    }

    @PersistenceContext(unitName = "projet_java_taxPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TauxTaxeRetardFacade() {
        super(TauxTaxeRetard.class);
    }

    public void clone(TauxTaxeRetard tauxTaxeRetardSource, TauxTaxeRetard tauxTaxeRetardDestaination) {
        tauxTaxeRetardDestaination.setId(tauxTaxeRetardSource.getId());
        tauxTaxeRetardDestaination.setCategorie(tauxTaxeRetardSource.getCategorie());
        tauxTaxeRetardDestaination.setTauxAutreRetard(tauxTaxeRetardSource.getTauxAutreRetard());
        tauxTaxeRetardDestaination.setTauxPremierRetard(tauxTaxeRetardSource.getTauxPremierRetard());

    }

    public TauxTaxeRetard clone(TauxTaxeRetard tauxTaxeRetard) {
        TauxTaxeRetard cloned = new TauxTaxeRetard();
        clone(tauxTaxeRetard, cloned);
        return cloned;
    }

}
