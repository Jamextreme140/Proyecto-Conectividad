import ui.MainFrame;
import ui.io.InputController;

public class App {
    public static void main(String[] args) throws Exception {
        MainFrame app = new MainFrame();
        InputController ioc = new InputController(app);
        app.showUI();
        app.implementListeners(ioc);
    }
}
