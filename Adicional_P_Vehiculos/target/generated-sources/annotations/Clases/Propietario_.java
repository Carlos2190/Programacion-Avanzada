package Clases;

import Clases.Vehiculo;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2025-05-05T21:06:07", comments="EclipseLink-2.7.12.v20230209-rNA")
@StaticMetamodel(Propietario.class)
public class Propietario_ { 

    public static volatile SingularAttribute<Propietario, String> cedula;
    public static volatile SingularAttribute<Propietario, String> apellido;
    public static volatile ListAttribute<Propietario, Vehiculo> vehiculo;
    public static volatile SingularAttribute<Propietario, Integer> idprop;
    public static volatile SingularAttribute<Propietario, String> nombre;

}