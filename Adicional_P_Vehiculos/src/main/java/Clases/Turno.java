package Clases;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Turno implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Basic
    private int anden;
    private int dia;
    private int hora;

    @ManyToOne
    private Vehiculo vehiculo;

    public Turno() {
    }

    public Turno(int id, int anden, int dia, int hora, Vehiculo vehiculo) {
        this.id = id;
        this.anden = anden;
        this.dia = dia;
        this.hora = hora;
        this.vehiculo = vehiculo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getAnden() {
        return anden;
    }

    public void setAnden(int anden) {
        this.anden = anden;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public int getHora() {
        return hora;
    }

    public void setHora(int hora) {
        this.hora = hora;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }


    
    
    
    
}