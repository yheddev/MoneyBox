package moneybox;

import java.util.HashMap;
import java.util.Map;

public class GoalStorage {
    private final Map<Long, Goal> goals = new HashMap<>();

    public void addGoal(Goal goal){
        if (goal == null){
            throw new IllegalArgumentException(
                    "The goal cannot be null."
            );
        }

        if (goals.containsKey(goal.getId())){
            throw new IllegalArgumentException(
                    "The goal with id: " + goal.getId() + " already exists."
            );
        }

        goals.put(goal.getId(), goal);
    }

    public Map<Long, Goal> getGoals(){
        return new HashMap<>(goals);
    }

    public Goal findGoal(long id) throws GoalNotFoundException{
        if (!goals.containsKey(id)){
            throw new GoalNotFoundException(id);
        }
        return goals.get(id);
    }
}
