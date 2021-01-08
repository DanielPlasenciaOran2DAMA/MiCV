package dad.javafx.micv.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Idioma extends Conocimientos {
	private StringProperty certificacion = new SimpleStringProperty();

	public Idioma(String denominacion, Nivel nivel, String certificacion) {
		super(denominacion, nivel);
		this.certificacion.set(certificacion);
	}

	public Idioma() {
		super();
	}

	public final StringProperty certificacionProperty() {
		return this.certificacion;
	}

	public final String getCertificacion() {
		return this.certificacionProperty().get();
	}

	public final void setCertificacion(final String certificacion) {
		this.certificacionProperty().set(certificacion);
	}
}
