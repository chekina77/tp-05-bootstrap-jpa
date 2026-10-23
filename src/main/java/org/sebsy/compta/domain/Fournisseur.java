
package org.sebsy.compta.domain;

import jakarta.persistence.*;

import java.util.Objects;

/**
 * A supplier.
 *
 * <p>Not final, with a protected no-argument constructor: an ORM instantiates it by reflection.
 * The identifier is a {@code Long}: an unsaved supplier has none, which a {@code long} cannot
 * express.
 */
@Entity
@Table(name = "fournisseur")
public class Fournisseur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 60, unique = true)
    private String nom;


    @Embedded
    @AttributeOverrides({
            @AttributeOverride(
                    name = "rue",
                    column = @Column(name = "rue", length = 120)
            ),
            @AttributeOverride(
                    name = "codePostal",
                    column = @Column(name = "code_postal", length = 10)
            ),
            @AttributeOverride(
                    name = "ville",
                    column = @Column(name = "ville", nullable = false, length = 60)
            )
    })
    private Adresse adresse;


    protected Fournisseur() {
    }

    /** A supplier not yet saved: no identifier. */
    public Fournisseur(String nom, Adresse adresse) {
        this.nom = Objects.requireNonNull(nom, "nom is mandatory");
        this.adresse = Objects.requireNonNull(adresse, "adresse is mandatory");
    }

    /** A supplier read from the database, identifier known. */
    public Fournisseur(Long id, String nom, Adresse adresse) {
        this(nom, adresse);
        this.id = id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = Objects.requireNonNull(nom);
    }

    public Adresse getAdresse() {
        return adresse;
    }

    public void setAdresse(Adresse adresse) {
        this.adresse = Objects.requireNonNull(adresse);
    }

    /**
     * Identity on {@code nom}: UNIQUE NOT NULL in the table, so a stable business key. The
     * generated id would change the hash code on insertion and lose the object in a HashSet.
     */
    @Override
    public boolean equals(Object other) {
        return other instanceof Fournisseur autre && nom.equals(autre.getNom());
    }

    @Override
    public int hashCode() {
        return nom.hashCode();
    }

    @Override
    public String toString() {
        return "Fournisseur[id=%s, nom=%s, ville=%s]".formatted(id, nom, adresse.ville());
    }
}
