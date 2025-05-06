package Clases;

import Clases.Cliente;
import Clases.Recargas;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2025-05-05T23:51:21", comments="EclipseLink-2.7.12.v20230209-rNA")
@StaticMetamodel(Celular.class)
public class Celular_ { 

    public static volatile SingularAttribute<Celular, String> Numero;
    public static volatile SingularAttribute<Celular, Cliente> cliente;
    public static volatile SingularAttribute<Celular, Double> Saldo;
    public static volatile SingularAttribute<Celular, Integer> idCel;
    public static volatile SingularAttribute<Celular, Double> Megas;
    public static volatile SingularAttribute<Celular, String> Estado;
    public static volatile ListAttribute<Celular, Recargas> recargas;

}