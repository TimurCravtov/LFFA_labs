package md.utm.lab2;

import md.utm.lab1.FiniteAutomaton;
import md.utm.lab1.State;
import md.utm.lab1.Transition;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

public class VisualisationService {

    record CustomState(String label, String type) {}

    record CustomTransition(String from, String to, String label) {}

    static class VisualData {
        List<CustomState> states;
        List<CustomTransition> transitions;
    }

    public static void visualise(FiniteAutomaton finiteAutomaton, String outputDir) {
        List<CustomState> states = finiteAutomaton.getQ().stream().map(state -> {
            String type;
            boolean isStart = state.equals(finiteAutomaton.getQ0());
            boolean isFinal = finiteAutomaton.getF().contains(state);
            if (isStart && isFinal) {
                type = "startandfinal";
            } else if (isStart) {
                type = "start";
            } else if (isFinal) {
                type = "final";
            } else {
                type = "general";
            }
            return new CustomState(state.getStateName(), type);
        }).toList();

        List<CustomTransition> transitions = finiteAutomaton.getDeltaTransitions().stream()
                .collect(Collectors.groupingBy(t -> new AbstractMap.SimpleEntry<>(t.getFrom(), t.getTo())))
                .entrySet().stream()
                .map(entry -> new CustomTransition(
                        entry.getKey().getKey().getStateName(),
                        entry.getKey().getValue().getStateName(),
                        entry.getValue().stream().map(t -> t.getLabel().toString()).collect(Collectors.joining("|"))
                )).toList();

        JSONArray statesArray = new JSONArray();
        for (CustomState state : states) {
            JSONObject stateObject = new JSONObject();
            stateObject.put("label", state.label());
            stateObject.put("type", state.type());
            statesArray.put(stateObject);
        }

        JSONArray transitionsArray = new JSONArray();
        for (CustomTransition transition : transitions) {
            JSONObject transitionObject = new JSONObject();
            transitionObject.put("from", transition.from());
            transitionObject.put("to", transition.to());
            transitionObject.put("label", transition.label());
            transitionsArray.put(transitionObject);
        }

        JSONObject visualData = new JSONObject();
        visualData.put("states", statesArray);
        visualData.put("transitions", transitionsArray);

        try {
            Files.createDirectories(Path.of(outputDir));
            FileWriter writer = new FileWriter("src/main/resources/visualisation_fa/data.json");
            writer.write(visualData.toString(4));
            writer.close();

            String url = "src/main/resources/visualisation_fa/index.html";
            File htmlFile = new File(url);
            Desktop.getDesktop().browse(htmlFile.toURI());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
