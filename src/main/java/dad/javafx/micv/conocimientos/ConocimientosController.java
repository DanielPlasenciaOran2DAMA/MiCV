package dad.javafx.micv.conocimientos;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import dad.javafx.micv.App;
import dad.javafx.micv.model.Conocimientos;
import dad.javafx.micv.model.Idioma;
import dad.javafx.micv.model.Nivel;
import dad.javafx.micv.utils.Mensajes;
import javafx.beans.Observable;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ConocimientosController implements Initializable {

	@FXML
	private HBox view;

	@FXML
	private TableView<Conocimientos> conocimientosTable;

	@FXML
	private TableColumn<Conocimientos, String> denominacionColumn;

	@FXML
	private TableColumn<Conocimientos, Nivel> nivelColumn;

	@FXML
	private Button añadirConocimientoButton;

	@FXML
	private Button añadirIdiomaButton;

	@FXML
	private Button eliminarConocimientoButton;

	// MODEL
	private ListProperty<Conocimientos> conocimientoList = new SimpleListProperty<>(
			FXCollections.observableArrayList());
	private ObjectProperty<Conocimientos> conocimientoSeleccionado = new SimpleObjectProperty<>();

	public ConocimientosController() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ConocimientosView.fxml"));
		loader.setController(this);
		loader.load();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		conocimientoList.addListener((o, ov, nv) -> onConocimientosListener(o, ov, nv));
		conocimientoSeleccionado.bind(conocimientosTable.getSelectionModel().selectedItemProperty());

		// FACTORIES
		denominacionColumn.setCellValueFactory(value -> value.getValue().denominacionProperty());
		denominacionColumn.setCellFactory(TextFieldTableCell.forTableColumn());

		nivelColumn.setCellValueFactory(value -> value.getValue().nivelProperty());
		nivelColumn.setCellFactory(ComboBoxTableCell.forTableColumn(Nivel.values()));

	}

	private void onConocimientosListener(Observable o, ObservableList<Conocimientos> ov,
			ObservableList<Conocimientos> nv) {
		conocimientosTable.itemsProperty().bindBidirectional(conocimientoList);
	}

	@FXML
	void onAñadirConoAction(ActionEvent event) {
		Dialog<Conocimientos> dialog = new Dialog<>();
		dialog.setTitle("Nuevo conocimiento");

		// PONER ICONO DEL PADRE
		Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
		stage.getIcons().setAll(App.getPrimaryStage().getIcons());

		// BOTONES DIALOGO
		ButtonType addButton = new ButtonType("Crear", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

		// ELEMENTOS DIALOGO PERSONALIZADO
		GridPane root = new GridPane();
		root.setHgap(10);
		root.setVgap(10);
		root.setPadding(new Insets(20, 150, 10, 10));

		TextField denominacionText = new TextField();

		ComboBox<Nivel> nivelCombo = new ComboBox<>();
		nivelCombo.getItems().addAll(Nivel.BASICO, Nivel.MEDIO, Nivel.AVANZADO);

		Button quitarNivel = new Button("X");

		HBox nivelHbox = new HBox(5, nivelCombo, quitarNivel);
		root.add(new Label("Denominación"), 0, 0);
		root.add(denominacionText, 1, 0);
		root.add(new Label("Nivel"), 0, 1);
		root.add(nivelHbox, 1, 1);

		quitarNivel.setOnAction(e -> onQuitarNivelAction(nivelCombo));

		dialog.getDialogPane().setContent(root);

		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == addButton) {
				return new Conocimientos(denominacionText.getText(), nivelCombo.getSelectionModel().getSelectedItem());
			}
			return null;
		});

		Optional<Conocimientos> result = dialog.showAndWait();
		if (dialog.getResult() != null) {
			conocimientoList.add(result.get());
		}
	}

	@FXML
	void onAñadirIdioAction(ActionEvent event) {
		Dialog<Idioma> dialog = new Dialog<>();
		dialog.setTitle("Nuevo conocimiento");

		// PONER ICONO DEL PADRE
		Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
		stage.getIcons().setAll(App.getPrimaryStage().getIcons());

		// BOTONES DIALOGO
		ButtonType addButton = new ButtonType("Crear", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

		// ELEMENTOS DIALOGO PERSONALIZADO
		GridPane root = new GridPane();
		root.setHgap(10);
		root.setVgap(10);
		root.setPadding(new Insets(20, 150, 10, 10));

		TextField denominacionText = new TextField();
		TextField certificacionText = new TextField();

		ComboBox<Nivel> nivelCombo = new ComboBox<>();
		nivelCombo.getItems().addAll(Nivel.BASICO, Nivel.MEDIO, Nivel.AVANZADO);

		Button quitarNivel = new Button("X");

		HBox nivelHbox = new HBox(5, nivelCombo, quitarNivel);
		root.add(new Label("Denominación"), 0, 0);
		root.add(denominacionText, 1, 0);
		root.add(new Label("Nivel"), 0, 1);
		root.add(nivelHbox, 1, 1);
		root.add(new Label("Certificación"), 0, 2);
		root.add(certificacionText, 1, 2);

		quitarNivel.setOnAction(e -> onQuitarNivelAction(nivelCombo));

		dialog.getDialogPane().setContent(root);

		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == addButton) {
				return new Idioma(denominacionText.getText(), nivelCombo.getSelectionModel().getSelectedItem(),
						certificacionText.getText());
			}
			return null;
		});

		Optional<Idioma> result = dialog.showAndWait();
		if (dialog.getResult() != null) {
			conocimientoList.add(result.get());
		}
	}

	@FXML
	void onEliminarConoAction(ActionEvent event) {
		Conocimientos conocimiento = conocimientoSeleccionado.get();
		Optional<ButtonType> result = Mensajes.confirmacion("Eliminar titulo",
				"¿Esta seguro de que quiere eliminar este conocimiento " + conocimiento.getDenominacion() + ", "
						+ conocimiento.getNivel() + "?");
		if (result.get() == ButtonType.OK) {
			conocimientoList.remove(conocimiento);
		}
	}

	private void onQuitarNivelAction(ComboBox<Nivel> nivelCombo) {
		nivelCombo.getSelectionModel().clearSelection();
	}

	public HBox getView() {
		return view;
	}

	public final ListProperty<Conocimientos> conocimientoListProperty() {
		return this.conocimientoList;
	}

	public final ObservableList<Conocimientos> getConocimientoList() {
		return this.conocimientoListProperty().get();
	}

	public final void setConocimientoList(final ObservableList<Conocimientos> conocimientoList) {
		this.conocimientoListProperty().set(conocimientoList);
	}

}