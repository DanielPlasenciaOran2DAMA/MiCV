package dad.javafx.micv;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import com.google.gson.JsonSyntaxException;

import dad.javafx.micv.conocimientos.ConocimientosController;
import dad.javafx.micv.contacto.ContactoController;
import dad.javafx.micv.experiencia.ExperienciaController;
import dad.javafx.micv.formacion.FormacionController;
import dad.javafx.micv.model.CV;
import dad.javafx.micv.personal.PersonalController;
import dad.javafx.micv.utils.JSONUtils;
import dad.javafx.micv.utils.Mensajes;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class MainController implements Initializable {

	// controllers

	private PersonalController personalController = new PersonalController();
	private ConocimientosController conocimientosController = new ConocimientosController();
	private ContactoController contactoController = new ContactoController();
	private ExperienciaController experienciaController = new ExperienciaController();
	private FormacionController formacionController = new FormacionController();

	// model

	private ObjectProperty<CV> cv = new SimpleObjectProperty<>();
	private File fichero = null;

	// view

	@FXML
	private BorderPane view;

	@FXML
	private TabPane cvTabPane;

	@FXML
	private Tab personalTab;

	@FXML
	private Tab contactoTab;

	@FXML
	private Tab formacionTab;

	@FXML
	private Tab experienciaTab;

	@FXML
	private Tab conocimientosTab;

	public MainController() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"));
		loader.setController(this);
		loader.load();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		personalTab.setContent(personalController.getView());
		contactoTab.setContent(contactoController.getView());
		formacionTab.setContent(formacionController.getView());
		experienciaTab.setContent(experienciaController.getView());
		conocimientosTab.setContent(conocimientosController.getView());

		cv.addListener((o, ov, nv) -> onCVChanged(o, ov, nv));

		cv.set(new CV());
	}

	private void onCVChanged(ObservableValue<? extends CV> o, CV ov, CV nv) {
		personalController.setModel(nv.getPersonal());

		if (nv.getPersonal() != null && nv.getPersonal().getPais().size() != 0) {
			personalController.setPaisSeleccionado(nv.getPersonal().getPais().get(0));
		}

		contactoController.setModel(nv.getContacto());
		formacionController.setFormacionList(nv.getFormacion());
		experienciaController.setExperienciaList(nv.getExperiencias());
		conocimientosController.setConocimientoList(nv.getHabilidades());

	}

	@FXML
	void onNuevoAction(ActionEvent event) {
		System.out.println("Nuevo");
		cv.set(new CV());
	}

	@FXML
	void onAbrirAction(ActionEvent event) {

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Abrir un currículum");
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Currículum (*.cv)", "*.cv"));
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Todos los archivos (*.*)", "*.*"));
		fichero = fileChooser.showOpenDialog(App.getPrimaryStage());
		if (fichero != null) {
			try {
				cv.set(JSONUtils.fromJson(fichero, CV.class));
				Mensajes.info("Se ha abierto el fichero " + fichero.getName() + " correctamente.", "Datos cargados");
			} catch (JsonSyntaxException | IOException e) {
				Mensajes.error("Ha ocurrido un error al abrir " + fichero, e.getMessage());
			}
		}
	}

	@FXML
	void onGuardarAction(ActionEvent event) {
		try {
			if (fichero != null) {
				JSONUtils.toJson(fichero, cv.get());
			} else {
				guardarFichero();
			}
		} catch (Exception e) {
			Mensajes.error("Error al guardar el CV", e.toString());
		}
	}

	@FXML
	void onGuardarComoAction(ActionEvent event) {
		guardarFichero();
	}

	private void guardarFichero() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Guardar un currículum");
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Currículum (*.cv)", "*.cv"));
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Todos los archivos (*.*)", "*.*"));
		fichero = fileChooser.showSaveDialog(App.getPrimaryStage());
		if (fichero != null) {
			try {
				JSONUtils.toJson(fichero, cv.get());
			} catch (JsonSyntaxException | IOException e) {
				Mensajes.error("Ha ocurrido un error al guardar " + fichero, e.getMessage());
			}
		}
	}

	@FXML
	void onSalirAction(ActionEvent event) {
		Optional<ButtonType> result = Mensajes.confirmacion("Salir de la aplicación",
				"¿Está seguro de que desea salir de la aplicación?");
		if (result.get() == ButtonType.OK) {
			App.getPrimaryStage().close();
		}
	}

	public BorderPane getView() {
		return view;
	}
}