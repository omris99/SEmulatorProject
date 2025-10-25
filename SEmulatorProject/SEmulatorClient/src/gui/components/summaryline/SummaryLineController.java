package gui.components.summaryline;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import types.modeltypes.ArchitectureType;
import types.modeltypes.InstructionType;

import java.util.Map;

public class SummaryLineController {

    @FXML
    private TextField basicInstructionsCount;

    @FXML
    private TextField syntheticInstructionsCount;

    @FXML
    private TextField architectureFourCount;

    @FXML
    private TextField architectureOneCount;

    @FXML
    private TextField architectureThreeCount;

    @FXML
    private TextField architectureTwoCount;


    public void setSummaryLineValues(Map<InstructionType, Integer> instructionsTypeCount,
                                     Map<ArchitectureType, Integer> instructionsCountByArchitecture) {
            this.basicInstructionsCount.setText(String.valueOf(instructionsTypeCount.get(InstructionType.BASIC)));
            this.syntheticInstructionsCount.setText(String.valueOf(instructionsTypeCount.get(InstructionType.SYNTHETIC)));
            this.architectureOneCount.setText(String.valueOf(instructionsCountByArchitecture.get(ArchitectureType.ONE)));
            this.architectureTwoCount.setText(String.valueOf(instructionsCountByArchitecture.get(ArchitectureType.TWO)));
            this.architectureThreeCount.setText(String.valueOf(instructionsCountByArchitecture.get(ArchitectureType.THREE)));
            this.architectureFourCount.setText(String.valueOf(instructionsCountByArchitecture.get(ArchitectureType.FOUR)));
    }

}
