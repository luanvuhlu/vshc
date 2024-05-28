module com.luanvv.http.client.vshc {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;

    requires org.kordamp.bootstrapfx.core;

    opens com.luanvv.http.client.vshc to javafx.fxml;
    exports com.luanvv.http.client.vshc;
}