package com.barbaro.apijuguetes.models;



import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

//Esto es una entidad
//Clase - tabla
@Entity
public class Juguete {

	// Es la llave primaria
	@Id
	// Generar automaticamente
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	// Una columna de la tabla
	@Column(name = "nombre")
	private String nombre;
	
	@Column(name = "descripcion")
    private String descripcion;
    
    @Column(name = "precio")
    private float precio;
   
    @JsonFormat
    (shape = Shape.STRING, pattern = "yyy-MM-dd HH:mm:ss")
    @Column(name = "fecha")
    private Date fecha;
    
 // Es para JPA
    public Juguete() {
        
    }
    
    public Juguete(int id, String nombre, String descripcion, 
            float precio, Date fecha) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.fecha = fecha;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
