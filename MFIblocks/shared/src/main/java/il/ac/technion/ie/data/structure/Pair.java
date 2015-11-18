package il.ac.technion.ie.data.structure;

import il.ac.technion.ie.context.MfiContext;
import il.ac.technion.ie.model.MfiRecord;
import il.ac.technion.ie.utils.StringSimTools;
import il.ac.technion.ie.utils.Utilities;

public class Pair {

	public int r1;
	public int r2;
	private double score = -1;
	private boolean covered = false;
	
	public Pair(int r1, int r2){
		this.r1 = r1;
		this.r2 = r2;
	}
	
	public boolean equals(Object other){
		Pair otherPair = (Pair)other;
		if(otherPair.r1 == r1 && otherPair.r2 == r2){
			return true;
		}
		if(otherPair.r1 == r2 && otherPair.r2 == r1){
			return true;
		}
		
		return false;
	}
	
	public int hashCode(){
		return r1 ^ r2;
	}
	
	public double getScore(){
		if(score < 0){
            score = StringSimTools.softTFIDF(this.getRecordOne(), this.getRecordTwo());
        }
		return score;
	}
	
	public void setCovered(boolean val){
		this.covered = val;
	}
	
	public boolean getCovered(){
		return covered;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
        sb.append(this.getRecordOne().toString()).append(Utilities.NEW_LINE).
                append(this.getRecordTwo().toString()).append(Utilities.NEW_LINE);
        sb.append("with score: " + getScore());
		return sb.toString();
		
	}
	
	public boolean sameSource(){
        String src1 = this.getRecordOne().getSrc();
        String src2 = this.getRecordTwo().getSrc();
        if(src1 == null || src2 == null){ //if null then assume different sources
			return false;
		}
		return (src1.equalsIgnoreCase(src2));
	}
	
	public String simpleToString(){
		StringBuilder sb = new StringBuilder();
        sb.append(this.getRecordOne().getRecordStr()).append(Utilities.NEW_LINE)
                .append(this.getRecordTwo().getRecordStr()).append(Utilities.NEW_LINE);
        return sb.toString();
	}

    private final MfiRecord getRecordOne() {
        MfiContext mfiContext = MfiContext.getInstance();
        return mfiContext.getRecordByKey(r1);
    }

    private final MfiRecord getRecordTwo() {
        MfiContext mfiContext = MfiContext.getInstance();
        return mfiContext.getRecordByKey(r2);
    }
}
