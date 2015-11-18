package il.ac.technion.ie.model;


import il.ac.technion.ie.context.MfiContext;
import il.ac.technion.ie.utils.StringSimTools;

import java.io.Serializable;


public class FrequentItem implements Comparable<FrequentItem>,Serializable{

	private static final long serialVersionUID = 1L;
	private int id;
	private String item;
	private BitSetIF columns;
	//protected BitSet support ;
	protected BitSetIF support ;
	private double weight = 1.0;
	private double log2IDF = -1;
	long supportSize = -1;
	private BitSetFactory factory;
	
	
	public FrequentItem(int id, String item, BitSetFactory factory){
		this.id = id;
		this.item = item;
		this.factory = factory;
		this.support = factory.createInstance();
		this.columns=factory.createInstance();
	}
	
	public FrequentItem(int id, String item, double weight,BitSetFactory factory){
		this(id,item,factory);
		this.weight = weight;
	}
	
	protected FrequentItem(int id, String item, double weight, boolean createBS, BitSetFactory factory){
		this.id = id;
		this.item = item;
		this.weight = weight;
		this.factory = factory;
		if(createBS){
			this.support = factory.createInstance();
		}
		this.columns=factory.createInstance();
	}

	public BitSetIF getSupport(){
		return support;
	}
	public BitSetIF getColumns(){
		return columns;
	}
	
	public double getWeight(){
		return weight;
	}

	public int getId(){
		return id;
	}
	
	public void addSupport(int recordId){		
		support.set(recordId);
	}
	public void addColumn(int id){		
		columns.set(id);
	}
	
	public String getItem(){
		return item;
	}
	
	public int hashCode(){
		return id;
	}
	
	public int getSupportSize(){
		return support.getCardinality();
		
				
	}
	
	public boolean equals(Object obj){
		if(obj == null)
			return false;
		if(obj.hashCode() != this.hashCode()){
			return false;
		}
		return ((FrequentItem)obj).getItem().equals(this.getItem());
		
	}
	
	public String getSupportString(){
		return support.getSupportString();
		
	}
	public double getLog2IDF(){
		return log2IDF;
	}
	
	public void setIDFWeight(){
        int dbSize = MfiContext.getInstance().getDBSize();
        log2IDF = ((double) dbSize / getSupportSize());
        log2IDF = StringSimTools.logBase2(log2IDF);
	}
	
	public String toString(){
		StringBuilder sb= new StringBuilder();
		sb.append("Item ").append(id).append(" value=").append(item)
			.append(" support size= ").append(getSupportSize());
		return sb.toString();
	}

	@Override
	public int compareTo(FrequentItem o) {
		if(this == o)
			return 0;		
		long supportDiff = getSupportSize() - o.getSupportSize();
		if(supportDiff == 0){
			return this.id - o.getId();
		}
		return (supportDiff>0)? 1:-1;		
	}

	public String getColumnsString() {
		return columns.getSupportString();
	}
}
