package Clases;

import Clases.Vehiculo;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2025-05-05T21:06:07", comments="EclipseLink-2.7.12.v20230209-rNA")
@StaticMetamodel(Turno.class)
public class Turno_ { 

    public static volatile SingularAttribute<Turno, Integer> hora;
    public static volatile SingularAttribute<Turno, Integer> anden;
    public static volatile SingularAttribute<Turno, Integer> id;
    public static volatile SingularAttribute<Turno, Vehiculo> vehiculo;
    public static volatile SingularAttribute<Turno, Integer> dia;

}