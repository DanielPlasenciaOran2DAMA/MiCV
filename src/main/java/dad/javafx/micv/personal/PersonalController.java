package dad.javafx.micv.personal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import dad.javafx.micv.App;
import dad.javafx.micv.model.Nacionalidad;
import dad.javafx.micv.model.Personal;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class PersonalController implements Initializable {

	private final String PATH = "./src/main/resources/";

	@FXML
	private GridPane view;

	@FXML
	private TextField dniText;

	@FXML
	private TextField nombreText;

	@FXML
	private TextField apellidosText;

	@FXML
	private TextField localidadText;

	@FXML
	private TextField codPostalText;

	@FXML
	private ComboBox<String> paisCombo;

	@FXML
	private ListView<Nacionalidad> nacionalidad;

	@FXML
	private DatePicker fechaNacDate;

	@FXML
	private TextArea direccionTextArea;

	@FXML
	private Button anadirButton;

	@FXML
	private Button quitarButton;

	// MODEL
	private ObjectProperty<Personal> model = new SimpleObjectProperty<>(new Personal());

	private StringProperty paisSeleccionado = new SimpleStringProperty();
	private ObjectProperty<Nacionalidad> nacionalidadSelecionada = new SimpleObjectProperty<>();

	public PersonalController() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PersonalView.fxml"));
		loader.setController(this);
		loader.load();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		paisCombo.itemsProperty().bindBidirectional(model.get().paisProperty());
		readCSVPaises();

		nacionalidadSelecionada.bind(nacionalidad.getSelectionModel().selectedItemProperty());
		paisCombo.valueProperty().bindBidirectional(paisSeleccionado);

		model.addListener((o, ov, nv) -> personalListener(o, ov, nv));
	}

	private void personalListener(Observable o, Personal ov, Personal nv) {
		if (ov != null) {
			dniText.textProperty().unbindBidirectional(ov.identificacionProperty());
			nombreText.textProperty().unbindBidirectional(ov.nombreProperty());
			apellidosText.textProperty().unbindBidirectional(ov.apellidosProperty());
			localidadText.textProperty().unbindBidirectional(ov.localidadProperty());
			codPostalText.textProperty().unbindBidirectional(ov.codigoPostalProperty());
			direccionTextArea.textProperty().unbindBidirectional(ov.direccionProperty());
			fechaNacDate.valueProperty().unbindBidirectional(ov.fechaNacimientoProperty());
			paisCombo.itemsProperty().unbindBidirectional(ov.paisProperty());
			nacionalidad.itemsProperty().unbindBidirectional(ov.nacionalidadesProperty());
		}

		dniText.textProperty().bindBidirectional(nv.identificacionProperty());
		nombreText.textProperty().bindBidirectional(nv.nombreProperty());
		apellidosText.textProperty().bindBidirectional(nv.apellidosProperty());
		localidadText.textProperty().bindBidirectional(nv.localidadProperty());
		codPostalText.textProperty().bindBidirectional(nv.codigoPostalProperty());
		direccionTextArea.textProperty().bindBidirectional(nv.direccionProperty());
		fechaNacDate.valueProperty().bindBidirectional(nv.fechaNacimientoProperty());
		nacionalidad.itemsProperty().bindBidirectional(nv.nacionalidadesProperty());
	}

	@FXML
	void onAnadirButtonAction(ActionEvent event) {
		List<String> nacionalidades = readCSVNacionalidades();

		ChoiceDialog<String> dialog = new ChoiceDialog<>(nacionalidades.get(0), nacionalidades);
		dialog.setTitle("Nueva nacionalidad");
		dialog.setHeaderText("Añadir nacionalidad");
		dialog.setContentText("Seleccione una nacionalidad:");

		Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
		stage.getIcons().setAll(App.getPrimaryStage().getIcons());

		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			System.out.println(result.get());
			model.get().getNacionalidades().add(new Nacionalidad(result.get())); // TODO
		}
	}

	@FXML
	void onQuitarButtonAction(ActionEvent event) {
		model.get().getNacionalidades().remove(nacionalidadSelecionada.get());
	}

	public List<String> readCSVNacionalidades() {
		List<String> nacionalidades = new ArrayList<>();
		try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(new FileInputStream(new File(PATH + "nacionalidades.csv")), "UTF-8"));
			String linea;
			while ((linea = br.readLine()) != null) {
				nacionalidades.add(linea);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nacionalidades;
	}

	private void readCSVPaises() {
		try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(new FileInputStream(new File(PATH + "paises.csv")), "UTF-8"));

			String linea;
			while ((linea = br.readLine()) != null) {
				model.get().getPais().add(linea);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public GridPane getView() {
		return view;
	}

	public final ObjectProperty<Personal> modelProperty() {
		return this.model;
	}

	public final Personal getModel() {
		return this.modelProperty().get();
	}

	public final void setModel(final Personal model) {
		this.modelProperty().set(model);
	}

	public final StringProperty paisSeleccionadoProperty() {
		return this.paisSeleccionado;
	}

	public final String getPaisSeleccionado() {
		return this.paisSeleccionadoProperty().get();
	}

	public final void setPaisSeleccionado(final String paisSeleccionado) {
		this.paisSeleccionadoProperty().set(paisSeleccionado);
	}
}
