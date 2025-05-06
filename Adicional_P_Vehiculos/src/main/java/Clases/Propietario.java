package Clases;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Propietario implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDPROP")
    private Integer idprop;
    
    @Basic
    private String cedula;
    private String apellido;
    private String nombre;


    @OneToMany(mappedBy = "propietario", cascade = CascadeType.ALL)
    private List<Vehiculo> vehiculo; // sin 's'

    // Constructor sin argumentos que inicializa la lista
    public Propietario() {
        this.vehiculo = new ArrayList<>();
    }

    // Constructor con argumentos
    public Propietario(String cedula, String apellido, String nombre, List<Vehiculo> vehiculo) {
        this.cedula = cedula;
        this.apellido = apellido;
        this.nombre = nombre;
        if (vehiculo == null) {
            this.vehiculo = new ArrayList<>();
        } else {
            this.vehiculo = vehiculo;
        }
    }

    // Getter y setter
    public Integer getIdprop() {
        return idprop;
    }

    public void setIdprop(Integer idprop) {
        this.idprop = idprop;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Vehiculo> getVehiculo() { 
        return vehiculo;
    }

    public void setVehiculo(List<Vehiculo> vehiculo) { 
        this.vehiculo = vehiculo;
    }
}