
package org.sebsy.compta.app;

import org.sebsy.compta.dao.jdbc.BonDaoJdbc;
import org.sebsy.compta.infra.Db;

public final class MesureUneFois {
    public static void main(String[] args) {
        var dao = new BonDaoJdbc();
        System.out.println(dao.chargerGrapheALaMain(1L));
        Db.close();
    }
}
