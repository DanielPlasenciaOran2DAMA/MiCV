package dad.javafx.micv.formacion;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

import dad.javafx.micv.App;
import dad.javafx.micv.model.Formacion;
import dad.javafx.micv.utils.DatePickerTableCell;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class FormacionController implements Initializable {

	@FXML
	private HBox view;

	@FXML
	private TableView<Formacion> formacionTable;

	@FXML
	private TableColumn<Formacion, LocalDate> desdeColumn;

	@FXML
	private TableColumn<Formacion, LocalDate> hastaColumn;

	@FXML
	private TableColumn<Formacion, String> denominacionColumn;

	@FXML
	private TableColumn<Formacion, String> organizadorColumn;

	@FXML
	private Button addFormacionButton;

	@FXML
	private Button eliminarFormacionButton;

	private ListProperty<Formacion> formacionList = new SimpleListProperty<>(FXCollections.observableArrayList());
	private ObjectProperty<Formacion> tituloSeleccionado = new SimpleObjectProperty<>();

	public FormacionController() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/FormacionView.fxml"));
		loader.setController(this);
		loader.load();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		formacionList.addListener((o, ov, nv) -> onFormacionListener(o, ov, nv));

		//tituloSeleccionado.bind(formacionTable.getSelectionModel().selectedItemProperty());

		denominacionColumn.setCellValueFactory(value -> value.getValue().denominacionProperty());
		denominacionColumn.setCellFactory(TextFieldTableCell.forTableColumn());

		organizadorColumn.setCellValueFactory(value -> value.getValue().organizadorProperty());
		organizadorColumn.setCellFactory(TextFieldTableCell.forTableColumn());

		desdeColumn.setCellValueFactory(value -> value.getValue().desdeProperty());
		desdeColumn.setCellFactory(DatePickerTableCell.forTableColumn());

		hastaColumn.setCellValueFactory(value -> value.getValue().hastaProperty());
		hastaColumn.setCellFactory(DatePickerTableCell.forTableColumn());
	}

	private void onFormacionListener(Observable o, ObservableList<Formacion> ov, ObservableList<Formacion> nv) {
		formacionTable.itemsProperty().bindBidirectional(formacionList);
	}

	@FXML
	void onAñadirFormAction(ActionEvent event) {
		Dialog<Formacion> dialog = new Dialog<>();
		dialog.setTitle("Nuevo título");
		dialog.setHeaderText("");

		Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
		stage.getIcons().setAll(App.getPrimaryStage().getIcons());

		ButtonType addButton = new ButtonType("Crear", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

		GridPane root = new GridPane();
		root.setHgap(10);
		root.setVgap(10);
		root.setPadding(new Insets(20, 150, 10, 10));

		TextField denominacionText = new TextField();
		TextField organizadorText = new TextField();
		DatePicker desdeDate = new DatePicker();
		DatePicker hastaDate = new DatePicker();

		root.add(new Label("Denominación"), 0, 0);
		root.add(denominacionText, 1, 0);
		root.add(new Label("Organizador"), 0, 1);
		root.add(organizadorText, 1, 1);
		root.add(new Label("Desde"), 0, 2);
		root.add(desdeDate, 1, 2);
		root.add(new Label("Hasta"), 0, 3);
		root.add(hastaDate, 1, 3);

		dialog.getDialogPane().setContent(root);

		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == addButton) {
				return new Formacion(desdeDate.getValue(), hastaDate.getValue(), denominacionText.getText(),
						organizadorText.getText());
			}
			return null;
		});

		Optional<Formacion> result = dialog.showAndWait();
		if (dialog.getResult() != null) {
			formacionList.add(result.get());
		}
	}

	@FXML
	void onEliminarFormAction(ActionEvent event) {
		Formacion titulo = tituloSeleccionado.get();
		Optional<ButtonType> result = Mensajes.confirmacion("Eliminar titulo",
				"¿Esta seguro de que quiere eliminar esta formación " + titulo.getDenominacion() + ", "
						+ titulo.getOrganizador() + ", " + titulo.getDesde() + ", " + titulo.getHasta() + "?");
		if (result.get() == ButtonType.OK) {
			formacionList.remove(titulo);
		}
	}

	public HBox getView() {
		return view;
	}

	public final ListProperty<Formacion> formacionListProperty() {
		return this.formacionList;
	}

	public final ObservableList<Formacion> getFormacionList() {
		return this.formacionListProperty().get();
	}

	public final void setFormacionList(final ObservableList<Formacion> formacionList) {
		this.formacionListProperty().set(formacionList);
	}

}
