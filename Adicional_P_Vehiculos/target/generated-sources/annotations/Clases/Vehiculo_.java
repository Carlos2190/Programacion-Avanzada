package Clases;

import Clases.Propietario;
import Clases.Turno;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2025-05-05T21:06:07", comments="EclipseLink-2.7.12.v20230209-rNA")
@StaticMetamodel(Vehiculo.class)
public class Vehiculo_ { 

    public static volatile SingularAttribute<Vehiculo, String> marca;
    public static volatile SingularAttribute<Vehiculo, String> estado;
    public static volatile SingularAttribute<Vehiculo, Propietario> propietario;
    public static volatile ListAttribute<Vehiculo, Turno> turnos;
    public static volatile SingularAttribute<Vehiculo, Integer> id;
    public static volatile SingularAttribute<Vehiculo, String> placa;

}