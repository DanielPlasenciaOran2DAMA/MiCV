package dad.javafx.micv.experiencia;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

import dad.javafx.micv.App;
import dad.javafx.micv.model.Experiencia;
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

public class ExperienciaController implements Initializable {

	@FXML
	private HBox view;

	@FXML
	private TableView<dad.javafx.micv.model.Experiencia> experienciaTable;

	@FXML
	private TableColumn<Experiencia, LocalDate> desdeColumn;

	@FXML
	private TableColumn<Experiencia, LocalDate> hastaColumn;

	@FXML
	private TableColumn<Experiencia, String> denominacionColumn;

	@FXML
	private TableColumn<Experiencia, String> empleadorColumn;

	@FXML
	private Button añadirExperienciaButton;

	@FXML
	private Button eliminarExperienciaButton;

	private ListProperty<Experiencia> experienciaList = new SimpleListProperty<>(FXCollections.observableArrayList());
	private ObjectProperty<Experiencia> experienciaSeleccionada = new SimpleObjectProperty<>();

	public ExperienciaController() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ExperienciaView.fxml"));
		loader.setController(this);
		loader.load();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		experienciaList.addListener((o, ov, nv) -> onExperienciaListener(o, ov, nv));
		experienciaSeleccionada.bind(experienciaTable.getSelectionModel().selectedItemProperty());

		denominacionColumn.setCellValueFactory(value -> value.getValue().denominacionProperty());
		denominacionColumn.setCellFactory(TextFieldTableCell.forTableColumn());

		empleadorColumn.setCellValueFactory(value -> value.getValue().empleadorProperty());
		empleadorColumn.setCellFactory(TextFieldTableCell.forTableColumn());

		desdeColumn.setCellValueFactory(value -> value.getValue().desdeProperty());
		desdeColumn.setCellFactory(DatePickerTableCell.forTableColumn());

		hastaColumn.setCellValueFactory(value -> value.getValue().hastaProperty());
		hastaColumn.setCellFactory(DatePickerTableCell.forTableColumn());
	}

	private void onExperienciaListener(Observable o, ObservableList<Experiencia> ov, ObservableList<Experiencia> nv) {
		experienciaTable.itemsProperty().bindBidirectional(experienciaList);
	}

	@FXML
	void onAñadirExpAction(ActionEvent event) {
		Dialog<Experiencia> dialog = new Dialog<>();
		dialog.setTitle("Nuevo experiencia");
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
		TextField empleadorText = new TextField();
		DatePicker desdeDate = new DatePicker();
		DatePicker hastaDate = new DatePicker();

		root.add(new Label("Denominación"), 0, 0);
		root.add(denominacionText, 1, 0);
		root.add(new Label("Empleador"), 0, 1);
		root.add(empleadorText, 1, 1);
		root.add(new Label("Desde"), 0, 2);
		root.add(desdeDate, 1, 2);
		root.add(new Label("Hasta"), 0, 3);
		root.add(hastaDate, 1, 3);

		dialog.getDialogPane().setContent(root);

		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == addButton) {
				return new Experiencia(desdeDate.getValue(), hastaDate.getValue(), denominacionText.getText(),
						empleadorText.getText());
			}
			return null;
		});

		Optional<Experiencia> result = dialog.showAndWait();
		if (dialog.getResult() != null) {
			experienciaList.add(result.get());
		}
	}

	@FXML
	void onEliminarExpAction(ActionEvent event) {
		Experiencia experienicia = experienciaSeleccionada.get();
		Optional<ButtonType> result = Mensajes.confirmacion("Eliminar titulo",
				"¿Esta seguro de que quiere eliminar esta experiencia " + experienicia.getDenominacion() + ", "
						+ experienicia.getEmpleador() + ", " + experienicia.getDesde() + ", " + experienicia.getHasta()
						+ "?");
		if (result.get() == ButtonType.OK) {
			experienciaList.remove(experienicia);
		}
	}

	public HBox getView() {
		return view;
	}

	public final ListProperty<Experiencia> experienciaListProperty() {
		return this.experienciaList;
	}

	public final ObservableList<Experiencia> getExperienciaList() {
		return this.experienciaListProperty().get();
	}

	public final void setExperienciaList(final ObservableList<Experiencia> experienciaList) {
		this.experienciaListProperty().set(experienciaList);
	}

}