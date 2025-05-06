package Clases;

import Clases.Celular;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2025-05-05T23:51:21", comments="EclipseLink-2.7.12.v20230209-rNA")
@StaticMetamodel(Cliente.class)
public class Cliente_ { 

    public static volatile SingularAttribute<Cliente, String> Apellidos;
    public static volatile SingularAttribute<Cliente, String> Nombres;
    public static volatile ListAttribute<Cliente, Celular> celular;
    public static volatile SingularAttribute<Cliente, String> Cedula;
    public static volatile SingularAttribute<Cliente, Integer> idClie;

}