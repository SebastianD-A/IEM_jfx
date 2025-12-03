package A2;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class IEMRun extends Application{
    
    @Override
    public void start(Stage primaryStage){
        primaryStage.setTitle("IEM Store");

        IEMModel model = new IEMModel();
        IEMController controller = new IEMController(model);
        IEMView view = new IEMView(controller, model, primaryStage);
        
        Scene scene = new Scene(view.asParent(), 700, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
