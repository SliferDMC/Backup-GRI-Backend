package co.edu.uniquindio.gri.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity(name = "JOVEN_INVES")
@Table(name = "JOVEN_INVES", schema = "gri")
public class Joven implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ID")
	private long id;

	@Column(name = "CEDULA")
	private String cedula;
	
	@Column(name = "NOMBRE")
	private String nombre;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FACULTADES_ID")
	private Facultad facultad;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "PROGRAMA_EGRESO_ID")
	private Programa programaEgreso;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "PROGRAMA_GRUPO_ID")
	private Programa programaGrupo;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "GRUPO_ID")
	private Grupo grupo;
	
	@Column(name = "PROPUESTA")
	private String propuesta;
	
	@Column(name = "VINCULACION")
	private String vinculacion;
	
	@Column(name = "FINANCIADORA")
	private String financiadora;
	
	public Joven(long id, String cedula, String nombre, Facultad facultad, Programa programaEgreso, 
			Programa programaGrupo, Grupo grupo, String propuesta, String vinculacion, String financiadora) {
		super();
		this.id = id;
		this.cedula = cedula;
		this.nombre = nombre;
		this.facultad = facultad;
		this.programaEgreso = programaEgreso;
		this.programaGrupo = programaGrupo;
		this.grupo = grupo;
		this.propuesta = propuesta;
		this.vinculacion = vinculacion;
		this.financiadora = financiadora;
		
	}

	public Joven() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCedula() {
		return cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Facultad getFacultad() {
		return facultad;
	}

	public void setFacultad(Facultad facultad) {
		this.facultad = facultad;
	}

	public Programa getProgramaEgreso() {
		return programaEgreso;
	}

	public void setProgramaEgreso(Programa programaEgreso) {
		this.programaEgreso = programaEgreso;
	}

	public Programa getProgramaGrupo() {
		return programaGrupo;
	}

	public void setProgramaGrupo(Programa programaGrupo) {
		this.programaGrupo = programaGrupo;
	}

	public Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}

	public String getPropuesta() {
		return propuesta;
	}

	public void setPropuesta(String propuesta) {
		this.propuesta = propuesta;
	}

	public String getVinculacion() {
		return vinculacion;
	}

	public void setVinculacion(String vinculacion) {
		this.vinculacion = vinculacion;
	}

	public String getFinanciadora() {
		return financiadora;
	}

	public void setFinanciadora(String financiadora) {
		this.financiadora = financiadora;
	}
	
}
