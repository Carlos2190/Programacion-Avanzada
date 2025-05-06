/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

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

/**
 *
 * @author Carlos Espinoza
 */
@Entity
public class Cliente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idClie")
    private int idClie;
    @Basic
    private String Cedula;
    private String Nombres;
    private String Apellidos;
    
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<Celular> celular; 

    public Cliente() {
        this.celular = new ArrayList<>();
    }

    public Cliente(int idClie, String Cedula, String Nombres, String Apellidos, List<Celular> celular) {
        this.idClie = idClie;
        this.Cedula = Cedula;
        this.Nombres = Nombres;
        this.Apellidos = Apellidos;
        if (celular == null) {
            this.celular = new ArrayList<>();
        } else {
            this.celular = celular;
        }
    }

    public int getIdClie() {
        return idClie;
    }

    public void setIdClie(int idClie) {
        this.idClie = idClie;
    }

    public String getCedula() {
        return Cedula;
    }

    public void setCedula(String Cedula) {
        this.Cedula = Cedula;
    }

    public String getNombres() {
        return Nombres;
    }

    public void setNombres(String Nombres) {
        this.Nombres = Nombres;
    }

    public String getApellidos() {
        return Apellidos;
    }

    public void setApellidos(String Apellidos) {
        this.Apellidos = Apellidos;
    }

    public List<Celular> getCelular() {
        return celular;
    }

    public void setCelular(List<Celular> celular) {
        this.celular = celular;
    }

}
