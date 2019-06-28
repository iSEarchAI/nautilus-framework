package thiagodnf.nautilus.plugin.extension.problem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.solution.IntegerSolution;

import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.remover.AbstractRemover;
import thiagodnf.nautilus.core.remover.BinarySolutionRemover;
import thiagodnf.nautilus.core.remover.IntegerSolutionRemover;
import thiagodnf.nautilus.core.util.Converter;
import thiagodnf.nautilus.plugin.extension.ProblemExtension;

public abstract class AbstractProblemExtension implements ProblemExtension {

	@Override
	public final String getId() {
		return Converter.toKey(getName());
	}
	
	public AbstractObjective getObjectiveById(String objectiveId) {

		for (AbstractObjective objective : getObjectives()) {

			if (objective.getId().equalsIgnoreCase(objectiveId)) {
				return objective;
			}
		}

		return null;
	}
	
	public List<AbstractObjective> getObjectiveByIds(List<String> objectiveIds) {

		List<AbstractObjective> selected = new ArrayList<>();

		for (String objectiveId : objectiveIds) {

			AbstractObjective objective = getObjectiveById(objectiveId);

			if (objective != null) {
				selected.add(objective);
			}
		}

		return selected;
	}
	
    public Map<String, List<AbstractObjective>> getObjectiveGroups() {

        Map<String, List<AbstractObjective>> objectiveGroups = new HashMap<>();

        for (AbstractObjective objective : getObjectives()) {

            if (!objectiveGroups.containsKey(objective.getGroupName())) {
                objectiveGroups.put(objective.getGroupName(), new ArrayList<>());
            }

            objectiveGroups.get(objective.getGroupName()).add(objective);
        }

        return objectiveGroups;
    }
	
    @Override
    public AbstractRemover getRemover() {

        if (supports() == IntegerSolution.class) {
            return new IntegerSolutionRemover();
        } else if (supports() == BinarySolution.class) {
            return new BinarySolutionRemover();
        }

        return null;
    }
}
