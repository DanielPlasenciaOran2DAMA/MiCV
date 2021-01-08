package dad.javafx.micv.model;

import java.util.ArrayList;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Contacto {
	private ListProperty<Telefono> telefonos = new SimpleListProperty<>(
			FXCollections.observableArrayList(new ArrayList<>()));
	private ListProperty<Correo> emails = new SimpleListProperty<>(
			FXCollections.observableArrayList(new ArrayList<>()));
	private ListProperty<Webs> webs = new SimpleListProperty<>(FXCollections.observableArrayList(new ArrayList<>()));

	public Contacto() {
	}

	// ARRAYlIST POR QUE CREAMOS EL FXCOLLECTIONS INICIALIZADO CON ARRAYLIST
	public Contacto(ArrayList<Telefono> telefonos, ArrayList<Correo> emails, ArrayList<Webs> webs) {
		this.telefonos.addAll(telefonos);
		this.emails.addAll(emails);
		this.webs.addAll(webs);
	}

	public final ListProperty<Telefono> telefonosProperty() {
		return this.telefonos;
	}

	public final ObservableList<Telefono> getTelefonos() {
		return this.telefonosProperty().get();
	}

	public final void setTelefonos(final ObservableList<Telefono> telefonos) {
		this.telefonosProperty().set(telefonos);
	}

	public final ListProperty<Correo> emailsProperty() {
		return this.emails;
	}

	public final ObservableList<Correo> getEmails() {
		return this.emailsProperty().get();
	}

	public final void setEmails(final ObservableList<Correo> emails) {
		this.emailsProperty().set(emails);
	}

	public final ListProperty<Webs> websProperty() {
		return this.webs;
	}

	public final ObservableList<Webs> getWebs() {
		return this.websProperty().get();
	}

	public final void setWebs(final ObservableList<Webs> webs) {
		this.websProperty().set(webs);
	}

}
