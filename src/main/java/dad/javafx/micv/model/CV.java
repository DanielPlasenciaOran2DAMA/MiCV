package dad.javafx.micv.model;

import java.util.List;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CV {
	private ObjectProperty<Personal> personal = new SimpleObjectProperty<>(new Personal());
	private ObjectProperty<Contacto> contacto = new SimpleObjectProperty<>(new Contacto());
	private ListProperty<Conocimientos> habilidades = new SimpleListProperty<>(FXCollections.observableArrayList());
	private ListProperty<Experiencia> experiencias = new SimpleListProperty<>(FXCollections.observableArrayList());
	private ListProperty<Formacion> formacion = new SimpleListProperty<>(FXCollections.observableArrayList());

	public CV(Personal personal, Contacto contacto, List<Conocimientos> habilidades, List<Experiencia> experiencias,
			List<Formacion> formacion) {
		this.personal.set(personal);
		this.contacto.set(contacto);
		this.habilidades.addAll(habilidades);
		this.experiencias.addAll(experiencias);
		this.formacion.addAll(formacion);
	}

	public CV() {
	}

	public final ObjectProperty<Personal> personalProperty() {
		return this.personal;
	}

	public final Personal getPersonal() {
		return this.personalProperty().get();
	}

	public final void setPersonal(final Personal personal) {
		this.personalProperty().set(personal);
	}

	public final ObjectProperty<Contacto> contactoProperty() {
		return this.contacto;
	}

	public final Contacto getContacto() {
		return this.contactoProperty().get();
	}

	public final void setContacto(final Contacto contacto) {
		this.contactoProperty().set(contacto);
	}

	public final ListProperty<Conocimientos> habilidadesProperty() {
		return this.habilidades;
	}

	public final ObservableList<Conocimientos> getHabilidades() {
		return this.habilidadesProperty().get();
	}

	public final void setHabilidades(final ObservableList<Conocimientos> habilidades) {
		this.habilidadesProperty().set(habilidades);
	}

	public final ListProperty<Experiencia> experienciasProperty() {
		return this.experiencias;
	}

	public final ObservableList<Experiencia> getExperiencias() {
		return this.experienciasProperty().get();
	}

	public final void setExperiencias(final ObservableList<Experiencia> experiencias) {
		this.experienciasProperty().set(experiencias);
	}

	public final ListProperty<Formacion> formacionProperty() {
		return this.formacion;
	}

	public final ObservableList<Formacion> getFormacion() {
		return this.formacionProperty().get();
	}

	public final void setFormacion(final ObservableList<Formacion> formacion) {
		this.formacionProperty().set(formacion);
	}

}
