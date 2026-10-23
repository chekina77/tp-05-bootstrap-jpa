
package org.sebsy.compta.infra;

import org.hibernate.SessionFactory;
import org.hibernate.jpa.HibernatePersistenceConfiguration;
import org.hibernate.tool.schema.Action;
import org.sebsy.compta.domain.Fournisseur;

public final class Jpa {

    private static final SessionFactory FACTORY = creerFactory();

    private Jpa() {
    }

    private static SessionFactory creerFactory() {
        return new HibernatePersistenceConfiguration("compta")
                .managedClasses(Fournisseur.class)
                .jdbcUrl(Env.required("DB_URL"))
                .property("jakarta.persistence.jdbc.user",
                        Env.required("DB_USER"))
                .property("jakarta.persistence.jdbc.password",
                        Env.required("DB_PASSWORD"))
                .jdbcPoolSize(10)
                .schemaToolingAction(Action.VALIDATE)
                .property("hibernate.generate_statistics", "true")
                .createEntityManagerFactory()
                .unwrap(SessionFactory.class);
    }

    public static SessionFactory factory() {
        return FACTORY;
    }

    public static void close() {
        FACTORY.close();
    }
}
