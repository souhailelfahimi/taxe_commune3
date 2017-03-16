/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.AnnexeAdministratif;
import bean.Categorie;
import bean.Locale;
import bean.Quartier;
import bean.Rue;
import bean.Secteur;
import bean.TaxeAnnuel;
import bean.TaxeTrim;
import controler.util.SearchUtil;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

/**
 *
 * @author HP
 */
@Stateless
public class TaxeTrimFacade extends AbstractFacade<TaxeTrim> {

    @PersistenceContext(unitName = "projet_java_taxPU")
    private EntityManager em;
    @EJB
    TauxTaxeFacade tauxTaxeFacade;
    @EJB
    TauxTaxeRetardFacade tauxTaxeRetardFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TaxeTrimFacade() {
        super(TaxeTrim.class);
    }
    @EJB
    private TaxeAnnuelFacade taxeAnnuelFacade;

    //creation d'une taxeTrim  ali
    public Object[] create(TaxeTrim taxeTrim, int annee, boolean simuler) {//false=save;true=simuler
        TaxeTrim loadedTaxe = findTaxeByTaxeAnnuel(taxeTrim, annee);
        if (loadedTaxe != null) {
            return new Object[]{-1, null};
        } else {
            //faire les calcules sans les enregistre dans la base de donnees
            System.out.println("les setters");
            taxeTrim.setMontantTotal(100D * taxeTrim.getNombreNuit());
            taxeTrim.setMontant(80D);
            taxeTrim.setMontantRetard(20D);
            taxeTrim.setPremierMoisRetard(12D);
            taxeTrim.setNbrMoisRetard(2);
            taxeTrim.setAutresMoisRetard(8D);
            if (1 == 1) {
                System.out.println("a");
            }
            if (simuler == false) {
                taxeAnnuelFacade.create(taxeTrim.getLocale(), annee);//si il n'existe une taxeAnnuel avec ce locale et l'annee il va le creer 
                TaxeAnnuel taxeAnnuel = taxeAnnuelFacade.findByLocaleAndAnnee(taxeTrim.getLocale(), annee);//100% taxeTrim existe dans la base de donnees
                if (taxeAnnuel.getNbrTrimesterPaye() >= 4) {//tous les taxeTrim sont paye pour cette annee e ce locale
                    System.out.println("3ndha kter mn 3 deja mkhlsinn");
                    return new Object[]{-2, null};
                } else {
                    System.out.println("incrementation");
                    taxeAnnuel.setNbrTrimesterPaye(taxeAnnuel.getNbrTrimesterPaye() + 1);
                    taxeAnnuelFacade.edit(taxeAnnuel);
                }
                System.out.println("attachement");
                taxeTrim.setTaxeAnnuel(taxeAnnuel);
            }
        }
        System.out.println("kolchi howa hadak");
        return new Object[]{1, taxeTrim};
    }

//la recherche d'une taxeTrim avec TaxeAnnuel,locale,numero
    public TaxeTrim findTaxeByTaxeAnnuel(TaxeTrim taxeTrim, int annee) {
        String requette = "SELECT tax FROM TaxeTrim tax where 1=1";
        requette += " AND tax.taxeAnnuel.annee =" + annee;
        requette += " AND tax.locale.id =" + taxeTrim.getLocale().getId();
        requette += " AND tax.numeroTrim =" + taxeTrim.getNumeroTrim();
        List<TaxeTrim> list = em.createQuery(requette).getResultList();
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public List<TaxeTrim> findTaxesByLocale(Locale locale) {
       
            return em.createQuery("SELECT tax FROM TaxeTrim tax where tax.locale.id='" + locale.getId() + "'").getResultList();
        

    }

    // recherche des taxes pour extraire un graphe
    public List<TaxeTrim> findTaxByCritere(String activite, int firstYear, int secondYear, Rue rue, Quartier quartier, AnnexeAdministratif annex, Secteur secteur) {
        String rqt = "SELECT tax FROM TaxeTrim tax where 1=1 ";
        if (activite != null) {
            rqt += SearchUtil.addConstraint("tax.locale", "activite", "=", activite);
        }
        if (firstYear > 0) {
            rqt += "AND tax.taxeAnnuel.annee=" + firstYear;
        }
        if (secondYear > 0) {
            rqt += " AND tax.taxeAnnuel.annee=" + secondYear;
        }
        if (rue == null) {
            if (quartier == null) {
                if (annex == null) {
                    if (secteur != null) {
                        rqt += SearchUtil.addConstraint("tax.locale", "rue.quartier.annexeAdministratif.secteur.id", "=", secteur.getId());
                    }
                } else {
                    rqt += SearchUtil.addConstraint("tax.locale", "rue.quartier.annexeAdministratif.id", "=", annex.getId());
                }
            } else {
                rqt += SearchUtil.addConstraint("tax.locale", "rue.quartier.id", "=", quartier.getId());
            }
        } else {
            rqt += SearchUtil.addConstraint("tax.locale", "rue.id", "=", rue.getId());
        }
        return em.createQuery(rqt).getResultList();
    }

    //pour construire la sereis des coordonnees
    public BarChartModel initBarModel(List<TaxeTrim> taxes, int firstYear, int secondYear) {
        ChartSeries firstYearTaxe = new ChartSeries();
        ChartSeries secondYearTaxe = new ChartSeries();
        BarChartModel model1 = new BarChartModel();
        firstYearTaxe.setLabel("" + firstYear);
        secondYearTaxe.setLabel("" + secondYear);
        int x;
        for (x = 1; x < 5; x++) {
            Double a = 0.0;
            Double b = 0.0;
            for (TaxeTrim taxeTrim : taxes) {
                if (taxeTrim.getTaxeAnnuel().getAnnee() == firstYear && taxeTrim.getNumeroTrim() == x) {
                    a += taxeTrim.getMontantTotal();
                }
                if (taxeTrim.getTaxeAnnuel().getAnnee() == secondYear && taxeTrim.getNumeroTrim() == x) {
                    b += taxeTrim.getMontantTotal();
                }
            }
            firstYearTaxe.set("Trimestre " + x, a);
            secondYearTaxe.set("Trimestre " + x, b);
        }
        model1.addSeries(firstYearTaxe);
        model1.addSeries(secondYearTaxe);
        return model1;
    }

    // hadi recherche ta3 taxetrime bga3 les crétére 
    public List<TaxeTrim> findLocaleByCretere(Date dateMin, Date dateMax, Double montantMin, Double montantMax, int nombreNuitMin, int nombreNuitMax, String local, String redevable, Categorie categorie, Secteur secteur, AnnexeAdministratif annexeAdministratif, Quartier quartier, Rue rue) {
        {
            String requete = "SELECT ta FROM TaxeTrim ta WHERE 1=1  ";
            requete += SearchUtil.addConstraintMinMaxDate("ta", "datePaiement", dateMin, dateMax);
            if (!local.equals("")) {
                requete += " AND ta.locale.reference='" + local + "'";
            }
            if (!redevable.equals("")) {
                requete += " AND ta.redevable.cin='" + redevable + "'";
            }
            if (categorie != null) {
                requete += " AND ta.locale.categorie.id='" + categorie.getId() + "'";
            }
            if (secteur != null) {
                requete += " AND ta.locale.rue.quartier.annexeAdministratif.secteur.id='" + secteur.getId() + "'";
            }
            if (annexeAdministratif != null) {
                requete += " AND ta.locale.rue.quartier.annexeAdministratif.id='" + annexeAdministratif.getId() + "'";
            }
            if (quartier != null) {
                requete += " AND ta.locale.rue.quartier.id='" + quartier.getId() + "'";
            }
            if (rue != null) {
                requete += " AND ta.locale.rue.id='" + rue.getId() + "'";
            }
            requete += SearchUtil.addConstraintMinMax("ta", "montantTotal", montantMin, montantMax);
            if (nombreNuitMin > 0) {
                requete += " AND ta.nombreNuit >='" + nombreNuitMin + "'";

            }
            if (nombreNuitMax > 0) {
                requete += " AND ta.nombreNuit <='" + nombreNuitMax + "'";

            }

            return em.createQuery(requete).getResultList();

        }
    }

    public void clone(TaxeTrim taxeTrimSource, TaxeTrim taxeTrimDestaination) {
        taxeTrimDestaination.setId(taxeTrimSource.getId());
        taxeTrimDestaination.setAutresMoisRetard(taxeTrimSource.getAutresMoisRetard());
        taxeTrimDestaination.setLocale(taxeTrimSource.getLocale());
        taxeTrimDestaination.setDatePaiement(taxeTrimSource.getDatePaiement());
        taxeTrimDestaination.setMontant(taxeTrimSource.getMontant());
        taxeTrimDestaination.setMontantRetard(taxeTrimSource.getMontantRetard());
        taxeTrimDestaination.setMontantTotal(taxeTrimSource.getMontantTotal());
        taxeTrimDestaination.setNbrMoisRetard(taxeTrimSource.getNbrMoisRetard());
        taxeTrimDestaination.setNombreClients(taxeTrimSource.getNombreClients());
        taxeTrimDestaination.setNombreNuit(taxeTrimSource.getNombreNuit());
        taxeTrimDestaination.setNumeroTrim(taxeTrimSource.getNumeroTrim());
        taxeTrimDestaination.setRedevable(taxeTrimSource.getRedevable());
        taxeTrimDestaination.setTaxeAnnuel(taxeTrimSource.getTaxeAnnuel());

    }

    public TaxeTrim clone(TaxeTrim taxeTrim) {
        TaxeTrim cloned = new TaxeTrim();
        clone(taxeTrim, cloned);
        return cloned;
    }

}
