package agh.ics.project;

import java.util.Map;

public interface IEngineObserver {

    void makeMoves(boolean isTorus, Map<String, Double> stats, boolean magicHappened);
}
