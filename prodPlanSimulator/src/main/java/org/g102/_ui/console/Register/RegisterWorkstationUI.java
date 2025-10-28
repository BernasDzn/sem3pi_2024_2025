package org.g102._ui.console.Register;

import org.g102._controllers.Register.RegisterWorkstationController;
import org.g102.domain.Workstation;
import org.g102.tools.ConsoleWriter;
import org.g102.tools.InputReader;

import java.util.List;

public class RegisterWorkstationUI implements Runnable{

    RegisterWorkstationController controller;

    public RegisterWorkstationUI() {
        controller = new RegisterWorkstationController();
    }

    @Override
    public void run() {
        List<Workstation> workstation_type_names = controller.getWorkstationTypes();

        if(!workstation_type_names.isEmpty()){
            String workstation_id = InputReader.read("Workstation ID");
            String workstation_name = InputReader.read("Name of the workstation");
            String workstation_description = InputReader.read("Description of the workstation");
            int workstation_type_index = InputReader.showListReturnIndex(workstation_type_names, "Select the type of workstation");
            controller.registerWorkstation(workstation_id, workstation_name, workstation_description, workstation_type_names.get(workstation_type_index - 1));
        }else {
            ConsoleWriter.displayError("No workstation types found.");
        }

    }

}
