package dad.javafx.micv.contacto;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import dad.javafx.micv.App;
import dad.javafx.micv.model.Contacto;
import dad.javafx.micv.model.Correo;
import dad.javafx.micv.model.Telefono;
import dad.javafx.micv.model.TipoTelefono;
import dad.javafx.micv.model.Webs;
import dad.javafx.micv.utils.Mensajes;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
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
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ContactoController implements Initializable {

	@FXML
	private SplitPane view;

	@FXML
	private TableView<Telefono> telefonoTable;

	@FXML
	private TableColumn<Telefono, String> numeroColumn;

	@FXML
	private TableColumn<Telefono, TipoTelefono> tipoColumn;

	@FXML
	private Button addTelefoButton;

	@FXML
	private Button deleTelefoButton;

	@FXML
	private TableView<Correo> emailTable;

	@FXML
	private TableColumn<Correo, String> emailColumn;

	@FXML
	private Button addEmailButton;

	@FXML
	private Button deleteEmailButton;

	@FXML
	private TableView<Webs> websTable;

	@FXML
	private TableColumn<Webs, String> urlColumn;

	@FXML
	private Button addWebsButton;

	@FXML
	private Button deleteWebsButton;

	// MODEL
	private ObjectProperty<Contacto> model = new SimpleObjectProperty<>();
	private ObjectProperty<Telefono> telefonoSeleccionado = new SimpleObjectProperty<>();
	private ObjectProperty<Correo> emailSeleccionado = new SimpleObjectProperty<>();
	private ObjectProperty<Webs> webSeleccionada = new SimpleObjectProperty<>();

	public ContactoController() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ContactoView.fxml"));
		loader.setController(this);
		loader.load();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		model.addListener((o, ov, nv) -> onContactoListener(o, ov, nv));

		// SELECTED
		telefonoSeleccionado.bind(telefonoTable.getSelectionModel().selectedItemProperty());
		emailSeleccionado.bind(emailTable.getSelectionModel().selectedItemProperty());
		webSeleccionada.bind(websTable.getSelectionModel().selectedItemProperty());

		// FACTORIES
		numeroColumn.setCellValueFactory(value -> value.getValue().numeroProperty());
		numeroColumn.setCellFactory(TextFieldTableCell.forTableColumn());

		tipoColumn.setCellValueFactory(value -> value.getValue().tipoTelefonoProperty());
		tipoColumn.setCellFactory(ComboBoxTableCell.forTableColumn(TipoTelefono.values()));

		emailColumn.setCellValueFactory(value -> value.getValue().direccionProperty());
		emailColumn.setCellFactory(TextFieldTableCell.forTableColumn());

		urlColumn.setCellValueFactory(value -> value.getValue().urlProperty());
		urlColumn.setCellFactory(TextFieldTableCell.forTableColumn());
	}

	private void onContactoListener(Observable o, Contacto ov, Contacto nv) {
		telefonoTable.itemsProperty().bindBidirectional(nv.telefonosProperty());
		emailTable.itemsProperty().bindBidirectional(nv.emailsProperty());
		websTable.itemsProperty().bindBidirectional(nv.websProperty());
	}

	@FXML
	void onAddEmailButtonAction(ActionEvent event) {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Nuevo e-mail");
		dialog.setHeaderText("Crear una nueva direccion de correo.");
		dialog.setContentText("E-mail:");

		// PONER ICONO DEL PADRE
		Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
		stage.getIcons().setAll(App.getPrimaryStage().getIcons());

		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			model.get().getEmails().add(new Correo(result.get()));
		}
	}

	@FXML
	void onAddTelefonoButtonAction(ActionEvent event) {
		Dialog<Telefono> dialog = new Dialog<>();
		dialog.setTitle("Nuevo teléfono");
		dialog.setHeaderText("Introduzca el nuevo número de teléfono.");

		// PONER ICONO DEL PADRE
		Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
		stage.getIcons().setAll(App.getPrimaryStage().getIcons());

		// BOTONES DIALOGO
		ButtonType addButton = new ButtonType("Añadir", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

		// ELEMENTOS DIALOGO PERSONALIZADO
		GridPane root = new GridPane();
		root.setHgap(10);
		root.setVgap(10);
		root.setPadding(new Insets(20, 150, 10, 10));

		TextField numeroText = new TextField();
		numeroText.setPromptText("Número de teléfono");

		ComboBox<TipoTelefono> tipoCombo = new ComboBox<>();
		tipoCombo.getItems().addAll(TipoTelefono.CASA, TipoTelefono.MOVIL);
		tipoCombo.setPromptText("Seleccione un tipo");

		root.add(new Label("Número:"), 0, 0);
		root.add(numeroText, 1, 0);
		root.add(new Label("Tipo:"), 0, 1);
		root.add(tipoCombo, 1, 1);

		dialog.getDialogPane().setContent(root);

		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == addButton) {
				return new Telefono(numeroText.getText(), tipoCombo.getSelectionModel().getSelectedItem());
			}
			return null;
		});

		Optional<Telefono> result = dialog.showAndWait();
		if (dialog.getResult() != null) {
			model.get().getTelefonos().add(result.get());
		}
	}

	@FXML
	void onAddWebsButtonAction(ActionEvent event) {
		TextInputDialog dialog = new TextInputDialog("http://");
		dialog.setTitle("Nueva web");
		dialog.setHeaderText("Crear una nueva direccion web.");
		dialog.setContentText("URL:");

		// PONER ICONO DEL PADRE
		Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
		stage.getIcons().setAll(App.getPrimaryStage().getIcons());

		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			model.get().getWebs().add(new Webs(result.get()));
		}
	}

	@FXML
	void onDeleteEmailButtonAction(ActionEvent event) {
		Correo email = emailSeleccionado.get();
		Optional<ButtonType> result = Mensajes.confirmacion("Eliminar email",
				"¿Esta seguro de que quiere eliminar este email " + email.getDireccion() + "?");
		if (result.get() == ButtonType.OK) {
			model.get().getEmails().remove(email);
		}
	}

	@FXML
	void onDeleteTelefonoButtonAction(ActionEvent event) {
		Telefono telefono = telefonoSeleccionado.get();
		Optional<ButtonType> result = Mensajes.confirmacion("Eliminar teléfono",
				"¿Esta seguro de que quiere eliminar este teléfono " + telefono.getNumero() + "?");
		if (result.get() == ButtonType.OK) {
			model.get().getTelefonos().remove(telefono);
		}
	}

	@FXML
	void onDeleteWebsButtonAction(ActionEvent event) {
		Webs web = webSeleccionada.get();
		Optional<ButtonType> result = Mensajes.confirmacion("Eliminar web",
				"¿Esta seguro de que quiere eliminar esta web " + web.getUrl() + "?");
		if (result.get() == ButtonType.OK) {
			model.get().getWebs().remove(web);
		}
	}

	public SplitPane getView() {
		return view;
	}

	public final ObjectProperty<Contacto> modelProperty() {
		return this.model;
	}

	public final Contacto getModel() {
		return this.modelProperty().get();
	}

	public final void setModel(final Contacto model) {
		this.modelProperty().set(model);
	}

}
