module com.simonhochrein.rconnclient {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.simonhochrein.rconclient to javafx.fxml;
    exports com.simonhochrein.rconclient;
}