package fimEntityResolution.entityResulution;

import candidateMatches.CandidatePairs;

public interface IComparison {
	public long measureComparisonExecution(
			CandidatePairs groundTruthCandidatePairs,
			CandidatePairs algorithmObtainedPairs);
}