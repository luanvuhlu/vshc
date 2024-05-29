module com.luanvv.http.client.vshc {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;

    requires org.kordamp.bootstrapfx.core;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;

    opens com.luanvv.http.client.vshc to javafx.fxml;
    opens com.luanvv.http.client.vshc.controllers to javafx.fxml;
    opens com.luanvv.http.client.vshc.components to javafx.fxml;
    opens com.luanvv.http.client.vshc.models to javafx.base;
    opens com.luanvv.http.client.vshc.models.postman to com.fasterxml.jackson.databind;
    exports com.luanvv.http.client.vshc;
}