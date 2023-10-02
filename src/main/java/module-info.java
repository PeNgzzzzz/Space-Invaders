module wwan.a3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;
    requires javafx.media;
    
                            
    opens wwan.a3 to javafx.fxml;
    exports wwan.a3;
}