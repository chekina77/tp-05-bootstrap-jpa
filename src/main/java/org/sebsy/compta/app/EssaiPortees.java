
package org.sebsy.compta.app;

import org.hibernate.SessionFactory;
import org.sebsy.compta.domain.Fournisseur;
import org.sebsy.compta.infra.Jpa;

public class EssaiPortees {

    public static void main(String[] args) {
        SessionFactory factory = Jpa.factory();

        try {
            factory.getStatistics().clear();

            Fournisseur f1 = factory.callInTransaction(session ->
                    session.find(Fournisseur.class, 1L));

            Fournisseur f2 = factory.callInTransaction(session ->
                    session.find(Fournisseur.class, 1L));

            System.out.println("Même instance (==) : " + (f1 == f2));
            System.out.println("Égalité (equals) : " + f1.equals(f2));
            System.out.println("Nombre de requêtes SQL : "
                    + factory.getStatistics().getPrepareStatementCount());

        } finally {
            factory.close();
        }
    }
}
