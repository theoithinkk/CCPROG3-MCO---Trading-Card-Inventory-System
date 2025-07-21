package com.tciss;

public class BinderManager extends CardContainerManager<Binder>{
	public BinderManager(int maxBinders) {  // inherits the constructors and methods from the superclass, CardContainerManager
		super(maxBinders);
	}
	
	protected String getContainerTypeName() {
		return "Binder";
	}
	
	protected Binder createNewContainer(String name) {
		return new Binder(name);
	}
	
	protected Binder[] createContainerArray() {
		return new Binder[MAX_CONTAINERS];
	}
	
}
