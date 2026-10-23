
package org.sebsy.compta.app;

import org.sebsy.compta.domain.Fournisseur;
import org.sebsy.compta.infra.Jpa;
import org.hibernate.SessionFactory;

public class DemarrageJpa {

    public static void main(String[] args) {
        // On récupère la factory avant le try pour préserver l'erreur initiale.
        SessionFactory factory = Jpa.factory();

        try {
            System.out.println("Factory : " + factory.getClass().getName());

            factory.callInTransaction(session -> {
                Fournisseur fournisseur = session.find(Fournisseur.class, 4L);

                System.out.println("Fournisseur : " + fournisseur);

                if (fournisseur != null) {
                    System.out.println(
                            "Adresse complète : " +
                                    fournisseur.getAdresse().estComplete()
                    );
                }

                return null;
            });
        } finally {
            Jpa.close();
        }
    }
}
