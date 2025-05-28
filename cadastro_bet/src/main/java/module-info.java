module cadastro.bet {
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.sql;
    requires javafx.controls;
    requires javafx.graphics;

    exports org.example.projetoBet.Infra;

    opens org.example.projetoBet;

    opens org.example.projetoBet.modelo to org.hibernate.orm.core, javafx.base;

}