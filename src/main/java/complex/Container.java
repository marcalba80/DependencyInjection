package complex;

import common.DependencyException;

import java.util.HashMap;

public class Container implements Injector {

    private boolean REGISTERED = true;
    private HashMap<Class, Object> objectHashMap;
    private HashMap<Class, complex.Factory> factoryHashMap;
    private HashMap<Class, complex.Factory> singletonHashMap;
    private HashMap<Class, Class[]> dependencesHashMap;

    public Container(){
        this.objectHashMap = new HashMap<>();
        this.factoryHashMap = new HashMap<>();
        this.singletonHashMap = new HashMap<>();
        this.dependencesHashMap = new HashMap<>();
    }

    @Override
    public <E> void registerConstant(Class<E> name, E value) throws DependencyException {
        if (this.objectHashMap.containsKey(name)){
            if (REGISTERED) System.err.println("ERROR: Constant " + name + " is already registered");
            throw new DependencyException("Constant " + name + " is already registered");
        }else{
            this.objectHashMap.put(name, value);
            if (REGISTERED)System.out.println("Name " + name + " registered with value " + value);
        }
    }

    @Override
    public <E> void registerFactory(Class<E> name, Factory<? extends E> creator, Class<?>... parameters) throws DependencyException {
        if (this.factoryHashMap.containsKey(name)){
            if (REGISTERED)System.err.println("ERROR: Factory " + name + " is already registered");
            throw new DependencyException("Factory " + name + " is already registered");
        }else{
            if (REGISTERED)System.out.println("Registering factory " + name);
            this.factoryHashMap.put(name, creator);
            if (REGISTERED) System.out.println("Factory " + name + " successfully registered");
            if (REGISTERED)System.out.println("Registering factory dependencies with factory " + name);
            this.dependencesHashMap.put(name, parameters);
            if (REGISTERED)System.out.println("Successfully registered dependencies for factory " + name);
        }
    }

    @Override
    public <E> void registerSingleton(Class<E> name, Factory<? extends E> creator, Class<?>... parameters) throws DependencyException {
        if (this.singletonHashMap.containsKey(name)){
            if (REGISTERED) System.err.println("ERROR: Singleton " + name + " is already registered");
            throw new DependencyException("Singleton " + name + " is already registered");
        }else{
            if (REGISTERED)System.out.println("Registering singleton " + name);
            this.singletonHashMap.put(name, creator);
            if (REGISTERED) System.out.println("Singleton factory " + name + " successfully registered");
            if (REGISTERED)System.out.println("Registering factory dependencies with factory " + name);
            this.dependencesHashMap.put(name,parameters);
            if (REGISTERED)System.out.println("Successfully registered dependencies for factory " + name);
        }
    }

    @Override
    public <E> E getObject(Class<E> name) throws DependencyException {
        if (this.objectHashMap.containsKey(name)){
            return (E)this.objectHashMap.get(name);
        }else if(this.factoryHashMap.containsKey(name)){
            return (E)this.createFactory(name);
        }else if (this.singletonHashMap.containsKey(name)){
            return (E) this.createSingletonFactory(name);
        }else {
            throw new DependencyException(name + " has not been registered");
        }
    }

    private <E> Object createFactory(Class <E> name) throws DependencyException{
        try {
            Factory creator;
            creator = this.factoryHashMap.get(name);
            Object[] object = new Object[this.dependencesHashMap.get(name).length];
            for (int i=0; i<this.dependencesHashMap.get(name).length; i++){
                object[i] = this.getObject(this.dependencesHashMap.get(name)[i]);
            }
            return creator.create(object);
        }catch(DependencyException ex){
            if (REGISTERED) System.err.println("ERROR: Factory " + name + " can't be created");
            throw new DependencyException(ex);
        }
    }

    private <E> Object createSingletonFactory(Class <E> name) throws DependencyException{
        try {
            Factory creator;
            creator = this.singletonHashMap.get(name);
            Object[] object = new Object[this.dependencesHashMap.get(name).length];
            for (int i=0; i<this.dependencesHashMap.get(name).length; i++){
                object[i] = this.getObject(this.dependencesHashMap.get(name)[i]);
            }
            return creator.create(object);
        }catch(DependencyException ex){
            if (REGISTERED) System.err.println("ERROR: Factory " + name + " can't be created");
            throw new DependencyException(ex);
        }
    }

}
