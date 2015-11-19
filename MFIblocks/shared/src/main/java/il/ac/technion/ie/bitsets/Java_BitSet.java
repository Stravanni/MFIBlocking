package il.ac.technion.ie.bitsets;

import il.ac.technion.ie.context.MfiContext;
import il.ac.technion.ie.data.structure.IFRecord;
import il.ac.technion.ie.data.structure.SetPairIF;
import il.ac.technion.ie.model.BitSetIF;

import javax.transaction.NotSupportedException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;


public class Java_BitSet implements BitSetIF {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3694882243554443784L;
	private BitSet bs = null;
	public Java_BitSet(){
		bs = new BitSet();
	}
	
	public Java_BitSet(int size){
		bs = new BitSet(size);
	}
	
	@Override
	public BitSetIF and(final BitSetIF other) {
		Java_BitSet jother = (Java_BitSet)other;
		bs.and(jother.bs);
		return this;
	}

	@Override
	public boolean get(int recordId) {
		return bs.get(recordId);
	}

	@Override
	public int getCardinality() {
		return bs.cardinality();
	}

	@Override
	public String getSupportString() {
		return bs.toString();
	}

	@Override
	public void set(int recordId) {
		bs.set(recordId);
	}

	@Override
	public void clearAll() {
		bs.clear();
	}

	@Override
	public BitSetIF or(final BitSetIF other) throws NotSupportedException {
		bs.or(((Java_BitSet)other).bs);
		return this;
	}

	@Override
	public List<IFRecord> getRecords() {
        List<IFRecord> retVal = new ArrayList<>(bs.cardinality());
        MfiContext mfiContext = MfiContext.getInstance();
        for (int i = bs.nextSetBit(1); i >= 0; i = bs.nextSetBit(i + 1)) {
            retVal.add(mfiContext.getRecordByKey(i));
        }
        return retVal;
			
	}
	
	public int markPairs(SetPairIF spf, double score) {
		int cnt =0;
		for(int i=bs.nextSetBit(0); i>=0; i=bs.nextSetBit(i+1)) {
			for(int j=bs.nextSetBit(i+1); j>=0; j=bs.nextSetBit(j+1)) {
				spf.setPair(i, j,score);
				cnt++;
			}			
		}
		return cnt;
	}

	@Override
	public void orInto(BitSetIF other) {
		for(int i=bs.nextSetBit(0); i>=0; i=bs.nextSetBit(i+1)) {
			other.set(i);
		}
		
	}

	@Override
	public List<Integer> getColumns() {
		List<Integer> retVal = new ArrayList<Integer>(bs.cardinality());
		for(int i=bs.nextSetBit(1); i>=0; i=bs.nextSetBit(i+1)){
			retVal.add(i);
		}
		return retVal;
	}

	@Override
	public int markPairs(SetPairIF spf, double score, List<Integer> items) {
		int cnt =0;
		for(int i=bs.nextSetBit(0); i>=0; i=bs.nextSetBit(i+1)) {
			for(int j=bs.nextSetBit(i+1); j>=0; j=bs.nextSetBit(j+1)) {
				spf.setPair(i, j,score);
				spf.setColumnsSupport(items,i,j);
				cnt++;
			}			
		}
		return cnt;
	}

}
